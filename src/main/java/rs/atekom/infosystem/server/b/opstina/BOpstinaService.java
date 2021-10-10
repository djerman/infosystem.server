package rs.atekom.infosystem.server.b.opstina;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.a.drzava.ADrzava;
import rs.atekom.infosystem.baza.b.BOpstinaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.drzava.ADrzavaRepo;

@Service
public class BOpstinaService extends OsnovniService{

	@Autowired
	BOpstinaRepo repo;
	@Autowired
	ADrzavaRepo repoDrzava;
	
	public BOpstinaOdgovor lista(Long drzavaId) {
		BOpstinaOdgovor odgovor = new BOpstinaOdgovor();
		try {
			ADrzava drzava = repoDrzava.findById(drzavaId).get();
			odgovor.setLista(repo.findByDrzavaAndIzbrisanFalseOrderByNazivAsc(drzava));
			}catch (Exception e) {
				e.printStackTrace();
				}
		return odgovor;
		}
	
	public BOpstinaOdgovor lista(Optional<String> pretraga) {
		BOpstinaOdgovor odgovor = new BOpstinaOdgovor();
		try {
			String pojam = null;
			if(pretraga != null && pretraga.isPresent()) {
				pojam = pretraga.get();
				}
			odgovor.setLista(repo.pretraga(pojam));
			}catch (Exception e) {
				e.printStackTrace();
				}
		return odgovor;
		}
	}
