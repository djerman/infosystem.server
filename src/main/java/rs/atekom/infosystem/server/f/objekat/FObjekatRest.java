package rs.atekom.infosystem.server.f.objekat;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.f.objekat.FObjekat;
import rs.atekom.infosystem.baza.f.objekat.FObjekatOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class FObjekatRest extends OsnovniRest{

	@Autowired
	private FObjekatService service;
	
	@PreAuthorize("(hasAuthority('AGENCIJA') && @pretplatnikService.proveraZaAgenciju(authentication.principal.username, #pretplatnik.get()))"
			+ " || (hasAuthority('ADMINISTRATOR') && @kontaktService.proveraKorisnika(authentication.principal.username, #pretplatnik.get()))"
			+ " || (hasAuthority('KORISNIK') && @kontaktService.proveraKorisnika(authentication.principal.username, #pretplatnik.get()))")
	@GetMapping("/objekti")
	public ResponseEntity<FObjekatOdgovor> pretraga(@RequestParam("pretraga") Optional<String> pretraga, @RequestParam("pretplatnikId") Optional<Long> pretplatnik){
		try {
			return new ResponseEntity<FObjekatOdgovor>(service.lista(pretraga, pretplatnik), HttpStatus.ACCEPTED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PreAuthorize("(hasAuthority('AGENCIJA') && @pretplatnikService.proveraZaAgenciju(authentication.principal.username, #objekat.getPretplatnik().getId()))"
			+ " || (hasAuthority('ADMINISTRATOR') && @kontaktService.proveraKorisnika(authentication.principal.username, #objekat.getPretplatnik().getId()))")
	@PutMapping("/objekat/")
	public ResponseEntity<FObjekatOdgovor> snimi(@RequestBody FObjekat objekat){
		return service.snimi(objekat);
	}
	
	@PreAuthorize("(hasAuthority('AGENCIJA') && @pretplatnikService.proveraZaAgenciju(authentication.principal.username, #id))"
			+ " || (hasAuthority('ADMINISTRATOR') && @kontaktService.proveraKorisnika(authentication.principal.username, #id))")
	@PutMapping("/objekat/id")
	public ResponseEntity<FObjekatOdgovor> brisi(@PathVariable Long id){
		return service.brisi(id);
	}
	
}
