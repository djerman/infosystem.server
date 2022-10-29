package rs.atekom.infosystem.server.f.objekat;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.atekom.infosystem.baza.f.objekat.FObjekat;
import rs.atekom.infosystem.baza.f.objekat.FObjekatOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class FObjekatService extends OsnovniService {

	@Autowired
	private FObjekatRepo repo;
	
	public FObjekatOdgovor lista(Optional<String> pretraga, Optional<Long> pretplId) {
		FObjekatOdgovor odgovor = new FObjekatOdgovor();
		try {
			String pojam = null;
			Long pretplatnikId = null;
			if(pretraga != null && pretraga.isPresent()) 
				pojam = pretraga.get();
			if(pretplId != null && pretplId.isPresent())
				pretplatnikId = pretplId.get();
			odgovor.setLista(repo.pretragaObjekata(pojam, pretplatnikId));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return odgovor;
	}
	
	@Transactional
	public ResponseEntity<FObjekatOdgovor> snimi(FObjekat noviObjekat){
		List<FObjekat> postojeci = repo.findByPretplatnikAndIzbrisanFalseAndNaziv(noviObjekat.getPretplatnik(), noviObjekat.getNaziv());
		FObjekatOdgovor odgovor = new FObjekatOdgovor();
		return repo.findById(noviObjekat.getId() != null ? noviObjekat.getId() : 0L)
				.map(objekat -> {
					try {
						Boolean postoji = false;
						if(postojeci.size() > 0) {
							for(FObjekat obj : postojeci) {
								if(!obj.getId().equals(noviObjekat.getId()) && !obj.getIzbrisan()) {
									postoji = true;
								}
							}
						}
						if(postoji) {
							return new ResponseEntity<FObjekatOdgovor>(odgovor, HttpStatus.ALREADY_REPORTED);
						}else {
							if(noviObjekat.getVerzija() == objekat.getVerzija()) {
								objekat = noviObjekat;
								objekat.setVerzija(objekat.getVerzija() + 1);
								if(objekat.getIzbrisan() == null)
									objekat.setIzbrisan(false);
								repo.save(objekat);
								odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalse(objekat.getPretplatnik()));
								return new ResponseEntity<FObjekatOdgovor>(odgovor, HttpStatus.ACCEPTED);
							}else{
								return new ResponseEntity<FObjekatOdgovor>(odgovor, HttpStatus.MULTI_STATUS);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						return new ResponseEntity<FObjekatOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}).orElseGet(() -> {
					try {
						if(postojeci != null && postojeci.size() > 0) {
							return new ResponseEntity<FObjekatOdgovor>(odgovor, HttpStatus.ALREADY_REPORTED);
						}else {
							if(noviObjekat.getNaziv() == null || noviObjekat.getNaziv().equals("") || noviObjekat.getPretplatnik() == null) {
								return new ResponseEntity<FObjekatOdgovor>(odgovor, HttpStatus.FORBIDDEN);
							}else {
								noviObjekat.setVerzija(0);
								noviObjekat.setIzbrisan(false);
								repo.save(noviObjekat);
								odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalse(noviObjekat.getPretplatnik()));
								return new ResponseEntity<FObjekatOdgovor>(odgovor, HttpStatus.ACCEPTED);
							}
						}
					}catch (Exception e) {
						return new ResponseEntity<FObjekatOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				});
	}
	
	@Transactional
	public ResponseEntity<FObjekatOdgovor> brisi(Long objekatId){
		if(objekatId != null) {
			FObjekatOdgovor odgovor = new FObjekatOdgovor();
			FObjekat objekat = repo.findById(objekatId).get();
			if(objekat != null) {
				try {
					repo.delete(objekat);
					odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalse(objekat.getPretplatnik()));
					return new ResponseEntity<FObjekatOdgovor>(odgovor, HttpStatus.ACCEPTED);
				}catch (Exception e) {
					try {
						objekat.setIzbrisan(false);
						repo.save(objekat);
						odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalse(objekat.getPretplatnik()));
						return new ResponseEntity<FObjekatOdgovor>(odgovor, HttpStatus.ACCEPTED);
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
}
