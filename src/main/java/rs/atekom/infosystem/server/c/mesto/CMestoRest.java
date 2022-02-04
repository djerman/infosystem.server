package rs.atekom.infosystem.server.c.mesto;

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

import rs.atekom.infosystem.baza.c.CMesto;
import rs.atekom.infosystem.baza.c.CMestoOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class CMestoRest extends OsnovniRest{

	@Autowired
	CMestoRepo repo;
	@Autowired
	CMestoService service;
	
	/**
	 * preuzimanje mesta po drzavi
	 * @param drzavaId
	 * @return
	 */
	@GetMapping("/mesta/drzava")
	public ResponseEntity<CMestoOdgovor> poDrzavi(@RequestParam(value = "drzavaId") Long drzavaId){
		try {
			return new ResponseEntity<CMestoOdgovor>(service.listaPoDrzavi(drzavaId), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	/**
	 * vraća listu mesta po opštini
	 * @param opstinaId
	 * @return
	 */
	@GetMapping("/mesta/opstina")
	public ResponseEntity<CMestoOdgovor> poOpstini(@RequestParam(value = "opstinaId") Long opstinaId){
		try {
			return new ResponseEntity<CMestoOdgovor>(service.listaPoOpstini(opstinaId), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	/**
	 * vraća listu mesta po pojmu pretrage
	 * @param pretraga
	 * @return
	 */
	@GetMapping("/mesta/pretraga")
	public ResponseEntity<CMestoOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga){
		try {
			return new ResponseEntity<CMestoOdgovor>(service.lista(pretraga), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@PutMapping("/mesto/snimi")
	@Transactional
	public ResponseEntity<CMestoOdgovor> snimiIzmeni(@RequestBody CMesto novoMesto){
		try {
			return repo.findById(novoMesto.getId())
					.map(mesto -> {
						if(mesto.getVerzija() == novoMesto.getVerzija()) {
							CMestoOdgovor odgovor;
							if(novoMesto.getSr() == null || novoMesto.getSr().equals("") || novoMesto.getSr().isEmpty() || novoMesto.getSr().isBlank()) {
								novoMesto.setSr(novoMesto.getNaziv());
								}
							if(novoMesto.getEn() == null || novoMesto.getEn().equals("") || novoMesto.getEn().isEmpty() || novoMesto.getEn().isBlank()) {
								novoMesto.setEn(novoMesto.getNaziv());
								}
							mesto = novoMesto;
							mesto.setVerzija(novoMesto.getVerzija() + 1);
							repo.save(mesto);
							odgovor = service.lista(null);
							return new ResponseEntity<CMestoOdgovor>(odgovor, HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<CMestoOdgovor>(HttpStatus.ALREADY_REPORTED);
								}
						
						/*
						if(mesto.getOpstina() == null) {
							odgovor = service.listaPoDrzavi(mesto.getDrzava().getId());
							}else {
								odgovor = service.listaPoOpstini(mesto.getOpstina().getId());
								}
						*/
						
						
					})
					.orElseGet(() -> {
						CMestoOdgovor odgovor;
						novoMesto.setId(null);
						novoMesto.setIzbrisan(false);
						novoMesto.setVerzija(0);
						if(novoMesto.getSr() == null || novoMesto.getSr().equals("") || novoMesto.getSr().isEmpty() || novoMesto.getSr().isBlank()) {
							novoMesto.setSr(novoMesto.getNaziv());
							}
						if(novoMesto.getEn() == null || novoMesto.getEn().equals("") || novoMesto.getEn().isEmpty() || novoMesto.getEn().isBlank()) {
							novoMesto.setEn(novoMesto.getNaziv());
							}
						repo.save(novoMesto);
						/*
						if(novoMesto.getOpstina() == null) {
							odgovor = service.listaPoDrzavi(novoMesto.getDrzava().getId());
							}else {
								odgovor = service.listaPoOpstini(novoMesto.getOpstina().getId());
								}
						*/
						odgovor = service.lista(null);
						return new ResponseEntity<CMestoOdgovor>(odgovor, HttpStatus.ACCEPTED);
					});
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	/**
	 * brisanje mesta
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('SISTEM')")
	@DeleteMapping("/mesto/brisi/{id}")
	@Transactional
	public ResponseEntity<CMestoOdgovor> brisi(@PathVariable Long id){
		CMesto mesto = repo.findById(id).get();
		if(mesto != null) {
			try {
				repo.deleteById(id);
				CMestoOdgovor odgovor;
				if(mesto.getOpstina() == null) {
					odgovor = service.listaPoDrzavi(mesto.getDrzava().getId());
					}else {
						odgovor = service.listaPoOpstini(mesto.getOpstina().getId());
						}
				return new ResponseEntity<CMestoOdgovor>(odgovor, HttpStatus.ACCEPTED);
				}catch (Exception e) {
					try {
						mesto.setIzbrisan(true);
						CMestoOdgovor odgovor;
						if(mesto.getOpstina() == null) {
							odgovor = service.listaPoDrzavi(mesto.getDrzava().getId());
							}else {
								odgovor = service.listaPoOpstini(mesto.getOpstina().getId());
								}
						return new ResponseEntity<CMestoOdgovor>(odgovor, HttpStatus.ACCEPTED);
						}catch (Exception ee) {
							e.printStackTrace();
							return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
							}
					}
			}else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	}
