package rs.atekom.infosystem.server.e.godina;

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

import rs.atekom.infosystem.baza.e.godina.EGodina;
import rs.atekom.infosystem.baza.e.godina.EGodinaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class EGodinaRest extends OsnovniRest{

	@Autowired
	EGodinaService service;
	
	@GetMapping("/godine")
	public ResponseEntity<EGodinaOdgovor> sve(){
		try {
			return new ResponseEntity<EGodinaOdgovor>(service.lista(), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@GetMapping("/godine/pretplatnik")
	public ResponseEntity<EGodinaOdgovor> poPretplatniku(@RequestParam("id") Long id){
		try {
			return new ResponseEntity<EGodinaOdgovor>(service.lista(id), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PutMapping("/godina/")
	public ResponseEntity<EGodinaOdgovor> sacuvajIliIzmeni(@RequestBody EGodina nova){
		return service.snimiIliIzmeni(nova);
		}
	
	@DeleteMapping("/godina/{id}")
	public ResponseEntity<EGodinaOdgovor> izbrisi(@PathVariable Long id){
		return service.izbrisi(id);
		}
	
	}
