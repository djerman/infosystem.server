package rs.atekom.infosystem.server.a.jedinicamere;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.a.jedinicamere.AJedinicaMere;
import rs.atekom.infosystem.baza.a.jedinicamere.AJedinicaMereOdgovor;

@RestController
@Validated
public class AJedinicaMereRest {

	@Autowired
	AJedinicaMereRepo repo;
	@Autowired
	AJedinicaMereService service;
	
	@GetMapping("/jedinice")
	public ResponseEntity<AJedinicaMereOdgovor> lista(){
		try {
			return new ResponseEntity<AJedinicaMereOdgovor>(new AJedinicaMereOdgovor(repo.findByIzbrisanFalse()), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PutMapping("/jedinica/snimi")
	@Transactional
	public ResponseEntity<AJedinicaMereOdgovor> snimi(@RequestBody AJedinicaMere novaJedinica) {
		List<AJedinicaMere> lista = repo.pretraga(novaJedinica.getNaziv(), novaJedinica.getOznaka());
		try {
			return repo.findById(novaJedinica.getId())
					.map(jedinica -> {
						if(lista.size() == 1 && lista.get(0).getId().equals(novaJedinica.getId())) {
							if(novaJedinica.getNazivSr() == null || novaJedinica.getNazivSr().equals("")) {
								novaJedinica.setNazivSr(novaJedinica.getNaziv());
								}
							if(novaJedinica.getNazivEn() == null || novaJedinica.getNazivEn().equals("")) {
								novaJedinica.setNazivEn(novaJedinica.getNaziv());
								}
							if(novaJedinica.getNazivDe() == null || novaJedinica.getNazivDe().equals("")) {
								novaJedinica.setNazivDe(novaJedinica.getNaziv());
								}
							if(novaJedinica.getSr() == null || novaJedinica.getSr().equals("")) {
								novaJedinica.setSr(novaJedinica.getOznaka());
								}
							if(novaJedinica.getEn() == null || novaJedinica.getEn().equals("")) {
								novaJedinica.setEn(novaJedinica.getOznaka());
								}
							if(novaJedinica.getDe() == null || novaJedinica.getDe().equals("")) {
								novaJedinica.setDe(novaJedinica.getOznaka());
								}
							novaJedinica.setVerzija(novaJedinica.getVerzija() + 1);
							jedinica = novaJedinica;
							repo.save(jedinica);
							return new ResponseEntity<AJedinicaMereOdgovor>(new AJedinicaMereOdgovor(repo.findByIzbrisanFalse()), HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<AJedinicaMereOdgovor>(HttpStatus.ALREADY_REPORTED);
								}
						})
					.orElseGet(() -> {
						if(lista == null || lista.size() == 0) {
							novaJedinica.setId(null);
							novaJedinica.setIzbrisan(false);
							novaJedinica.setVerzija(0);
							if(novaJedinica.getNazivSr() == null || novaJedinica.getNazivSr().equals("")) {
								novaJedinica.setNazivSr(novaJedinica.getNaziv());
								}
							if(novaJedinica.getNazivEn() == null || novaJedinica.getNazivEn().equals("")) {
								novaJedinica.setNazivEn(novaJedinica.getNaziv());
								}
							if(novaJedinica.getNazivDe() == null || novaJedinica.getNazivDe().equals("")) {
								novaJedinica.setNazivDe(novaJedinica.getNaziv());
								}
							if(novaJedinica.getSr() == null || novaJedinica.getSr().equals("")) {
								novaJedinica.setSr(novaJedinica.getOznaka());
								}
							if(novaJedinica.getEn() == null || novaJedinica.getEn().equals("")) {
								novaJedinica.setEn(novaJedinica.getOznaka());
								}
							if(novaJedinica.getDe() == null || novaJedinica.getDe().equals("")) {
								novaJedinica.setDe(novaJedinica.getOznaka());
								}
							repo.save(novaJedinica);
							return new ResponseEntity<AJedinicaMereOdgovor>(new AJedinicaMereOdgovor(repo.findByIzbrisanFalse()), HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
								}
					});
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@DeleteMapping("/jedinica/brisi/{id}")
	@Transactional
	public ResponseEntity<AJedinicaMereOdgovor> brisi(@PathVariable Long id){
		try {
			repo.deleteById(id);
			return new ResponseEntity<AJedinicaMereOdgovor>(new AJedinicaMereOdgovor(repo.findByIzbrisanFalse()), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					AJedinicaMere jedinica = repo.findById(id).get();
					jedinica.setIzbrisan(true);
					return new ResponseEntity<AJedinicaMereOdgovor>(new AJedinicaMereOdgovor(repo.findByIzbrisanFalse()), HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	
	}
