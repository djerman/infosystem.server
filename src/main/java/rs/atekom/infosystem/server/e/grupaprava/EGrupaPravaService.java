package rs.atekom.infosystem.server.e.grupaprava;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.e.grupaprava.EGrupaPrava;
import rs.atekom.infosystem.baza.e.grupaprava.EGrupaPravaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;

@Service("grupaPravaService")
public class EGrupaPravaService extends OsnovniService{

	@Autowired
	private EGrupaPravaRepo repo;
	@Autowired
	private DPretplatnikRepo pretplatnikRepo;
	
	public EGrupaPravaOdgovor lista(Optional<Long> pretplatnikId) {
		EGrupaPravaOdgovor odgovor = new EGrupaPravaOdgovor();
		try {
			Long id = null;
			DPretplatnik pretplatnik = null;
			if(pretplatnikId != null && pretplatnikId.isPresent()) {
				id = pretplatnikId.get();
				pretplatnik = pretplatnikRepo.findById(id).get();
				odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(pretplatnik));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return odgovor;
		}
	
	public ResponseEntity<EGrupaPravaOdgovor> snimi(EGrupaPrava nova){
		EGrupaPravaOdgovor odgovor = new EGrupaPravaOdgovor();
		List<EGrupaPrava> lista = repo.findByNazivAndIzbrisanFalse(nova.getNaziv());
		return repo.findById(nova.getId() != null ? nova.getId() : 0L)
				.map(grupa -> {
					try {
						if(nova.getVerzija().equals(grupa.getVerzija())) {
							boolean postoji = false;
							if(nova.getNaziv() != null) {
								for(EGrupaPrava grupaPrava : lista) {
									if(nova.getPretplatnik() == grupaPrava.getPretplatnik() && nova.getId() != grupaPrava.getId()) {
										postoji = true;
									}
								}
								if(!postoji) {
									grupa = nova;
									grupa.setVerzija(nova.getVerzija() + 1);
									repo.save(grupa);
									odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(nova.getPretplatnik()));
								}else {
									return new ResponseEntity<EGrupaPravaOdgovor>(odgovor, HttpStatus.ALREADY_REPORTED);
								}
								return new ResponseEntity<EGrupaPravaOdgovor>(odgovor, HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<EGrupaPravaOdgovor>(HttpStatus.FORBIDDEN);
							}

						}else {
							return new ResponseEntity<EGrupaPravaOdgovor>(HttpStatus.MULTI_STATUS);
						}
					}catch (Exception e) {
						return new ResponseEntity<EGrupaPravaOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}).orElseGet(() -> {
					try {
						if(lista == null || lista.size() == 0) {
							nova.setIzbrisan(false);
							nova.setVerzija(0);
							odgovor.setLista(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(nova.getPretplatnik()));
							return new ResponseEntity<EGrupaPravaOdgovor>(odgovor, HttpStatus.ACCEPTED);
						}else {
							return new ResponseEntity<EGrupaPravaOdgovor>(odgovor, HttpStatus.ALREADY_REPORTED);
						}
					}catch (Exception e) {
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				});
	}
	
	public ResponseEntity<EGrupaPravaOdgovor> izbrisi(Long grupaId){
		EGrupaPrava grupa = repo.findById(grupaId).get();
		try {
			repo.deleteById(grupaId);
			return new ResponseEntity<EGrupaPravaOdgovor>(new EGrupaPravaOdgovor(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(grupa.getPretplatnik())), HttpStatus.ACCEPTED);
		}catch (Exception e) {
			try {
				grupa.setIzbrisan(true);
				return new ResponseEntity<EGrupaPravaOdgovor>(new EGrupaPravaOdgovor(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(grupa.getPretplatnik())), HttpStatus.ACCEPTED);
			}catch (Exception ee) {
				ee.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	}
