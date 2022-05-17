package rs.atekom.infosystem.server.h.kontakt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.a.prava.APrava;
import rs.atekom.infosystem.baza.g.GPartnerOdgovorPodaci;
import rs.atekom.infosystem.baza.h.HKontakt;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.prava.APravaRepo;
import rs.atekom.infosystem.server.f.grupeprava.FGrupePravaRepo;
import rs.atekom.infosystem.server.g.partner.GPartnerRepo;

@Service(value = "kontaktService")
public class HKontaktService extends OsnovniService{

	@Autowired
	private HKontaktRepo kontaktRepo;
	@Autowired
	private GPartnerRepo partnerRepo;
	@Autowired
	private FGrupePravaRepo grupePravaRepo;
	@Autowired
	private APravaRepo pravaRepo;
	
	public boolean proveraKorisnika(String korisnicko, Long pretplatnkId) {
		boolean provera = false;
		if (kontaktRepo.findByKorisnickoAndPretplatnikIdAndAktivanTrueAndPristupTrue(korisnicko, pretplatnkId) != null)
			provera = true;
		return provera;
		}
	
	
	public boolean proveraKorisnikaPrekoPartnera(String korisnicko, GPartnerOdgovorPodaci partnerPodaci) {
		boolean provera = false;
		if(kontaktRepo.findByKorisnickoAndPretplatnikIdAndAktivanTrueAndPristupTrue(korisnicko, partnerPodaci.getPartner().getPretplatnik().getId()) != null) {
			provera = true;
			}
		return provera;
		}
	
	public boolean proveraKorisnikaPrekoIdPartnera(String korisnicko, Long id) {
		boolean provera = false;
		if(kontaktRepo.findByKorisnickoAndPretplatnikIdAndAktivanTrueAndPristupTrue(korisnicko, partnerRepo.findById(id).get().getId()) != null) {
			provera = true;
			}
		return provera;
		}
	
	public boolean proveraKorisnickogPrava(String korisnicko, Long pretplatnikId, String pravo) {
		boolean provera = false;
		HKontakt korisnik = kontaktRepo.findByKorisnickoAndPretplatnikIdAndAktivanTrueAndPristupTrue(korisnicko, pretplatnikId);
		if(korisnik != null && korisnik.getPristup()) {
			APrava aPravo = pravaRepo.findByPutanjaAndIzbrisanFalse(pravo);
			if(aPravo != null) {
				if(grupePravaRepo.findTopByGrupaPravaAndPravo(korisnik.getGrupaPrava(), aPravo) != null) {
					provera = true;
					}
				}
			}
		return provera;
		}
	
	}
