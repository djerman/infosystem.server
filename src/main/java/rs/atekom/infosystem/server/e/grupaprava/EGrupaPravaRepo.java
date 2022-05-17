package rs.atekom.infosystem.server.e.grupaprava;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.e.grupaprava.EGrupaPrava;

@Repository("grupaPravaRepo")
public interface EGrupaPravaRepo extends PagingAndSortingRepository<EGrupaPrava, Long>{

	public List<EGrupaPrava> findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(DPretplatnik pretplatnik);
	
	public List<EGrupaPrava> findByPretplatnikAndNazivAndIzbrisanFalse(DPretplatnik pretplatnik, String naziv);
	
	public List<EGrupaPrava> findByIzbrisanFalseOrderByNazivAsc();
	
	public List<EGrupaPrava> findByNazivAndIzbrisanFalse(String naziv);
	
	}
