package rs.atekom.infosystem.server.g.partner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.atekom.infosystem.baza.f.brojac.FBrojac;
import rs.atekom.infosystem.baza.g.GPartner;
import rs.atekom.infosystem.baza.g.GPartnerOdgovor;
import rs.atekom.infosystem.baza.g.GPartnerOdgovorPodaci;
import rs.atekom.infosystem.baza.i.IAdresa;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.tipbrojaca.ATipBrojacaRepo;
import rs.atekom.infosystem.server.c.mesto.CMestoRepo;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;
import rs.atekom.infosystem.server.e.konto.EKontoRepo;
import rs.atekom.infosystem.server.f.brojac.FBrojacRepo;
import rs.atekom.infosystem.server.f.grupapartnera.FGrupaPartneraRepo;
import rs.atekom.infosystem.server.f.preduzece.FPreduzeceRepo;
import rs.atekom.infosystem.server.i.adresa.IAdresaRepo;

@Service(value = "partnerService")
public class GPartnerService extends OsnovniService{

	@Autowired
	GPartnerRepo repo;
	@Autowired
	FGrupaPartneraRepo repoGrupa;
	@Autowired
	CMestoRepo repoMesto;
	@Autowired
	DPretplatnikRepo repoPretplatnik;
	@Autowired
	FPreduzeceRepo repoPreduzece;
	@Autowired
	IAdresaRepo repoAdresa;
	@Autowired
	private FBrojacRepo repoBrojac;
	@Autowired
	private ATipBrojacaRepo repoTipBrojaca;
	@Autowired
	private EKontoRepo repoKonto;
	
	@Transactional
	public ResponseEntity<GPartnerOdgovor> snimiPreduzece(GPartnerOdgovorPodaci noviPodaciPartnera, Optional<Boolean> kupac){
		GPartner noviPartner = noviPodaciPartnera.getPartner();
		List<GPartner> lista = repo.proveraPreduzeca(noviPartner.getPretplatnik().getId(), noviPartner.getPreduzece().getPib(), noviPartner.getPreduzece().getMb());
		//System.out.println("broj zapisa " + lista.size() + " id: " + noviPartner.getId());
		if(lista == null || lista.size() <= 1) {
			try {
				return repo.findById(noviPartner.getId() == null ? 0L : noviPartner.getId())
						.map(partner -> {
							if(lista != null && lista.size() == 1 && lista.get(0).getId().equals(partner.getId())) {
								if(partner.getVerzija().equals(noviPartner.getVerzija())) {
									if(noviPartner.getSifra() == null || !noviPartner.getSifra().equals("")
											|| noviPartner.getSifra().isBlank() || noviPartner.getSifra().isEmpty()) {
										if(partner.getSifra() != null && !partner.getSifra().equals("")
												&& !partner.getSifra().isBlank() && !partner.getSifra().isEmpty()) {
											noviPartner.setSifra(partner.getSifra());
										}else {
											postaviBrojac(noviPartner);
										}
									}
									partner.setGrupaPartnera(noviPartner.getGrupaPartnera());
									partner.setSifra(noviPartner.getSifra());
									partner.setKupac(noviPartner.getKupac());
									partner.setKupacKonto(noviPartner.getKupacKonto());
									partner.setDobavljac(noviPartner.getDobavljac());
									partner.setDobavljacKonto(noviPartner.getDobavljacKonto());
									partner.setKupacRabat(noviPartner.getKupacRabat());
									partner.setDobavljacRabat(noviPartner.getDobavljacRabat());
									partner.setPreduzece(noviPartner.getPreduzece());
									partner.setVerzija(partner.getVerzija() + 1);
									partner.setJezik(noviPartner.getJezik());
									//System.out.println("ažuriranje ... " + (partner.getGrupaPartnera() == null ? "nema grupe " : partner.getGrupaPartnera()));
									partner = repo.save(partner);
									
									if(noviPodaciPartnera.getSediste() != null){
										IAdresa sediste = noviPodaciPartnera.getSediste();
										sediste.setNaziv("Sedište");
										sediste.setIzbrisan(false);
										sediste.setVerzija(partner.getVerzija());
										sediste.setPartner(partner);
										if(sediste.getId() == null) {
											sediste.setSediste(true);
											}
										repoAdresa.save(sediste);
										}
									return listaPreduzeca(null, partner.getPretplatnik().getId(), kupac);
									}else {
										System.out.println("greška multi ");
										return new ResponseEntity<GPartnerOdgovor>(new GPartnerOdgovor(), HttpStatus.MULTI_STATUS);
										}
								}else {
									System.out.println("greška postoji 1 ");
									return new ResponseEntity<GPartnerOdgovor>(HttpStatus.ALREADY_REPORTED);
									}
							})
						.orElseGet(() -> {
							if(lista != null && lista.size() > 0) {
								return new ResponseEntity<GPartnerOdgovor>(HttpStatus.ALREADY_REPORTED);
								}else {
									if(noviPartner.getSifra() == null || !noviPartner.getSifra().equals("")
											|| noviPartner.getSifra().isBlank() || noviPartner.getSifra().isEmpty()) 
										postaviBrojac(noviPartner);
									noviPartner.setId(null);
									noviPartner.setIzbrisan(false);
									noviPartner.setVerzija(0);
									noviPartner.getPreduzece().setVerzija(0);
									noviPartner.getPreduzece().setIzbrisan(false);
									Long pretplatnik = repo.save(noviPartner).getPretplatnik().getId();
									
									IAdresa sediste = noviPodaciPartnera.getSediste();
									if(sediste != null) {
										sediste.setPartner(noviPartner);
										sediste.setIzbrisan(false);
										sediste.setNaziv("Sedište");
										sediste.setSediste(true);
										sediste.setVerzija(noviPartner.getVerzija());
										repoAdresa.save(sediste);
										}
									
									return listaPreduzeca(null, pretplatnik, kupac);
									}
							});
				}catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
			}else {
				System.out.println("greška postoji 2 ");
				return new ResponseEntity<GPartnerOdgovor>(HttpStatus.ALREADY_REPORTED);
				}
		}
	
