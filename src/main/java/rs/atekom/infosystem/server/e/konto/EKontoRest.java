package rs.atekom.infosystem.server.e.konto;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.e.konto.EKontoOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class EKontoRest extends OsnovniRest{

	@Autowired
	EKontoService service;
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/konta")
	public ResponseEntity<EKontoOdgovor> pretraga(@RequestParam("pretraga") Optional<String> pretraga, 
			@RequestParam("podgrupaId") Optional<Long> podgrupa, @RequestParam("pretplatnikId") Optional<Long> pretplatnik){
		try {
			return new ResponseEntity<EKontoOdgovor>(service.pretraga(pretraga, podgrupa, pretplatnik), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
