package rs.atekom.infosystem.server.f.grupeprava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.e.grupaprava.EGrupaPrava;
import rs.atekom.infosystem.baza.f.grupeprava.FGrupePrava;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.e.grupaprava.EGrupaPravaRepo;

@Service
public class FGrupePravaService extends OsnovniService{

	@Autowired
	private FGrupePravaRepo repo;
	@Autowired
	private EGrupaPravaRepo grupaRepo;
	
	public List<FGrupePrava> pravaPoGrupi(Long grupaId){
		return repo.findByGrupaPrava(grupaRepo.findById(grupaId).get());
		}
	
	public List<FGrupePrava> snimi(Long grupaId, List<FGrupePrava> lista){
		EGrupaPrava grupa = grupaRepo.findById(grupaId).get();
		for(FGrupePrava gr : lista) {
			if(repo.findTopByGrupaPravaAndPravo(gr.getGrupaPrava(), gr.getPravo()) == null) {
				repo.save(gr);
				}
			}
		return repo.findByGrupaPrava(grupa);
		}
	
	}