	@Transactional
	public ResponseEntity<GPartnerOdgovor> brisiPreduzece(Long partnerId, Optional<Boolean> kupac){
		GPartner partner = repo.findById(partnerId).get();
		try {
			repoAdresa.deleteAll(repoAdresa.findByPartnerAndIzbrisanFalse(partner));
			//repoPreduzece.delete(preduzece);
			repo.delete(partner);
			return listaPreduzeca(null, partner.getPretplatnik().getId(), kupac);
			}catch (Exception e) {
				e.printStackTrace();
				try {
					partner.setIzbrisan(false);
					repo.save(partner);
					return listaPreduzeca(null, partner.getPretplatnik().getId(), kupac);
					}catch (Exception ee) {
						//ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	
	
	public ResponseEntity<GPartnerOdgovor> listaPreduzeca(Optional<String> pretraga, Long pretplatnikId, Optional<Boolean> kupac){
		try {
			GPartnerOdgovor odgovor = new GPartnerOdgovor();
			String pojam = null;
			
			if(pretraga != null && pretraga.isPresent()) {
				pojam = pretraga.get();
				}
			
			Boolean kup = null;
			if(kupac != null && kupac.isPresent()) {
				kup = kupac.get();
				}
			
			List<GPartner> lista = null;
			if(kup == null) {
				lista = repo.pretragaPreduzeca(pojam, pretplatnikId);
				}else {
					if(kup) {
						lista = repo.pretragaPreduzecaKupaca(pojam, pretplatnikId);
						}else {
							lista = repo.pretragaPreduzecaDobavljaca(pojam, pretplatnikId);
							}
					}
			List<GPartnerOdgovorPodaci> listaSaPodacima = new ArrayList<GPartnerOdgovorPodaci>();
			for(GPartner partner : lista) {
				listaSaPodacima.add(new GPartnerOdgovorPodaci(partner, repoAdresa.findTopByPartnerAndSedisteTrueAndIzbrisanFalse(partner), repoAdresa.findByPartnerAndIzbrisanFalseAndSedisteFalse(partner)));
				}
			odgovor.setListaSaPodacima(listaSaPodacima);
			odgovor.setGrupe(repoGrupa.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(repoPretplatnik.findById(pretplatnikId).get()));
			odgovor.setMesta(repoMesto.findByIzbrisanFalseOrderByNazivAsc());
			odgovor.setKonta(repoKonto.findByPretplatnikOrPretplatnikIsNullAndIzbrisanFalseOrderBySifraAsc(repoPretplatnik.findById(pretplatnikId).get()));
			return new ResponseEntity<GPartnerOdgovor>(odgovor, HttpStatus.ACCEPTED);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	
	private void postaviBrojac(GPartner partner) {
		FBrojac brojac = repoBrojac.findByPretplatnikAndTip(partner.getPretplatnik(), repoTipBrojaca.findByTip(12));
		String broj = String.format("%0" + brojac.getBrojPolja() + "d", brojac.getStanje());
		partner.setSifra(broj);
		brojac.setStanje(brojac.getStanje() + 1);
		repoBrojac.save(brojac);
	}
	
}
