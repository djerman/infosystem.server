package rs.atekom.infosystem.server.a.prava;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.a.prava.APravaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class APravaRest extends OsnovniRest{

	@Autowired
	APravaService service;
	
	@PreAuthorize("hasAuthority('SISTEM')"
			+ " || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/prava")
	public ResponseEntity<APravaOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga){
		try {
			return new ResponseEntity<APravaOdgovor>(service.listaPrava(pretraga), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
