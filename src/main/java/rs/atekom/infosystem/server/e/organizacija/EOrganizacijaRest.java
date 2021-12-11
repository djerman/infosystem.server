package rs.atekom.infosystem.server.e.organizacija;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class EOrganizacijaRest extends OsnovniRest{

	@Autowired
	EOrganizacijaService service;
	
	}
