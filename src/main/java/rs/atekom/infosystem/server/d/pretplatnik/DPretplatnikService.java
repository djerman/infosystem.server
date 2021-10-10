package rs.atekom.infosystem.server.d.pretplatnik;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.a.agencija.AAgencija;
import rs.atekom.infosystem.baza.d.DPodaciZaPretplatnikaOdgovor;
import rs.atekom.infosystem.baza.d.DPretplatnik;
import rs.atekom.infosystem.baza.d.DPretplatnikOdgovor;
import rs.atekom.infosystem.baza.d.DPretplatnikPodaciOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.agencija.AAgencijaRepo;
import rs.atekom.infosystem.server.a.drzava.ADrzavaRepo;
import rs.atekom.infosystem.server.c.mesto.CMestoRepo;
import rs.atekom.infosystem.server.e.organizacija.EOrganizacijaRepo;

@Service
public class DPretplatnikService extends OsnovniService{

	@Autowired
	DPretplatnikRepo repo;
	@Autowired
	AAgencijaRepo repoAgencija;
	@Autowired
	CMestoRepo repoMesta;
	@Autowired
	ADrzavaRepo repoDrzava;
	@Autowired
	EOrganizacijaRepo repoOrganizacija;
	
	public DPretplatnikOdgovor lista(Optional<String> pretraga, Optional<Long> agencijaId) {
		DPretplatnikOdgovor odgovor = new DPretplatnikOdgovor();
		try {
			String pojam = null;
			if(pretraga != null && pretraga.isPresent()) {
				pojam = pretraga.get();
				}
			Long id = null;
			AAgencija agencija = null;
			if(agencijaId != null && agencijaId.isPresent()) {
				id = agencijaId.get();
				agencija = repoAgencija.findById(id).get();
				}
			List<DPretplatnik> pretplatnici = repo.pretraga(pojam, agencija);
			List<DPretplatnikPodaciOdgovor> saPodacima = new ArrayList<DPretplatnikPodaciOdgovor>();
			for(DPretplatnik pr : pretplatnici) {
				DPretplatnikPodaciOdgovor podaci = new DPretplatnikPodaciOdgovor();
				podaci.setPretplatnik(pr);
				podaci.setOrganizacija(repoOrganizacija.findTopByPretplatnikAndSedisteTrue(pr));
				saPodacima.add(podaci);
				}
			odgovor.setListaSaPodacima(saPodacima);
			}catch (Exception e) {
				// TODO: handle exception
				}
		return odgovor;
		}
	
	public DPodaciZaPretplatnikaOdgovor podaciZaPretplatnika() {
		DPodaciZaPretplatnikaOdgovor odgovor = new DPodaciZaPretplatnikaOdgovor();
		odgovor.setAgencije(repoAgencija.pretraga(null));
		odgovor.setMesta(repoMesta.findByDrzavaAndIzbrisanFalseOrderByNazivAsc(repoDrzava.findTopByPodrazumevanTrue()));
		return odgovor;
		}
	
	}
