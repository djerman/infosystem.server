package rs.atekom.infosystem.server.db.kontoracun;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.db.kontoracun.DBKontoRacunOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class DBKontoRacunService extends OsnovniService{

	@Autowired
	private DBKontoRacunRepo repo;
	
	public DBKontoRacunOdgovor lista() {
		return new DBKontoRacunOdgovor(repo.findAllByOrderBySifraAsc());
		}
	
	public DBKontoRacunOdgovor pretraga(Optional<String> pretraga, Optional<Long> grupa) {
		String pojam = null;
		if(pretraga != null && pretraga.isPresent()) {
			pojam = pretraga.get();
			}
		Long grupaId = null;
		if(grupa != null && grupa.isPresent()) {
			grupaId = grupa.get();
			}
		return new DBKontoRacunOdgovor(repo.pretraga(pojam, grupaId));
		}
	
	}
