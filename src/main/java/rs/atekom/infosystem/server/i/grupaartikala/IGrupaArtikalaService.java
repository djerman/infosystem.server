package rs.atekom.infosystem.server.i.grupaartikala;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.f.brojac.FBrojac;
import rs.atekom.infosystem.baza.i.grupaartikala.IGrupaArtikala;
import rs.atekom.infosystem.baza.i.grupaartikala.IGrupaArtikalaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.tipbrojaca.ATipBrojacaRepo;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;
import rs.atekom.infosystem.server.e.konto.EKontoRepo;
import rs.atekom.infosystem.server.f.brojac.FBrojacRepo;

@Service
public class IGrupaArtikalaService extends OsnovniService{

	@Autowired
	private IGrupaArtikalaRepo repo;
	@Autowired
	private DPretplatnikRepo repoPretplatnik;
	@Autowired
	private FBrojacRepo repoBrojac;
	@Autowired
	private ATipBrojacaRepo repoTipBrojaca;
	@Autowired
	private EKontoRepo repoKonto;
	
	public IGrupaArtikalaOdgovor lista(Long pretplatnikId) {
		try {
			IGrupaArtikalaOdgovor odgovor = new IGrupaArtikalaOdgovor();
			DPretplatnik pretplatnik = repoPretplatnik.findById(pretplatnikId).get();
			odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(pretplatnik));
			odgovor.setKonta(repoKonto.findByPretplatnikOrPretplatnikIsNullAndIzbrisanFalseOrderBySifraAsc(pretplatnik));
			return odgovor;
			}catch (Exception e) {
				e.printStackTrace();
				return  null;
				}
		}
	
	@Transactional
	public ResponseEntity<IGrupaArtikalaOdgovor> snimi(IGrupaArtikala nova) {
		List<IGrupaArtikala> postojeci = repo.findByPretplatnikAndIzbrisanFalseAndSifraAndNaziv(nova.getPretplatnik(), 
				nova.getSifra(), nova.getNaziv());
		return repo.findById(nova.getId() == null ? 0 : nova.getId())
				.map(grupa -> {
					try {
						Boolean postoji = false;
						if(postojeci.size() > 0) {
							for(IGrupaArtikala gr : postojeci) {
								if(!gr.getId().equals(nova.getId()) && !gr.getIzbrisan()) {
									postoji = true;
								}
							}
						}
						if(postoji) {
							return new ResponseEntity<IGrupaArtikalaOdgovor>(HttpStatus.ALREADY_REPORTED);
						}else {
							if(nova.getVerzija() == grupa.getVerzija()) {
								if(nova.getSifra() == null || nova.getSifra().equals("") 
										|| nova.getSifra().isBlank() || nova.getSifra().isEmpty()) {
									if(grupa.getSifra() != null && !grupa.getSifra().equals("")
											&& !grupa.getSifra().isBlank() && !grupa.getSifra().isEmpty()) {
										nova.setSifra(grupa.getSifra());
									}else {
										postaviBrojac(nova);
									}
								}
								grupa = nova;
								grupa.setVerzija(nova.getVerzija() + 1);
								return new ResponseEntity<IGrupaArtikalaOdgovor>(lista(repo.save(grupa).getPretplatnik().getId()), 
										HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<IGrupaArtikalaOdgovor>(HttpStatus.MULTI_STATUS);
							}
						}
						
						}catch (Exception e) {
							return new ResponseEntity<IGrupaArtikalaOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
							}
					}).orElseGet(() -> {
						try {
							nova.setId(null);
							nova.setIzbrisan(false);
							nova.setVerzija(0);
							if(nova.getSifra() == null || nova.getSifra().equals("") 
									|| nova.getSifra().isBlank() || nova.getSifra().isEmpty()) {
								postaviBrojac(nova);
							}
							return new ResponseEntity<IGrupaArtikalaOdgovor>(lista(repo.save(nova).getPretplatnik().getId()), HttpStatus.ACCEPTED);
							}catch (Exception e) {
								return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
								}
						});
		}
	
	@Transactional
	public ResponseEntity<IGrupaArtikalaOdgovor> izbrisi(Long grupaId){
		IGrupaArtikala grupa = repo.findById(grupaId).get();
		try {
			repo.delete(grupa);
			return new ResponseEntity<IGrupaArtikalaOdgovor>(lista(grupa.getPretplatnik().getId()), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					grupa.setIzbrisan(true);
					return new ResponseEntity<IGrupaArtikalaOdgovor>(lista(repo.save(grupa).getPretplatnik().getId()), HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	
	private void postaviBrojac(IGrupaArtikala grupa) {
		FBrojac brojac = repoBrojac.findByPretplatnikAndTip(grupa.getPretplatnik(), repoTipBrojaca.findByTip(21));
		String broj = String.format("%0" + brojac.getBrojPolja() + "d", brojac.getStanje());
		grupa.setSifra(broj);
		brojac.setStanje(brojac.getStanje() + 1);
		repoBrojac.save(brojac);
	
	}
	}
