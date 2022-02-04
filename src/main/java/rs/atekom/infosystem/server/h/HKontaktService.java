package rs.atekom.infosystem.server.h;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.server.OsnovniService;

@Service(value = "kontaktService")
public class HKontaktService extends OsnovniService{

	@Autowired
	HKontaktRepo kontaktRepo;
	
	public boolean proveraKorisnika(String korisnicko, Long pretplatnkId) {
		boolean provera = false;
		if (kontaktRepo.findByKorisnickoAndPretplatnikIdAndAktivanTrueAndPristupTrue(korisnicko, pretplatnkId) != null)
			provera = true;
		return provera;
	}
}
