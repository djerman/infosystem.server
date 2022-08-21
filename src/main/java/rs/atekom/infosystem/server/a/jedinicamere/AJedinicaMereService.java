package rs.atekom.infosystem.server.a.jedinicamere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.a.jedinicamere.AJedinicaMereOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class AJedinicaMereService extends OsnovniService{

	@Autowired
	private AJedinicaMereRepo repo;
	
	public AJedinicaMereOdgovor lista() {
		AJedinicaMereOdgovor odgovor = new AJedinicaMereOdgovor();
		try {
			odgovor.setLista(repo.findByIzbrisanFalse());
			}catch (Exception e) {
				// TODO: handle exception
				}
		return odgovor;
		}
	
	}
