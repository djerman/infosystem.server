package rs.atekom.infosystem.server.f.grupapartnera;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.f.brojac.FBrojac;
import rs.atekom.infosystem.baza.f.grupapartnera.FGrupaPartnera;
import rs.atekom.infosystem.baza.f.grupapartnera.FGrupaPartneraOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.tipbrojaca.ATipBrojacaRepo;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;
import rs.atekom.infosystem.server.e.konto.EKontoRepo;
import rs.atekom.infosystem.server.f.brojac.FBrojacRepo;

@Service
public class FGrupaPartneraService extends OsnovniService{

	@Autowired
	private FGrupaPartneraRepo repo;
	@Autowired
	private DPretplatnikRepo repoPretplatnik;
	@Autowired
	private FBrojacRepo repoBrojac;
	@Autowired
	private ATipBrojacaRepo repoTipBrojaca;
	@Autowired
	private EKontoRepo repoKonto;
	
	public FGrupaPartneraOdgovor lista(Long pretplatnikId) {
		try {
			FGrupaPartneraOdgovor odgovor = new FGrupaPartneraOdgovor();
			DPretplatnik pretplatnik = repoPretplatnik.findById(pretplatnikId).get();
			odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(repoPretplatnik.findById(pretplatnikId).get()));
			odgovor.setKonta(repoKonto.findByPretplatnikOrPretplatnikIsNullAndIzbrisanFalseOrderBySifraAsc(pretplatnik));
			return odgovor;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
				}
		}
	
	public ResponseEntity<FGrupaPartneraOdgovor> snimi(FGrupaPartnera nova){
		List<FGrupaPartnera> postojeci = repo.findByPretplatnikAndIzbrisanFalseAndSifraOrNaziv(nova.getPretplatnik(), 
				nova.getSifra(), nova.getNaziv());
		return repo.findById(nova.getId() != null ? nova.getId() : 0L)
				.map(grupa -> {
					try {
						Boolean postoji = false;
						if(postojeci.size() > 0) {
							for(FGrupaPartnera gr : postojeci) {
								if(!gr.getId().equals(nova.getId()) && !gr.getIzbrisan()) {
									postoji = true;
								}
							}
						}
						if(postoji) {
							return new ResponseEntity<FGrupaPartneraOdgovor>(HttpStatus.ALREADY_REPORTED);
						}else {
							if(nova.getVerzija() == grupa.getVerzija()) {
								if(nova.getSifra() == null || nova.getSifra().equals("")
										|| nova.getSifra().isBlank() || nova.getSifra().isEmpty()) {
									if(grupa.getSifra() != null && !grupa.getSifra().equals("")
											 && !grupa.getSifra().isBlank() || !grupa.getSifra().isEmpty()) {
										nova.setSifra(grupa.getSifra());
									}else {
										postaviBrojac(nova);
									}
								}
								grupa = nova;
								grupa.setVerzija(nova.getVerzija() + 1);
								repo.save(grupa);
								return new ResponseEntity<FGrupaPartneraOdgovor>(lista(grupa.getPretplatnik().getId()), HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<FGrupaPartneraOdgovor>(HttpStatus.MULTI_STATUS);
							}
						}
						}catch (Exception e) {
							return new ResponseEntity<FGrupaPartneraOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
							}
					}).orElseGet(() -> {
						try {
							if(postojeci != null && postojeci.size() > 0) {
								return new ResponseEntity<FGrupaPartneraOdgovor>(HttpStatus.ALREADY_REPORTED);
							}else {
								nova.setId(null);
								nova.setIzbrisan(false);
								nova.setVerzija(0);
								if(nova.getSifra() == null || nova.getSifra().equals("")
										|| nova.getSifra().isBlank() || nova.getSifra().isEmpty())
									postaviBrojac(nova);
								repo.save(nova);
								return new ResponseEntity<FGrupaPartneraOdgovor>(lista(nova.getPretplatnik().getId()), HttpStatus.ACCEPTED);
							}
							}catch (Exception e) {
								return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
								}
					});
				}
	
	public ResponseEntity<FGrupaPartneraOdgovor> izbrisi(Long grupaId){
		FGrupaPartnera grupa = repo.findById(grupaId).get();
		try {
			repo.delete(grupa);
			return new ResponseEntity<FGrupaPartneraOdgovor>(lista(grupaId), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					grupa.setIzbrisan(true);
					return new ResponseEntity<FGrupaPartneraOdgovor>(lista(repo.save(grupa).getPretplatnik().getId()), HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	
	private void postaviBrojac(FGrupaPartnera grupa) {
		FBrojac brojac = repoBrojac.findByPretplatnikAndTip(grupa.getPretplatnik(), repoTipBrojaca.findByTip(11));
		String broj = String.format("%0" + brojac.getBrojPolja() + "d", brojac.getStanje());
		grupa.setSifra(broj);
		brojac.setStanje(brojac.getStanje() + 1);
		repoBrojac.save(brojac);
	}
	}
