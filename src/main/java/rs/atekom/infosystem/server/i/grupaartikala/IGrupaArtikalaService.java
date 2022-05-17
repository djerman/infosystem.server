package rs.atekom.infosystem.server.i.grupaartikala;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.i.grupaartikala.IGrupaArtikala;
import rs.atekom.infosystem.baza.i.grupaartikala.IGrupaArtikalaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;

@Service
public class IGrupaArtikalaService extends OsnovniService{

	@Autowired
	IGrupaArtikalaRepo repo;
	@Autowired
	DPretplatnikRepo repoPretplatnik;
	
	public IGrupaArtikalaOdgovor lista(Long pretplatnikId) {
		try {
			return new IGrupaArtikalaOdgovor(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(repoPretplatnik.findById(pretplatnikId).get()));
			}catch (Exception e) {
				e.printStackTrace();
				return  null;
				}
		}
	
	public ResponseEntity<IGrupaArtikalaOdgovor> snimi(IGrupaArtikala nova) {
		return repo.findById(nova.getId())
				.map(grupa -> {
					try {
						grupa = nova;
						grupa.setVerzija(nova.getVerzija() + 1);
						return new ResponseEntity<IGrupaArtikalaOdgovor>(lista(repo.save(grupa).getPretplatnik().getId()), HttpStatus.ACCEPTED);
						}catch (Exception e) {
							return new ResponseEntity<IGrupaArtikalaOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
							}
					}).orElseGet(() -> {
						try {
							nova.setId(null);
							nova.setIzbrisan(false);
							nova.setVerzija(0);
							return new ResponseEntity<IGrupaArtikalaOdgovor>(lista(repo.save(nova).getPretplatnik().getId()), HttpStatus.ACCEPTED);
							}catch (Exception e) {
								return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
								}
						});
		}
	
	public ResponseEntity<IGrupaArtikalaOdgovor> izbrisi(Long grupaId){
		IGrupaArtikala grupa = repo.findById(grupaId).get();
		try {
			repo.delete(grupa);
			return new ResponseEntity<IGrupaArtikalaOdgovor>(lista(grupaId), HttpStatus.ACCEPTED);
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
	
	}
