package rs.atekom.infosystem.server.i.grupaartikala;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rs.atekom.infosystem.baza.i.grupaartikala.IGrupaArtikala;
import rs.atekom.infosystem.baza.i.grupaartikala.IGrupaArtikalaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class IGrupaArtikalaRest extends OsnovniRest{

	@Autowired
	private IGrupaArtikalaService service;
	
	@PreAuthorize("hasAuthority('SISTEM')"
			+ " || hasAuthority('AGENCIJA')"
			+ " || hasAuthority('ADMINISTRATOR')"
			+ " || hasAuthority('KORISNIK')")
	@GetMapping("/grupeartikala")
	public ResponseEntity<IGrupaArtikalaOdgovor> lista(Long pretplatnikId){
		try {
			return new ResponseEntity<IGrupaArtikalaOdgovor>(service.lista(pretplatnikId), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')"
			+ " || hasAuthority('AGENCIJA')"
			+ " || hasAuthority('ADMINISTRATOR')")
	@PutMapping("/grupaartikala/")
	public ResponseEntity<IGrupaArtikalaOdgovor> snimi(@RequestBody IGrupaArtikala grupa){
		return service.snimi(grupa);
		}
	
	@PreAuthorize("hasAuthority('SISTEM')"
			+ " || hasAuthority('AGENCIJA')"
			+ " || hasAuthority('ADMINISTRATOR')")
	@DeleteMapping("/grupaartikala/{id}")
	public ResponseEntity<IGrupaArtikalaOdgovor> izbrisi(@PathVariable Long id){
		return service.izbrisi(id);
		}
	
	}
