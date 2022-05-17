package rs.atekom.infosystem.server.f.grupeprava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.f.grupeprava.FGrupePrava;
import rs.atekom.infosystem.baza.f.grupeprava.FGrupePravaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class FGrupePravaRest extends OsnovniRest{
	
	@Autowired
	private FGrupePravaService service;
	
	@PreAuthorize("hasAuthority('SISTEM')"
			/*+ " || hasAuthority('AGENCIJA')"*/
			+ " || hasAuthority('ADMINISTRATOR')"
			/*+ " || hasAuthority('KORISNIK')"*/)
	@GetMapping("/grupeprava")
	public ResponseEntity<FGrupePravaOdgovor> lista(@RequestParam("grupaId") Long grupaId){
		try {
			return new ResponseEntity<FGrupePravaOdgovor>(new FGrupePravaOdgovor(service.pravaPoGrupi(grupaId)), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')"
			/*+ " || hasAuthority('AGENCIJA')"*/
			+ " || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/grupeprava/snimi")
	public ResponseEntity<FGrupePravaOdgovor> snimi(@RequestParam("grupaId") Long grupaId, @RequestBody List<FGrupePrava> grupePrava){
		try{
			return new ResponseEntity<FGrupePravaOdgovor>(new FGrupePravaOdgovor(service.snimi(grupaId, grupePrava)), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
