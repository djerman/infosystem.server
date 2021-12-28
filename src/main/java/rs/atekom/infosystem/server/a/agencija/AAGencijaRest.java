package rs.atekom.infosystem.server.a.agencija;

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

import rs.atekom.infosystem.baza.a.agencija.AAgencija;
import rs.atekom.infosystem.baza.a.agencija.AAgencijaOdgovor;
import rs.atekom.infosystem.server.OsnovniRest;

@RestController
@Validated
public class AAGencijaRest extends OsnovniRest{

	@Autowired
	AAgencijaRepo repo;
	@Autowired
	AAgencijaService service;
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@GetMapping("/agencije")
	public ResponseEntity<AAgencijaOdgovor> pretraga(@RequestParam(value = "pretraga") Optional<String> pretraga){
		try {
			return new ResponseEntity<AAgencijaOdgovor>(service.napraviOdgovorSaListom(pretraga), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@PutMapping("/agencija/snimi")
	@Transactional
	public ResponseEntity<AAgencijaOdgovor> snimi(@RequestBody AAgencija novaAgencija){
		try {
			return repo.findById(novaAgencija.getId() == null ? 0L : novaAgencija.getId())
					.map(agencija -> {
						return new ResponseEntity<AAgencijaOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
						})
					.orElseGet(() -> {
						novaAgencija.setId(null);
						novaAgencija.setIzbrisan(false);
						repo.save(novaAgencija);
						return new ResponseEntity<>(service.napraviOdgovorSaListom(null), HttpStatus.ACCEPTED);
					});
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@PutMapping("/agencija/izmeni")
	@Transactional
	public ResponseEntity<AAgencijaOdgovor> izmeni(@RequestBody AAgencija novaAgencija){
		try {
			return repo.findById(novaAgencija.getId() == null ? 0L : novaAgencija.getId())
					.map(agencija -> {
						agencija = novaAgencija;
						repo.save(agencija);
						return new ResponseEntity<>(service.napraviOdgovorSaListom(null), HttpStatus.ACCEPTED);
						})
					.orElseGet(() -> {
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					});
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	@PreAuthorize("hasAuthority('SISTEM')")
	@DeleteMapping("/agencija/brisi/{id}")
	@Transactional
	public ResponseEntity<AAgencijaOdgovor> brisi(@PathVariable Long id){
		try {
			repo.deleteById(id);
			return new ResponseEntity<>(service.napraviOdgovorSaListom(null), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					AAgencija agencija = repo.findById(id).get();
					agencija.setIzbrisan(true);
					return new ResponseEntity<>(service.napraviOdgovorSaListom(null), HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	/*
	 * @PreAuthorize("!hasAuthority('USER') || (#oldPassword != null && !#oldPassword.isEmpty() "
			+ "&& authentication.principal == @userRepository.findById(#id).orElse(new net.reliqs.gleeometer.users.User()).email)")
			@PreAuthorize("!hasAuthority('USER') || (authentication.principal == #email)")
			@PostAuthorize("!hasAuthority('USER') || (returnObject != null && returnObject.email == authentication.principal)")
			
	@GetMapping("/{id}")
	@PostAuthorize("!hasAuthority('USER') || (returnObject != null && returnObject.email == authentication.principal)")
	User one(@PathVariable Long id) {
	return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
	
	   @PutMapping("/{id}")
   @PreAuthorize("!hasAuthority('USER') || (authentication.principal == @userRepository.findById(#id).orElse(new net.reliqs.gleeometer.users.User()).email)")
   void update(@PathVariable Long id, @Valid @RequestBody User res) {
       User u = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
       res.setPassword(u.getPassword());
       res.setGlee(u.getGlee());
       repository.save(res);
   }

   @PostMapping
   @PreAuthorize("!hasAuthority('USER')")
   User create(@Valid @RequestBody User res) {
       return repository.save(res);
   }

   @DeleteMapping("/{id}")
   @PreAuthorize("!hasAuthority('USER')")
   void delete(@PathVariable Long id) {
       if (repository.existsById(id)) {
           repository.deleteById(id);
       } else {
           throw new EntityNotFoundException(User.class, "id", id.toString());
       }
   }

   @PutMapping("/{id}/changePassword")
   @PreAuthorize("!hasAuthority('USER') || (#oldPassword != null && !#oldPassword.isEmpty() 
   && authentication.principal == @userRepository.findById(#id).orElse(new net.reliqs.gleeometer.users.User()).email)")
   void changePassword(@PathVariable Long id, @RequestParam(required = false) String oldPassword, @Valid @Size(min = 3) @RequestParam String newPassword) {
       User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
       if (oldPassword == null || oldPassword.isEmpty() || passwordEncoder.matches(oldPassword, user.getPassword())) {
           user.setPassword(passwordEncoder.encode(newPassword));
           repository.save(user);
       } else {
           throw new ConstraintViolationException("old password doesn't match", new HashSet<>());
       }
   }
   
   uloge: glavni administrator, agencija, admin pretplatnika, korisnik - grupa prava(opšta, i po pretplatniku)
	}
			
			*/
	}
