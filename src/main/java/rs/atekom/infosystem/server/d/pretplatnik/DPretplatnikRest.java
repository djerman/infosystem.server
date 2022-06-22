package rs.atekom.infosystem.server.d.pretplatnik;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.a.tipbrojaca.ATipBrojaca;
import rs.atekom.infosystem.baza.d.pretplatnik.DPodaciZaPretplatnikaOdgovor;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnikOdgovor;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnikPodaciOdgovor;
import rs.atekom.infosystem.baza.e.organizacija.EOrganizacija;
import rs.atekom.infosystem.baza.f.brojac.FBrojac;
import rs.atekom.infosystem.baza.i.IAdresa;
import rs.atekom.infosystem.server.OsnovniRest;
import rs.atekom.infosystem.server.a.tipbrojaca.ATipBrojacaRepo;
import rs.atekom.infosystem.server.e.organizacija.EOrganizacijaRepo;
import rs.atekom.infosystem.server.f.brojac.FBrojacRepo;
import rs.atekom.infosystem.server.i.adresa.IAdresaRepo;

@RestController
@Validated
public class DPretplatnikRest extends OsnovniRest{

	@Autowired
	private DPretplatnikRepo repo;
	@Autowired
	private DPretplatnikService service;
	@Autowired
	private EOrganizacijaRepo repoOrganizacija;
	@Autowired
	private IAdresaRepo repoAdresa;
	@Autowired
	private ATipBrojacaRepo repoTipBrojaca;
	@Autowired
	private FBrojacRepo repoBrojac;
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA')")
	@GetMapping("/pretplatnici")
	public ResponseEntity<DPretplatnikOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga, 
			@RequestParam(value = "agencijaId") Optional<Long> agencijaId){
		try {
			return new ResponseEntity<DPretplatnikOdgovor>(service.lista(pretraga, agencijaId), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@GetMapping("/pretplatnici/podaci")
	public ResponseEntity<DPodaciZaPretplatnikaOdgovor> podaci(){
		try {
			return new ResponseEntity<DPodaciZaPretplatnikaOdgovor>(service.podaciZaPretplatnika(), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@PutMapping("/pretplatnik/snimi")
	@Transactional
	public ResponseEntity<DPretplatnikOdgovor> snimiIzmeni(@RequestBody DPretplatnikPodaciOdgovor noviPretplatnik){
		List<DPretplatnik> lista = new ArrayList<DPretplatnik>();
		lista.addAll(repo.pretragaSvih(noviPretplatnik.getPretplatnik().getMb()));
		if(repo.pretragaSvih(noviPretplatnik.getPretplatnik().getPib()) != null && repo.pretragaSvih(noviPretplatnik.getPretplatnik().getPib()).size() > 0) {
			for(DPretplatnik pretpl : repo.pretragaSvih(noviPretplatnik.getPretplatnik().getPib())) {
				if(!lista.contains(pretpl)) {
					lista.add(pretpl);
					}
				}
			}
		try {
			return repo.findById(noviPretplatnik.getPretplatnik().getId() == null ? 0L : noviPretplatnik.getPretplatnik().getId())
					.map(pretplatnik -> {
						if(lista.size() == 1 && lista.get(0).getId().equals(noviPretplatnik.getPretplatnik().getId())) {
							if(pretplatnik.getVerzija().equals(noviPretplatnik.getPretplatnik().getVerzija())) {
								pretplatnik = noviPretplatnik.getPretplatnik();
								pretplatnik.setVerzija(pretplatnik.getVerzija() + 1);
								pretplatnik = service.sacuvajPretplatnika(pretplatnik);
								
								IAdresa adresa = noviPretplatnik.getOrganizacija().getAdresa();
								adresa.setVerzija(pretplatnik.getVerzija());
								adresa = repoAdresa.save(adresa);
								//napravi organizaciju
								EOrganizacija organizacija = noviPretplatnik.getOrganizacija();
								organizacija.setVerzija(pretplatnik.getVerzija());
								organizacija.setAdresa(adresa);
								organizacija = repoOrganizacija.save(organizacija);
								
								return new ResponseEntity<DPretplatnikOdgovor>(service.lista(null, null), HttpStatus.ACCEPTED);
								}else {
									return new ResponseEntity<DPretplatnikOdgovor>(HttpStatus.MULTI_STATUS);
									}
							}else {
								return new ResponseEntity<DPretplatnikOdgovor>(new DPretplatnikOdgovor(), HttpStatus.ALREADY_REPORTED);
								}
						})
					.orElseGet(() -> {
						if(lista.size() < 1) {
							DPretplatnik pretplatnik = noviPretplatnik.getPretplatnik();
							EOrganizacija organizacija = noviPretplatnik.getOrganizacija();
							IAdresa adresa = organizacija.getAdresa();
							
							pretplatnik.setIzbrisan(false);
							pretplatnik.setVerzija(0);
							pretplatnik = service.sacuvajPretplatnika(pretplatnik);
							
							adresa.setPretplatnik(pretplatnik);
							adresa.setIzbrisan(false);
							adresa.setVerzija(0);
							
							organizacija.setPretplatnik(pretplatnik);
							organizacija.setIzbrisan(false);
							organizacija.setSediste(true);
							organizacija.setVerzija(0);
							
							organizacija.setAdresa(repoAdresa.save(adresa));
							repoOrganizacija.save(organizacija);
							//sacuvaj brojace
							if(pretplatnik.getId() == null) {
								List<ATipBrojaca> tipovi = repoTipBrojaca.findAll();
								for(ATipBrojaca tip : tipovi) {
									FBrojac brojac = new FBrojac();
									brojac.setPretplatnik(pretplatnik);
									brojac.setTip(tip);
									brojac.setPrefiks("");
									brojac.setBrojPolja(4);
									brojac.setStanje(1);
									brojac.setSufiks("");
									brojac.setReset(false);
									brojac.setVerzija(0);
									repoBrojac.save(brojac);
								}
							}
							
							return new ResponseEntity<DPretplatnikOdgovor>(service.lista(null, null), HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<DPretplatnikOdgovor>(new DPretplatnikOdgovor(), HttpStatus.ALREADY_REPORTED);
								}
						});
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@DeleteMapping("/pretplatnik/brisi/{id}")
	@Transactional
	public ResponseEntity<DPretplatnikOdgovor>  brisi(@PathVariable Long id){
		DPretplatnik pretplatnik = repo.findById(id).get();
		if(pretplatnik != null) {
			List<EOrganizacija> organizacije = repoOrganizacija.findByPretplatnik(pretplatnik);
			List<IAdresa> adrese = new ArrayList<IAdresa>();
			for(EOrganizacija org : organizacije) {
				adrese.add(org.getAdresa());
				}
			try {
				repoOrganizacija.deleteAll(organizacije);
				repoAdresa.deleteAll(adrese);
				repo.delete(pretplatnik);
				return new ResponseEntity<DPretplatnikOdgovor>(service.lista(null, null), HttpStatus.ACCEPTED);
				}catch (Exception e) {
					try {
						repoOrganizacija.deleteAll(organizacije);
						repoAdresa.deleteAll(adrese);
						pretplatnik.setIzbrisan(true);
						repo.save(pretplatnik);
						return new ResponseEntity<DPretplatnikOdgovor>(service.lista(null, null), HttpStatus.ACCEPTED);
						}catch (Exception ee) {
							pretplatnik.setIzbrisan(true);
							for(EOrganizacija org : organizacije) {
								org.setIzbrisan(true);
								repoOrganizacija.save(org);
								}
							for(IAdresa adr : adrese) {
								adr.setIzbrisan(true);
								repoAdresa.save(adr);
								}
							ee.printStackTrace();
							return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
							}
					}
			}else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
