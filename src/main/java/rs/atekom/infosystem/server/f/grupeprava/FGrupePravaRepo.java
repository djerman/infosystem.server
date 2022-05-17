package rs.atekom.infosystem.server.f.grupeprava;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.prava.APrava;
import rs.atekom.infosystem.baza.e.grupaprava.EGrupaPrava;
import rs.atekom.infosystem.baza.f.grupeprava.FGrupePrava;

@Repository
public interface FGrupePravaRepo extends PagingAndSortingRepository<FGrupePrava, Long>{

	public List<FGrupePrava> findByGrupaPrava(EGrupaPrava grupa);
	
	public FGrupePrava findTopByGrupaPravaAndPravo(EGrupaPrava grupa, APrava pravo);
	
	}
