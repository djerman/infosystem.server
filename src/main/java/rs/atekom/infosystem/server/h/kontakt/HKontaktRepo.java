package rs.atekom.infosystem.server.h.kontakt;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.h.HKontakt;

@Repository("kontaktRepo")
public interface HKontaktRepo extends PagingAndSortingRepository<HKontakt, Long>{

	public Optional<HKontakt> findByKorisnickoAndLozinka(String korisnicko, String lozinka);
	
	public HKontakt findByKorisnickoAndIzbrisanFalseAndAktivanTrueAndPristupTrue(String korisnicko);
	
	public HKontakt findTopByKorisnickoAndIzbrisanFalseAndAktivanTrueAndPristupTrue(String korisnicko);
	
	public HKontakt findByKorisnickoAndPretplatnikIdAndAktivanTrueAndPristupTrue(String korisnicko, Long id);
	
	}
