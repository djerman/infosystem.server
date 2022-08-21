package rs.atekom.infosystem.server.h.kontakt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.h.HKontakt;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class HKontaktRest extends OsnovniRest{

	@Autowired
	private HKontaktRepo repo;
	
	@GetMapping(value = "/kontakti", params = "korisnicko")
	public HKontakt korisnicko(@RequestParam String korisnicko){
		return repo.findTopByKorisnickoAndIzbrisanFalseAndAktivanTrueAndPristupTrue(korisnicko);
				}
	
	}
