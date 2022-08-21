package rs.atekom.infosystem.server.f.grupapartnera;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.f.grupapartnera.FGrupaPartnera;

@Repository
public interface FGrupaPartneraRepo extends PagingAndSortingRepository<FGrupaPartnera, Long>{

	public List<FGrupaPartnera> findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(DPretplatnik pretplatnik);
	
	public List<FGrupaPartnera> findByPretplatnikAndIzbrisanFalseAndSifraOrNaziv(DPretplatnik pretplatnik, String sifra, String naziv);
	
	}
