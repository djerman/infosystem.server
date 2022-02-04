package rs.atekom.infosystem.server.b.opstina;

import java.util.Optional;

import javax.transaction.Transactional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.atekom.infosystem.baza.b.BOpstina;
import rs.atekom.infosystem.baza.b.BOpstinaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class BOpstinaRest extends OsnovniRest{

	@Autowired
	BOpstinaRepo repo;
	@Autowired
	BOpstinaService service;
	
	/**
	 * vraća listu opština prema izabranoj državi
	 * @param drzavaId
	 * @return
	 */
	@GetMapping("/opstine/drzava")
	public ResponseEntity<BOpstinaOdgovor> poDrzavi(@RequestParam(value = "drzavaId") Long drzavaId){
		try {
			return new ResponseEntity<BOpstinaOdgovor>(service.lista(drzavaId), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	/**
	 * vraća listu po pretrazi
	 * @param pretraga
	 * @return
	 */
	@GetMapping("/opstine/pretraga")
	public ResponseEntity<BOpstinaOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga){
		try {
			return new ResponseEntity<BOpstinaOdgovor>(service.pretraga(pretraga), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@PutMapping("/opstina/snimi")
	@Transactional
	public ResponseEntity<BOpstinaOdgovor> snimiIzmeni(@RequestBody BOpstina novaOpstina){
		try {
			return repo.findById(novaOpstina.getId())
					.map(opstina -> {
						if(opstina.getVerzija() == novaOpstina.getVerzija()) {
							opstina = novaOpstina;
							repo.save(opstina);
							//return new ResponseEntity<BOpstinaOdgovor>(service.lista(repo.save(opstina).getDrzava().getId()), HttpStatus.ACCEPTED);
							return new ResponseEntity<BOpstinaOdgovor>(service.pretraga(null), HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<BOpstinaOdgovor>(HttpStatus.ALREADY_REPORTED);
								}
						})
					.orElseGet(() -> {
						novaOpstina.setId(null);
						novaOpstina.setIzbrisan(false);
						novaOpstina.setVerzija(0);
						repo.save(novaOpstina);
						return new ResponseEntity<BOpstinaOdgovor>(service.pretraga(null), HttpStatus.ACCEPTED);
					});
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	/**
	 * brisanje 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('SISTEM')")
	@DeleteMapping("/opstina/brisi/{id}")
	@Transactional
	public ResponseEntity<BOpstinaOdgovor> brisi(@PathVariable Long id){
		BOpstina opstina = repo.findById(id).get();
		if(opstina != null) {
			try {
				repo.deleteById(id);
				return new ResponseEntity<BOpstinaOdgovor>(service.lista(opstina.getDrzava().getId()), HttpStatus.ACCEPTED);
				}catch (Exception e) {
					try {
						opstina.setIzbrisan(true);
						return new ResponseEntity<BOpstinaOdgovor>(service.lista(opstina.getDrzava().getId()), HttpStatus.ACCEPTED);
						}catch (Exception ee) {
							ee.printStackTrace();
							return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
							}
					}
			}else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
