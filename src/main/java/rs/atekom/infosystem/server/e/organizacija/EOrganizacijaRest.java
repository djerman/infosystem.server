package rs.atekom.infosystem.server.e.organizacija;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.e.organizacija.EOrganizacijaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class EOrganizacijaRest extends OsnovniRest{

	@Autowired
	EOrganizacijaService service;
	
	//zabraniti listanje (a odnosi se i na upis/izmenu/brisanje) ako pretplatnik ne odgovara dodeljenom za agenciju i administratoru
	@PreAuthorize("hasAuthority('SISTEM') || (hasAuthority('AGENCIJA') && #pretplatnik != null) || (hasAuthority('ADMINISTRATOR') && #pretplatnik != null)")
	@GetMapping("/organizacije")
	public ResponseEntity<EOrganizacijaOdgovor> pretraga(@RequestParam("pretraga") Optional<String> pretraga, @RequestParam("pretplatnikId") Optional<Long> pretplatnik){
		return null;
	}
	
	}
