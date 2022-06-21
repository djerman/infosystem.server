package rs.atekom.infosystem.server.j.artikal;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.j.JArtikal;
import rs.atekom.infosystem.baza.j.JArtikalOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class JArtikalRest extends OsnovniRest{

	@Autowired
	public JArtikalService service;
	
	@PreAuthorize("hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR') || hasAuthority('KORISNIK')")
	@GetMapping("/artikli")
	public ResponseEntity<JArtikalOdgovor> lista(@RequestParam(value = "pretplatnikId") Long pretplatnikId, @RequestParam(value = "pretraga") Optional<String> pretraga,
			@RequestParam() int strana){
		try {
			return new ResponseEntity<JArtikalOdgovor>(service.vratiListuPoPretplatniku(pretplatnikId, pretraga, strana), HttpStatus.ACCEPTED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PreAuthorize("hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@PutMapping("/artikal/")
	public ResponseEntity<JArtikalOdgovor> snimi(@RequestBody JArtikal artikal){
		return service.snimiArtikal(artikal);
	}
	
	@PreAuthorize("hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@DeleteMapping("/artikal/{id}")
	public ResponseEntity<JArtikalOdgovor> brisi(@PathVariable Long id){
		return service.brisi(id);
	}
	
}
