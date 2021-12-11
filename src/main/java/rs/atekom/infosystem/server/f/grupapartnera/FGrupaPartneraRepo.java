package rs.atekom.infosystem.server.f.grupapartnera;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.f.FGrupaPartnera;

@Repository
public interface FGrupaPartneraRepo extends JpaRepository<FGrupaPartnera, Long>{

	public List<FGrupaPartnera> findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(DPretplatnik pretplatnik);
	
	}
