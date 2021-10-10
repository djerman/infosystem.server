package rs.atekom.infosystem.server.a.agencija;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.a.agencija.AAgencijaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class AAgencijaService extends OsnovniService{

	@Autowired
	AAgencijaRepo repo;
	
	public AAgencijaOdgovor napraviOdgovorSaListom(Optional<String> pretraga) {
		try {
			AAgencijaOdgovor odgovor = new AAgencijaOdgovor();
			String pojam = null;
			if(pretraga != null && pretraga.isPresent()) {
				pojam = pretraga.get();
				}
			odgovor.setLista(repo.pretraga(pojam));
			return odgovor;
			}catch (Exception e) {
				return null;
				}
		}
	}
