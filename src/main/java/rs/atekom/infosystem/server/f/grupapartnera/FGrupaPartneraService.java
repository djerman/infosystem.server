package rs.atekom.infosystem.server.f.grupapartnera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.f.grupapartnera.FGrupaPartnera;
import rs.atekom.infosystem.baza.f.grupapartnera.FGrupaPartneraOdgovor;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;

@Service
public class FGrupaPartneraService extends OsnovniService{

	@Autowired
	private FGrupaPartneraRepo repo;
	@Autowired
	private DPretplatnikRepo repoPretplatnik;
	
	public FGrupaPartneraOdgovor lista(Long pretplatnikId) {
		try {
			return new FGrupaPartneraOdgovor(repo.findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(repoPretplatnik.findById(pretplatnikId).get()));
			}catch (Exception e) {
				e.printStackTrace();
				return null;
				}
		}
	
	public ResponseEntity<FGrupaPartneraOdgovor> snimi(FGrupaPartnera nova){
		return repo.findById(nova.getId() != null ? nova.getId() : 0L)
				.map(grupa -> {
					try {
						grupa = nova;
						grupa.setVerzija(nova.getVerzija() + 1);
						repo.save(grupa);
						return new ResponseEntity<FGrupaPartneraOdgovor>(lista(grupa.getPretplatnik().getId()), HttpStatus.ACCEPTED);
						}catch (Exception e) {
							return new ResponseEntity<FGrupaPartneraOdgovor>(HttpStatus.INTERNAL_SERVER_ERROR);
							}
					}).orElseGet(() -> {
						try {
							nova.setId(null);
							nova.setIzbrisan(false);
							nova.setVerzija(0);
							repo.save(nova);
							return new ResponseEntity<FGrupaPartneraOdgovor>(lista(nova.getPretplatnik().getId()), HttpStatus.ACCEPTED);
							}catch (Exception e) {
								return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
								}
					});
				}
	
	public ResponseEntity<FGrupaPartneraOdgovor> izbrisi(Long grupaId){
		FGrupaPartnera grupa = repo.findById(grupaId).get();
		try {
			repo.delete(grupa);
			return new ResponseEntity<FGrupaPartneraOdgovor>(lista(grupaId), HttpStatus.ACCEPTED);
			}catch (Exception e) {
				try {
					grupa.setIzbrisan(true);
					return new ResponseEntity<FGrupaPartneraOdgovor>(lista(repo.save(grupa).getPretplatnik().getId()), HttpStatus.ACCEPTED);
					}catch (Exception ee) {
						ee.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
						}
				}
		}
	
	}
