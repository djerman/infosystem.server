package rs.atekom.infosystem.server.e.organizacija;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.e.organizacija.EOrganizacija;
import rs.atekom.infosystem.baza.e.organizacija.EOrganizacijaOdgovor;
import rs.atekom.infosystem.baza.f.brojac.FBrojac;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.tipbrojaca.ATipBrojacaRepo;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;
import rs.atekom.infosystem.server.f.brojac.FBrojacRepo;

@Service
public class EOrganizacijaService extends OsnovniService{

	@Autowired
	private EOrganizacijaRepo repo;
	@Autowired
	private DPretplatnikRepo repoPretplatnik;
	@Autowired
	private FBrojacRepo repoBrojac;
	@Autowired
	private ATipBrojacaRepo repoTipBrojaca;
	
	public EOrganizacijaOdgovor lista(Optional<String> pretraga, Optional<Long> pretplId) {
		EOrganizacijaOdgovor odgovor = new EOrganizacijaOdgovor();
		try {
			String pojam = null;
			Long pretplatnikId = null;
			if(pretraga != null && pretraga.isPresent()) {
				pojam = pretraga.get();
			}
			if(pretplId != null && pretplId.isPresent())
				pretplatnikId = pretplId.get();
			odgovor.setLista(repo.pretragaOrganizacija(pojam, pretplatnikId));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return odgovor;
	}
	
	public List<EOrganizacija> poPretplatniku(Long pretplathnikId){
		return repo.findByPretplatnikAndIzbrisanFalse(repoPretplatnik.findById(pretplathnikId).get());
		}
	
	@Transactional
	public ResponseEntity<EOrganizacijaOdgovor> snimi(EOrganizacija novaOrganizacija){
		List<EOrganizacija> postojeci = repo.findByPretplatnikAndIzbrisanFalseAndSifraOrNaziv(novaOrganizacija.getPretplatnik(),
				novaOrganizacija.getSifra(), novaOrganizacija.getNaziv());
		EOrganizacijaOdgovor odgovor = new EOrganizacijaOdgovor();
		return repo.findById(novaOrganizacija.getId() != null ? novaOrganizacija.getId() : 0L)
				.map(organizacija -> {
					try {
						Boolean postoji = false;
						if(postojeci.size() > 0) {
							for(EOrganizacija org : postojeci) {
								if(!org.getId().equals(novaOrganizacija.getId()) && !org.getIzbrisan()) {
									postoji = true;
								}
							}
						}
						if(postoji) {
							return new ResponseEntity<EOrganizacijaOdgovor>(odgovor, HttpStatus.ALREADY_REPORTED);
						}else {
							if(novaOrganizacija.getVerzija() == organizacija.getVerzija()) {
								if(novaOrganizacija.getSifra() == null || novaOrganizacija.getSifra().equals("") 
										|| novaOrganizacija.getSifra().isBlank() || novaOrganizacija.getSifra().isEmpty()) {
									if(organizacija.getSifra() != null && !organizacija.getSifra().equals("")
											&& !organizacija.getSifra().isBlank() && !organizacija.getSifra().isEmpty()) {
										novaOrganizacija.setSifra(organizacija.getSifra());
									}else {
										postaviBrojac(novaOrganizacija);
									}
								}
								organizacija = novaOrganizacija;
								organizacija.setVerzija(organizacija.getVerzija() + 1);
								if(organizacija.getSediste() == null)
									organizacija.setSediste(false);
								if(organizacija.getIzbrisan() == null)
									organizacija.setIzbrisan(false);
								repo.save(organizacija);
								odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalse(organizacija.getPretplatnik()));
								return new ResponseEntity<EOrganizacijaOdgovor>(odgovor, HttpStatus.ACCEPTED);
							}else{
								return new ResponseEntity<EOrganizacijaOdgovor>(odgovor, HttpStatus.MULTI_STATUS);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						return new ResponseEntity<EOrganizacijaOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}).orElseGet(() -> {
					try {
						if(postojeci != null && postojeci.size() > 0) {
							return new ResponseEntity<EOrganizacijaOdgovor>(odgovor, HttpStatus.ALREADY_REPORTED);
						}else {
							if(novaOrganizacija.getNaziv() == null || novaOrganizacija.getNaziv().equals("") || novaOrganizacija.getPretplatnik() == null) {
								return new ResponseEntity<EOrganizacijaOdgovor>(odgovor, HttpStatus.FORBIDDEN);
							}else {
								novaOrganizacija.setVerzija(0);
								novaOrganizacija.setIzbrisan(false);
								if(novaOrganizacija.getSifra() == null || novaOrganizacija.getSifra().equals("")
										|| novaOrganizacija.getSifra().isBlank() || novaOrganizacija.getSifra().isEmpty());
										postaviBrojac(novaOrganizacija);
								repo.save(novaOrganizacija);
								odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalse(novaOrganizacija.getPretplatnik()));
								return new ResponseEntity<EOrganizacijaOdgovor>(odgovor, HttpStatus.ACCEPTED);
							}
						}
					}catch (Exception e) {
						return new ResponseEntity<EOrganizacijaOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				});
	}
	
	@Transactional
	public ResponseEntity<EOrganizacijaOdgovor> brisi(Long organizacijaId){
		if(organizacijaId != null) {
			EOrganizacijaOdgovor odgovor = new EOrganizacijaOdgovor();
			EOrganizacija organizacija = repo.findById(organizacijaId).get();
			if(organizacija != null) {
				try {
					repo.delete(organizacija);
					odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalse(organizacija.getPretplatnik()));
					return new ResponseEntity<EOrganizacijaOdgovor>(odgovor, HttpStatus.ACCEPTED);
				}catch (Exception e) {
					try {
						organizacija.setIzbrisan(false);
						repo.save(organizacija);
						odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalse(organizacija.getPretplatnik()));
						return new ResponseEntity<EOrganizacijaOdgovor>(odgovor, HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
	
	public EOrganizacija sacuvajSediste(EOrganizacija organizacija) {
		postaviBrojac(organizacija);
		return repo.save(organizacija);
	}
	
	private void postaviBrojac(EOrganizacija organizacija) {
		FBrojac brojac = repoBrojac.findByPretplatnikAndTip(organizacija.getPretplatnik(), repoTipBrojaca.findByTip(1));
		String broj = String.format("%0" + brojac.getBrojPolja() + "d", brojac.getStanje());
		organizacija.setSifra(broj);
		brojac.setStanje(brojac.getStanje() + 1);
		repoBrojac.save(brojac);
	}
	
	}
