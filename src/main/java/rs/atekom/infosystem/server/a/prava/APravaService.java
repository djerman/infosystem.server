package rs.atekom.infosystem.server.a.prava;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.a.prava.APravaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class APravaService extends OsnovniService{

	@Autowired
	private APravaRepo repo;
	
	//pretraga mora biti uskđaena da 'tip' - integer odnosno nivoom prava koje ima onaj koji pretražuje
	public APravaOdgovor listaPrava(Optional<String> pretraga){
		try {
			APravaOdgovor odgovor = new APravaOdgovor();
			String pojam = null;
			if(pretraga != null && pretraga.isPresent()) {
				pojam = pretraga.get();
				}
			odgovor.setLista(repo.pretraga(pojam));
			return odgovor;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
				}
		}
	
	}
