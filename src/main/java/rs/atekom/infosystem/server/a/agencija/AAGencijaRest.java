package rs.atekom.infosystem.server.a.agencija;

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

import rs.atekom.infosystem.baza.a.agencija.AAgencija;
import rs.atekom.infosystem.baza.a.agencija.AAgencijaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class AAGencijaRest extends OsnovniRest{

	@Autowired
	AAgencijaRepo repo;
	@Autowired
	AAgencijaService service;
	
	@GetMapping("/agencije")
	public ResponseEntity<AAgencijaOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga){
		try {
			return new ResponseEntity<AAgencijaOdgovor>(service.napraviOdgovorSaListom(pretraga), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PutMapping("/agencija/snimi")
	@Transactional
	public ResponseEntity<AAgencijaOdgovor> snimiIzmeni(@RequestBody AAgencija novaAgencija){
		try {
			return repo.findById(novaAgencija.getId())
					.map(agencija -> {
						agencija = novaAgencija;
						repo.save(agencija);
						return new ResponseEntity<>(service.napraviOdgovorSaListom(null), HttpStatus.ACCEPTED);
						})
					.orElseGet(() -> {
						novaAgencija.setId(null);
						novaAgencija.setIzbrisan(false);
						repo.save(novaAgencija);
						return new ResponseEntity<>(service.napraviOdgovorSaListom(null), HttpStatus.ACCEPTED);
					});
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@DeleteMapping("/agencija/brisi/{id}")
	@Transactional
	public ResponseEntity<AAgencijaOdgovor> brisi(@PathVariable Long id){
		try {
			repo.deleteById(id);
			return new ResponseEntity<>(service.napraviOdgovorSaListom(null), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					AAgencija agencija = repo.findById(id).get();
					agencija.setIzbrisan(true);
					return new ResponseEntity<>(service.napraviOdgovorSaListom(null), HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	
	}
