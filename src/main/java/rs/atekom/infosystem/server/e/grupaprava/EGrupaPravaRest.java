package rs.atekom.infosystem.server.e.grupaprava;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import rs.atekom.infosystem.baza.e.grupaprava.EGrupaPrava;
import rs.atekom.infosystem.baza.e.grupaprava.EGrupaPravaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

public class EGrupaPravaRest extends OsnovniRest{

	@Autowired
	private EGrupaPravaService service;
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/grupaprava")
	public ResponseEntity<EGrupaPravaOdgovor> lista(@RequestParam("pretplatnikId") Optional<Long> pretplatnikId){
		try {
			return new ResponseEntity<EGrupaPravaOdgovor>(service.lista(pretplatnikId), HttpStatus.ACCEPTED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/grupaprava/snimi")
	public ResponseEntity<EGrupaPravaOdgovor> snimi(@RequestBody EGrupaPrava grupaPrava){
		return service.snimi(grupaPrava);
	}
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/grupaprava/{id}")
	public ResponseEntity<EGrupaPravaOdgovor> izbrisi(@PathVariable Long id){
		return service.izbrisi(id);
	}
	
}
