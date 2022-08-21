package rs.atekom.infosystem.server.c.mesto;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.a.drzava.ADrzava;
import rs.atekom.infosystem.baza.b.BOpstina;
import rs.atekom.infosystem.baza.c.CMestoOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.drzava.ADrzavaRepo;
import rs.atekom.infosystem.server.b.opstina.BOpstinaRepo;

@Service
public class CMestoService extends OsnovniService{

	@Autowired
	private CMestoRepo repo;
	@Autowired
	private ADrzavaRepo repoDrzava;
	@Autowired
	private BOpstinaRepo repoOpstina;
	
	public CMestoOdgovor listaPoDrzavi(Long drzavaId) {
		CMestoOdgovor odgovor = new CMestoOdgovor();
		try {
			ADrzava drzava = repoDrzava.findById(drzavaId).get();
			odgovor.setLista(repo.findByDrzavaAndIzbrisanFalseOrderByNazivAsc(drzava));
			}catch (Exception e) {
				e.printStackTrace();
				}
		return odgovor;
		}
	
	public CMestoOdgovor listaPoOpstini(Long opstinaId) {
		CMestoOdgovor odgovor = new CMestoOdgovor();
		try {
			BOpstina drzava = repoOpstina.findById(opstinaId).get();
			odgovor.setLista(repo.findByOpstinaAndIzbrisanFalseOrderByNazivAsc(drzava));
			}catch (Exception e) {
				e.printStackTrace();
				}
		return odgovor;
		}
	
	public  CMestoOdgovor lista(Optional<String> pretraga) {
		CMestoOdgovor odgovor = new CMestoOdgovor();
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
