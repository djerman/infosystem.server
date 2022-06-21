package rs.atekom.infosystem.server.f.brojac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rs.atekom.infosystem.baza.f.brojac.FBrojac;
import rs.atekom.infosystem.baza.f.brojac.FBrojacOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class FBrojacRest extends OsnovniRest{

	@Autowired
	private FBrojacService service;
	
	@PreAuthorize("hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR') || hasAuthority('KORISNIK')")
	@GetMapping("/brojaci")
	public ResponseEntity<FBrojacOdgovor> lista(Long pretplatnikId){
		try {
			FBrojacOdgovor odgovor = new FBrojacOdgovor();
			odgovor.setLista(service.vratiBrojacePretplatnika(pretplatnikId));
			return new ResponseEntity<FBrojacOdgovor>(odgovor, HttpStatus.ACCEPTED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PreAuthorize("hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR') || hasAuthority('KORISNIK')")
	@GetMapping("/brojac")
	public ResponseEntity<FBrojacOdgovor> brojac(){
		try {
			FBrojacOdgovor odgovor = new FBrojacOdgovor();
			
			return new ResponseEntity<FBrojacOdgovor>(odgovor, HttpStatus.ACCEPTED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PreAuthorize("hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@PutMapping("/brojac/")
	public ResponseEntity<FBrojacOdgovor> snimi(@RequestBody FBrojac brojac){
		return service.snimi(brojac);
	}
	
}
