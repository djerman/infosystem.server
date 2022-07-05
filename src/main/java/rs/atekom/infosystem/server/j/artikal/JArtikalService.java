package rs.atekom.infosystem.server.j.artikal;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.f.brojac.FBrojac;
import rs.atekom.infosystem.baza.j.JArtikal;
import rs.atekom.infosystem.baza.j.JArtikalOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.jedinicamere.AJedinicaMereRepo;
import rs.atekom.infosystem.server.a.poreskatarifa.APoreskaTarifaRepo;
import rs.atekom.infosystem.server.a.tipbrojaca.ATipBrojacaRepo;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;
import rs.atekom.infosystem.server.f.brojac.FBrojacRepo;
import rs.atekom.infosystem.server.i.grupaartikala.IGrupaArtikalaRepo;

@Service("artikalService")
public class JArtikalService extends OsnovniService{

	@Autowired
	private JArtikalRepo repo;
	@Autowired
	private AJedinicaMereRepo repoJedinice;
	@Autowired
	private APoreskaTarifaRepo repoTarifa;
	@Autowired
	private IGrupaArtikalaRepo repoGrupa;
	@Autowired
	private DPretplatnikRepo repoPretplantik;
	@Autowired
	private FBrojacRepo repoBrojac;
	@Autowired
	private ATipBrojacaRepo repoTipBrojaca;
	
	public JArtikalOdgovor vratiListuPoPretplatniku(Long pretplatnikId, Optional<String> pretraga, Optional<Integer> tip, int strana) {
		JArtikalOdgovor odgovor = new JArtikalOdgovor();
		Pageable pageable = PageRequest.of(strana(strana), brZapisaPoStrani);
		String pojam = null;
		Integer tipArtikla = null;
		if(pretraga != null && pretraga.isPresent())
			pojam = pretraga.get();
		if(tip != null && tip.isPresent())
			tipArtikla = tip.get();
		Page<JArtikal> artikliPoStrani = repo.pretragaArtikalaPretplatnika(pretplatnikId, pojam, tipArtikla, pageable);
		if(artikliPoStrani != null) {
			odgovor.setArtikli(artikliPoStrani.getContent());
			odgovor.setTrenutnaStrana(artikliPoStrani.getNumber());
			odgovor.setUkupnoZapisa(artikliPoStrani.getTotalElements());
			odgovor.setUkupnoStrana(artikliPoStrani.getTotalPages());
		}
		odgovor.setJedinice(repoJedinice.findByIzbrisanFalse());
		odgovor.setTarife(repoTarifa.findAllByOrderBySifraAsc());
		odgovor.setGrupe(repoGrupa.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(repoPretplantik.findById(pretplatnikId).get()));
		return odgovor;
	}
	
