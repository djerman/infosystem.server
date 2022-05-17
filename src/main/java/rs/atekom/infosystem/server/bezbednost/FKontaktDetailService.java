package rs.atekom.infosystem.server.bezbednost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.h.HKontakt;
import rs.atekom.infosystem.server.a.uloga.AUlogaRepo;
import rs.atekom.infosystem.server.h.kontakt.HKontaktRepo;

@Service
public class FKontaktDetailService implements UserDetailsService{

	@Autowired
	HKontaktRepo repo;
	@Autowired
	AUlogaRepo repoUloga;
	
	@Override
	public UserDetails loadUserByUsername(String korisnik) throws UsernameNotFoundException {
		UserDetails user;
		HKontakt kontakt = repo.findByKorisnickoAndIzbrisanFalseAndAktivanTrueAndPristupTrue(korisnik);
		if(kontakt == null) {
			System.out.println("Korisnik nije pronađen... ");
			throw new UsernameNotFoundException(korisnik);
			}else {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String uloga = kontakt.getUloga().getSr();
				user = User.withUsername(kontakt.getKorisnicko()).password(passwordEncoder.encode(kontakt.getLozinka())).authorities(uloga).build();
				}
		return user;
		}
	
	}
