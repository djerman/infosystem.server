package rs.atekom.infosystem.server.f.grupapartnera;

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

import rs.atekom.infosystem.baza.f.FGrupaPartnera;
import rs.atekom.infosystem.baza.f.FGrupaPartneraOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class FGrupaPartneraRest extends OsnovniRest{

	@Autowired
	FGrupaPartneraService service;
	
	//@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@GetMapping("/grupepartnera")
	public ResponseEntity<FGrupaPartneraOdgovor> lista(Long pretplatnikId){
		try {
			return new ResponseEntity<FGrupaPartneraOdgovor>(service.lista(pretplatnikId), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@PutMapping("/grupapartnera/")
	public ResponseEntity<FGrupaPartneraOdgovor> snimi(@RequestBody FGrupaPartnera grupa){
		return service.snimi(grupa);
		}
	
	@PreAuthorize("hasAuthority('SISTEM') || hasAuthority('AGENCIJA') || hasAuthority('ADMINISTRATOR')")
	@DeleteMapping("/grupapartnera/{id}")
	public ResponseEntity<FGrupaPartneraOdgovor> izbrisi(@PathVariable Long id){
		return service.izbrisi(id);
		}
	
	}
