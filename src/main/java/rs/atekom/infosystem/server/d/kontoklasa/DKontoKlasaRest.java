package rs.atekom.infosystem.server.d.kontoklasa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.d.kontoklasa.DKontoKlasaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class DKontoKlasaRest extends OsnovniRest{

	@Autowired
	private DKontoKlasaService service;
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/klasekonta")
	public ResponseEntity<DKontoKlasaOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga){
		try {
			return new ResponseEntity<DKontoKlasaOdgovor>(service.pretraga(pretraga), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
