package rs.atekom.infosystem.server.da.kontogrupa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.da.kontogrupa.DAKontoGrupaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class DAKontoGrupaRest extends OsnovniRest{

	@Autowired
	private DAKontoGrupaService service;
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/grupekonta")
	public ResponseEntity<DAKontoGrupaOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga, 
			@RequestParam("klasaId") Optional<Long> klasa){
		try {
			return new ResponseEntity<DAKontoGrupaOdgovor>(service.pretraga(pretraga, klasa), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
