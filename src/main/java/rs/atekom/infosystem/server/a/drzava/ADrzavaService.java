package rs.atekom.infosystem.server.a.drzava;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.a.drzava.ADrzava;
import rs.atekom.infosystem.baza.a.drzava.ADrzavaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.b.opstina.BOpstinaRepo;
import rs.atekom.infosystem.server.c.mesto.CMestoRepo;

@Service
public class ADrzavaService extends OsnovniService{

	@Autowired
	private ADrzavaRepo repo;
	@Autowired
	private BOpstinaRepo repoOpstina;
	@Autowired
	private CMestoRepo repoMesto;
	
	public ADrzavaOdgovor lista(Optional<String> pretraga) {
		ADrzavaOdgovor odgovor = new ADrzavaOdgovor();
		try {
			String pojam = null;
			if(pretraga != null && pretraga.isPresent()) {
				pojam = pretraga.get();
				}
			odgovor.setLista(repo.pretraga(pojam));
			}catch (Exception e) {
				// TODO: handle exception
				}
		return odgovor;
		}
	
	public ADrzavaOdgovor liste(Long drzavaId) {
		ADrzavaOdgovor odgovor = new ADrzavaOdgovor();
		if(drzavaId != null) {
			try {
				ADrzava drzava = repo.findById(drzavaId).get();
				if(drzava != null) {
					odgovor.setListaOpstina(repoOpstina.findByDrzavaAndIzbrisanFalseOrderByNazivAsc(drzava));
					odgovor.setListaMesta(repoMesto.findByDrzavaAndIzbrisanFalseOrderByNazivAsc(drzava));
					}
				}catch (Exception e) {
					// TODO: handle exception
					}
			}
		return odgovor;
		}
	
	}
