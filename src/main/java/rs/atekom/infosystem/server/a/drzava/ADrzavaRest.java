package rs.atekom.infosystem.server.a.drzava;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.atekom.infosystem.baza.a.drzava.ADrzava;
import rs.atekom.infosystem.baza.a.drzava.ADrzavaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class ADrzavaRest extends OsnovniRest{

	@Autowired
	ADrzavaRepo repo;
	@Autowired
	ADrzavaService service;
	
	@GetMapping("/drzave")
	public ResponseEntity<ADrzavaOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga){
		try {
			return new ResponseEntity<ADrzavaOdgovor>(service.lista(pretraga), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	/**
	 * vraća liste mesta i opština za izabranu državu
	 * @param id
	 * @return
	 */
	@GetMapping("/drzave/liste")
	public ResponseEntity<ADrzavaOdgovor> liste(@RequestParam("drzavaId") Long id){
		try {
			return new ResponseEntity<ADrzavaOdgovor>(service.liste(id), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PutMapping("/drzava/snimi")
	@Transactional
	public ResponseEntity<ADrzavaOdgovor> snimiIzmeni(@RequestBody ADrzava novaDrzava){
		try {
			return repo.findById(novaDrzava.getId())
					.map(drzava -> {
						drzava = novaDrzava;
						repo.save(drzava);
						return new ResponseEntity<ADrzavaOdgovor>(service.lista(null), HttpStatus.ACCEPTED);
						})
					.orElseGet(() -> {
						novaDrzava.setId(null);
						novaDrzava.setIzbrisan(false);
						repo.save(novaDrzava);
						return new ResponseEntity<ADrzavaOdgovor>(service.lista(null), HttpStatus.ACCEPTED);
						});
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@DeleteMapping("/drzava/brisi/{id}")
	@Transactional
	public ResponseEntity<ADrzavaOdgovor> brisi(@PathVariable Long id){
		try {
			repo.deleteById(id);
			return new ResponseEntity<ADrzavaOdgovor>(service.lista(null), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					ADrzava drzava = repo.findById(id).get();
					drzava.setIzbrisan(true);
					return new ResponseEntity<ADrzavaOdgovor>(service.lista(null), HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	
	}
