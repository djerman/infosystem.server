package rs.atekom.infosystem.server.e.organizacija;

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
import rs.atekom.infosystem.baza.e.organizacija.EOrganizacija;
import rs.atekom.infosystem.baza.e.organizacija.EOrganizacijaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class EOrganizacijaRest extends OsnovniRest{

	@Autowired
	private EOrganizacijaService service;
	
	//zabraniti listanje (a odnosi se i na upis/izmenu/brisanje) ako pretplatnik ne odgovara dodeljenom za agenciju i administratoru
	//@PreAuthorize("hasAuthority('SISTEM') || (hasAuthority('AGENCIJA') && #pretplatnik != null) || (hasAuthority('ADMINISTRATOR') && #pretplatnik != null)")
	//@PreAuthorize("(hasAuthority('AGENCIJA') && @pretplatnikService.proveraZaAgenciju(authentication.principal.username, #organizacija.getPretplatnik().getId()))"
		//	+ " || (hasAuthority('ADMINISTRATOR') && @kontaktService.proveraKorisnika(authentication.principal.username, #organizacija.getPretplatnik().getId()))"
		//	+ " || (hasAuthority('KORISNIK') && @kontaktService.proveraKorisnika(authentication.principal.username, #organizacija.getPretplatnik().getId()))")
	@PreAuthorize("(hasAuthority('AGENCIJA') && @pretplatnikService.proveraZaAgenciju(authentication.principal.username, #pretplatnik.get()))"
			+ " || (hasAuthority('ADMINISTRATOR') && @kontaktService.proveraKorisnika(authentication.principal.username, #pretplatnik.get()))"
			+ " || (hasAuthority('KORISNIK') && @kontaktService.proveraKorisnika(authentication.principal.username, #pretplatnik.get()))")
	@GetMapping("/organizacije")
	public ResponseEntity<EOrganizacijaOdgovor> pretraga(@RequestParam("pretraga") Optional<String> pretraga, @RequestParam("pretplatnikId") Optional<Long> pretplatnik){
		try {
			return new ResponseEntity<EOrganizacijaOdgovor>(service.lista(pretraga, pretplatnik), HttpStatus.ACCEPTED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PreAuthorize("(hasAuthority('AGENCIJA') && @pretplatnikService.proveraZaAgenciju(authentication.principal.username, #organizacija.getPretplatnik().getId()))"
			+ " || (hasAuthority('ADMINISTRATOR') && @kontaktService.proveraKorisnika(authentication.principal.username, #organizacija.getPretplatnik().getId()))")
	@PutMapping("/organizacija/")
	public ResponseEntity<EOrganizacijaOdgovor> snimi(@RequestBody EOrganizacija organizacija){
		return service.snimi(organizacija);
	}
	
	@PreAuthorize("(hasAuthority('AGENCIJA') && @pretplatnikService.proveraZaAgenciju(authentication.principal.username, #id))"
			+ " || (hasAuthority('ADMINISTRATOR') && @kontaktService.proveraKorisnika(authentication.principal.username, #id))")
	@PutMapping("/organizacija/{id}")
	public ResponseEntity<EOrganizacijaOdgovor> brisi(@PathVariable Long id){
		return service.brisi(id);
	}
	
	}
