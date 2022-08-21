package rs.atekom.infosystem.server.a.poreskatarifa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.a.poreskatarifa.APoreskaTarifaOdgovor;

@RestController
@Validated
public class APoreskaTarifaRest {

	@Autowired
	private APoreskaTarifaRepo repo;
	
	@GetMapping("/poresketarife")
	public ResponseEntity<APoreskaTarifaOdgovor> lista(){
		try {
			return new ResponseEntity<APoreskaTarifaOdgovor>(new APoreskaTarifaOdgovor(repo.findAllByOrderBySifraAsc()), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
