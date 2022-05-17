package rs.atekom.infosystem.server.e.tipdokumenta;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.atekom.infosystem.baza.e.tipdokumenta.ETipDokumentaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class ETipDokumentaRest extends OsnovniRest{

	@Autowired
	ETipDokumentaService service;
	
	//@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/tipdokumenta")
	public ResponseEntity<ETipDokumentaOdgovor> pretraga(@RequestParam("pretraga") Optional<String> pretraga, @RequestParam("pretplatnikId") Optional<Long> pretplatnik){
		try {
			return new ResponseEntity<ETipDokumentaOdgovor>(service.pretraga(pretraga, pretplatnik), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
