package rs.atekom.infosystem.server.b.opstina;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.drzava.ADrzava;
import rs.atekom.infosystem.baza.b.BOpstina;

@Repository
public interface BOpstinaRepo extends PagingAndSortingRepository<BOpstina, Long>{

	public List<BOpstina> findByDrzavaAndIzbrisanFalseOrderByNazivAsc(ADrzava drzava);
	
	@Query(value = "SELECT o FROM BOpstina AS o"
			+ " WHERE o.izbrisan='0'"
			+ " AND (:pretraga IS NULL OR (o.naziv IS NULL OR lower(o.naziv) like lower(concat('%',:pretraga,'%')))"
			+ " OR (o.sr IS NULL OR lower(o.sr) like lower(concat('%',:pretraga,'%')))"
			+ " OR (o.en IS NULL OR lower(o.en) like lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY o.naziv ASC")
	public List<BOpstina> pretraga(@Param("pretraga") String pretraga);
	}
