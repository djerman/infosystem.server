package rs.atekom.infosystem.server.bezbednost;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.h.HKontakt;
import rs.atekom.infosystem.server.h.HKontaktRepo;

@Service
public class FKontaktService {

	@Autowired
	private HKontaktRepo repo;
	
	FKontaktService(HKontaktRepo repo){
		this.repo = repo;
		}
	
	public Optional<HKontakt> nadjiKorisnika(String korisnicko, String lozinka){
		Optional<HKontakt> kontakt = repo.findByKorisnickoAndLozinka(korisnicko, lozinka);
		return Optional.ofNullable(kontakt).orElseThrow();
		}
	
	public HKontakt nadjiKorisnika(String korisnik) {
		return repo.findByKorisnickoAndIzbrisanFalseAndAktivanTrueAndPristupTrue(korisnik);
		}
	
	}
