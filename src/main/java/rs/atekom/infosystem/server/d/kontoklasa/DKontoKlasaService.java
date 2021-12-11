package rs.atekom.infosystem.server.d.kontoklasa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.d.kontoklasa.DKontoKlasaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class DKontoKlasaService extends OsnovniService{

	@Autowired
	DKontoKlasaRepo repo;
	
	public DKontoKlasaOdgovor lista() {	
		return new DKontoKlasaOdgovor(repo.findAllByIzbrisanFalseOrderBySifraAsc());
		}
	
	 public DKontoKlasaOdgovor pretraga(Optional<String> pretraga) {
		 String pojam = null;
		 if(pretraga != null && pretraga.isPresent()) {
			 pojam = pretraga.get();
			 }
		 return new DKontoKlasaOdgovor(repo.pretraga(pojam));
		 }
	 
	}
