package rs.atekom.infosystem.server.db.kontoracun;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.db.kontoracun.DBKontoRacunOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class DBKontoRacunRest extends OsnovniRest{

	@Autowired
	DBKontoRacunService service;
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/racunikonta")
	public ResponseEntity<DBKontoRacunOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga, 
			@RequestParam("grupaId") Optional<Long> grupa){
		try {
			return new ResponseEntity<DBKontoRacunOdgovor>(service.pretraga(pretraga, grupa), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
