package rs.atekom.infosystem.server.e.godina;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.e.godina.EGodina;
import rs.atekom.infosystem.baza.e.godina.EGodinaOdgovor;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;

@Service
public class EGodinaService {

	@Autowired
	private EGodinaRepo repo;
	@Autowired
	private DPretplatnikRepo repoPretplatnik;
	
	public EGodinaOdgovor lista(Long pretplatnikId) {
		EGodinaOdgovor odgovor = new EGodinaOdgovor();
		DPretplatnik pretplatnik = repoPretplatnik.findById(pretplatnikId).get();
		if(pretplatnik != null) {
			odgovor.setLista(repo.findByPretplatnikOrderByGodinaDesc(pretplatnik));
			return null;
			}else {
				return null;
				}
		}
	
	public EGodinaOdgovor lista() {
		EGodinaOdgovor odgovor = new EGodinaOdgovor();
		odgovor.setLista(repo.findAllByOrderByGodinaDesc());
		return odgovor;
		}
	
	public ResponseEntity<EGodinaOdgovor> snimiIliIzmeni(EGodina novi){
		List<EGodina> lista = repo.findByPretplatnikAndGodinaAndAktivanTrue(novi.getPretplatnik(), novi.getGodina());
		return repo.findById(novi.getId())
				.map(godina -> {
					try {
						if(lista != null && !lista.isEmpty() && lista.size() > 1) {
							return new ResponseEntity<EGodinaOdgovor>(HttpStatus.ALREADY_REPORTED);
							}else {
								godina = novi;
								repo.save(godina);
								return new ResponseEntity<>(lista(godina.getPretplatnik().getId()), HttpStatus.ACCEPTED);
								}
						}catch (Exception e) {
						return new ResponseEntity<EGodinaOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
					}).orElseGet(() -> {
						try {
							if(lista != null && !lista.isEmpty() && lista.size() > 1) {
								return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
								}else {
									repo.save(novi);
									return new ResponseEntity<>(lista(novi.getPretplatnik().getId()), HttpStatus.ACCEPTED);
									}
							}catch (Exception e) {
								return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
								}
					});
				}
	
	public ResponseEntity<EGodinaOdgovor> izbrisi(Long id){
		EGodina godina = repo.findById(id).get();
		try {
			if(godina != null) {
				repo.delete(godina);
				}
			return new ResponseEntity<>(lista(godina.getPretplatnik().getId()), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					godina.setAktivan(false);
					godina.setIzbrisan(true);
					return new ResponseEntity<>(lista(godina.getPretplatnik().getId()), HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	
	}
