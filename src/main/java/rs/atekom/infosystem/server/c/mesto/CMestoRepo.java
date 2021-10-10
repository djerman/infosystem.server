package rs.atekom.infosystem.server.c.mesto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.drzava.ADrzava;
import rs.atekom.infosystem.baza.b.BOpstina;
import rs.atekom.infosystem.baza.c.CMesto;

@Repository
public interface CMestoRepo extends PagingAndSortingRepository<CMesto, Long> {

	public List<CMesto> findByOpstinaAndIzbrisanFalseOrderByNazivAsc(BOpstina opstina);
	
	public List<CMesto> findByDrzavaAndIzbrisanFalseOrderByNazivAsc(ADrzava drzava);
	
	@Query(value = "SELECT m FROM CMesto AS m"
			+ " WHERE m.izbrisan='0'"
			+ " AND (:pretraga IS NULL OR (m.naziv IS NULL OR lower(m.naziv) like lower(concat('%',pretraga,'%')))"
			+ " OR (m.sr IS NULL OR lower(m.sr) like lower(concat('%',:pretraga,'%')))"
			+ " OR (m.en IS NULL OR lower(m.en) like lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY m.naziv ASC")
	public List<CMesto> pretraga(@Param("pretraga") String pretraga);
	}
