package rs.atekom.infosystem.server.a.agencija;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.agencija.AAgencija;

@Repository
public interface AAgencijaRepo extends PagingAndSortingRepository<AAgencija, Long>{

	@Query("SELECT a FROM AAgencija AS a"
			+ " WHERE a.izbrisan='0'"
			+ " AND (:pretraga IS NULL OR (a.naziv IS NULL OR lower(a.naziv) like lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY a.naziv ASC")
	public List<AAgencija> pretraga(@Param("pretraga") String pretraga);
	
	}
