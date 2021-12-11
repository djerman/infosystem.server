package rs.atekom.infosystem.server.da.kontogrupa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.da.kontogrupa.DAKontoGrupaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class DAKontoGrupaService extends OsnovniService{

	@Autowired
	DAKontoGrupaRepo repo;
	
	public DAKontoGrupaOdgovor lista() {
		return new DAKontoGrupaOdgovor(repo.findAllByOrderBySifraAsc());
		}
	
	public DAKontoGrupaOdgovor pretraga(Optional<String> pretraga, Optional<Long> klasa) {
		String pojam = null;
		if(pretraga != null && pretraga.isPresent()) {
			pojam = pretraga.get();
			}
		Long klasaId = null;
		if(klasa != null && klasa.isPresent()) {
			klasaId = klasa.get();
			}
		return new DAKontoGrupaOdgovor(repo.pretraga(pojam, klasaId));
		}
	
	}
