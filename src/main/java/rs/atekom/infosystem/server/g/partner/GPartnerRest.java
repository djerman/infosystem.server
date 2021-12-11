package rs.atekom.infosystem.server.g.partner;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.atekom.infosystem.baza.g.GPartnerOdgovor;
import rs.atekom.infosystem.baza.g.GPartnerOdgovorPodaci;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class GPartnerRest extends OsnovniRest{

	@Autowired
	GPartnerService service;

	
	@GetMapping("/partneri")
	public ResponseEntity<GPartnerOdgovor> pretraga(@RequestParam("pretraga") Optional<String> pretraga, @RequestParam("pretplatnikId") Long pretplatnikId,
			@RequestParam("kupac") Optional<Boolean> kupac){
		return service.listaPreduzeca(pretraga, pretplatnikId, kupac);
		}
	
	@PutMapping("/partner/snimi")
	public ResponseEntity<GPartnerOdgovor> snimi(@RequestBody GPartnerOdgovorPodaci noviPartner, @RequestParam("kupac") Optional<Boolean> kupac){
		return service.snimiPreduzece(noviPartner, kupac);//return service.snimiPreduzece(noviPartner, kupac)
		}
	
	@DeleteMapping("/partner/brisi")
	public ResponseEntity<GPartnerOdgovor> brisi(@RequestParam("id") Long id, @RequestParam("kupac") Optional<Boolean> kupac){
		return service.brisiPreduzece(id, kupac);
		}
	
	}