	/**
	 * pri upisu provera: 
	 * da li postoji artikal sa istom šifrom, 
	 * da li je verzija ista, 
	 * da li je šifra i ime prazno 
	 * vratiti sa stranom na kojoj se nalazi novi artikal?
	 * @param noviArtikal
	 * @return
	 */
	@Transactional
	public ResponseEntity<JArtikalOdgovor> snimiArtikal(JArtikal noviArtikal) {
		List<JArtikal> postojeci = repo.findByPretplatnikAndSifraAndIzbrisanFalse(noviArtikal.getPretplatnik(), noviArtikal.getSifra());
		List<JArtikal> postojeci2 = repo.findByPretplatnikAndNazivAndIzbrisanFalse(noviArtikal.getPretplatnik(), noviArtikal.getNaziv());
		Pageable pageable = PageRequest.of(strana(1), brZapisaPoStrani);
		JArtikalOdgovor odgovor = new JArtikalOdgovor();
		return repo.findById(noviArtikal.getId() != null ? noviArtikal.getId() : 0L)
				.map(artikal -> {
					try {
						Boolean postoji = false;
						if(postojeci.size() > 0) {
							for(JArtikal art : postojeci) {
								if(!art.getId().equals(noviArtikal.getId())) {
									postoji = true;
								}
							}
						}
						if(postojeci2.size() > 0) {
							for(JArtikal art : postojeci2) {
								if(!art.getId().equals(noviArtikal.getId())) {
									postoji = true;
								}
							}
						}
						if(postoji) {
							return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.ALREADY_REPORTED);
						}else {
							if(noviArtikal.getVerzija() == artikal.getVerzija()) {
								artikal = noviArtikal;
								artikal.setVerzija(artikal.getVerzija() + 1);
								repo.save(artikal);
								Page<JArtikal> artikliPoStrani = repo.pretragaArtikalaPretplatnika(repo.save(artikal).getPretplatnik().getId(), null, artikal.getTip(), pageable);
								odgovor.setArtikli(artikliPoStrani.getContent());
								odgovor.setTrenutnaStrana(artikliPoStrani.getNumber());
								odgovor.setUkupnoZapisa(artikliPoStrani.getTotalElements());
								odgovor.setUkupnoStrana(artikliPoStrani.getTotalPages());
								return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.ACCEPTED);
							}else {
								return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.MULTI_STATUS);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}).orElseGet(() -> {
					try {
						if((postojeci != null && postojeci.size() > 0) || (postojeci2 != null && postojeci2.size() > 0)) {
							return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.ALREADY_REPORTED);
						}else {
							if(noviArtikal.getNaziv() == null || noviArtikal.getNaziv().equals("")) {
								return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.FORBIDDEN);
							}else {
								noviArtikal.setVerzija(0);
								noviArtikal.setIzbrisan(false);
								if(noviArtikal.getSifra() == null || noviArtikal.getSifra().equals(""))
									postaviBrojac(noviArtikal);
								repo.save(noviArtikal);
								Page<JArtikal> artikliPoStrani = repo.pretragaArtikalaPretplatnika(repo.save(noviArtikal).getPretplatnik().getId(), null, noviArtikal.getTip(), pageable);
								odgovor.setArtikli(artikliPoStrani.getContent());
								odgovor.setTrenutnaStrana(artikliPoStrani.getNumber());
								odgovor.setUkupnoZapisa(artikliPoStrani.getTotalElements());
								odgovor.setUkupnoStrana(artikliPoStrani.getTotalPages());
								return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.ACCEPTED);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.INTERNAL_SERVER_ERROR);
						}
				});
	}
	
	/**
	 * brisanje artikla i vraćanje liste za pregled
	 * @param artikalId
	 * @return
	 */
	@Transactional
	public ResponseEntity<JArtikalOdgovor> brisi(Long artikalId){
		if(artikalId != null) {
			JArtikalOdgovor odgovor = new JArtikalOdgovor();
			JArtikal artikal = repo.findById(artikalId).get();
			Pageable pageable = PageRequest.of(strana(1), brZapisaPoStrani);
			try {
				repo.delete(artikal);
				Page<JArtikal> artikliPoStrani = repo.pretragaArtikalaPretplatnika(artikal.getPretplatnik().getId(), null, null, pageable);
				odgovor.setArtikli(artikliPoStrani.getContent());
				odgovor.setTrenutnaStrana(artikliPoStrani.getNumber());
				odgovor.setUkupnoZapisa(artikliPoStrani.getTotalElements());
				odgovor.setUkupnoStrana(artikliPoStrani.getTotalPages());
				return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					artikal.setIzbrisan(true);
					artikal.setVerzija(artikal.getVerzija() + 1);
					Page<JArtikal> artikliPoStrani = repo.pretragaArtikalaPretplatnika(repo.save(artikal).getPretplatnik().getId(), null, null, pageable);
					odgovor.setArtikli(artikliPoStrani.getContent());
					odgovor.setTrenutnaStrana(artikliPoStrani.getNumber());
					odgovor.setUkupnoZapisa(artikliPoStrani.getTotalElements());
					odgovor.setUkupnoStrana(artikliPoStrani.getTotalPages());
					return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.ACCEPTED);
				}catch (Exception ee) {
					ee.printStackTrace();
					return new ResponseEntity<JArtikalOdgovor>(odgovor, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}else {
			return new ResponseEntity<JArtikalOdgovor>(HttpStatus.FORBIDDEN);
		}
	}
	
	private void postaviBrojac(JArtikal artikal) {
		FBrojac brojac = repoBrojac.findByPretplatnikAndTip(artikal.getPretplatnik(), repoTipBrojaca.findByTip(22));
		String broj = String.format("%0" + brojac.getBrojPolja() + "d", brojac.getStanje());
		artikal.setSifra(broj);
		brojac.setStanje(brojac.getStanje() + 1);
		repoBrojac.save(brojac);
	}
	
}
