package rs.atekom.infosystem.server.f.brojac;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.f.brojac.FBrojac;
import rs.atekom.infosystem.baza.f.brojac.FBrojacOdgovor;
import rs.atekom.infosystem.server.a.tipbrojaca.ATipBrojacaRepo;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;

@Service
public class FBrojacService {

	@Autowired
	private FBrojacRepo repo;
	@Autowired
	private DPretplatnikRepo repoPretplatnik;
	@Autowired
	private ATipBrojacaRepo repoTipBrojaca;
	
	public List<FBrojac> vratiBrojacePretplatnika(Long pretplatnikId){
		return repo.findByPretplatnik(repoPretplatnik.findById(pretplatnikId).get());
	}
	
	public FBrojac vratiBrojacPretplatnikaPoTipu(Long pretplatnikId, Long tipId) {
		return repo.findByPretplatnikAndTip(repoPretplatnik.findById(pretplatnikId).get(), repoTipBrojaca.getById(tipId));
	}
	
	/**
	 * snimanje namenjeno isključio kod snimanaja novog pretplatnika
	 * @param brojac
	 */
	public void snimiNovi(FBrojac brojac){
		try {
			repo.save(brojac);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * samo ako je smnimanje postojećeg brojača
	 */
	public ResponseEntity<FBrojacOdgovor> snimi(FBrojac brojac){
		return repo.findById(brojac.getId())
				.map(br -> {
					repo.save(brojac);
					FBrojacOdgovor odgovor = new FBrojacOdgovor();
					odgovor.setLista(repo.findByPretplatnik(brojac.getPretplatnik()));
					return new ResponseEntity<FBrojacOdgovor>(odgovor, HttpStatus.ACCEPTED);
				}).orElseGet(() -> {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				});
	}
	
}
