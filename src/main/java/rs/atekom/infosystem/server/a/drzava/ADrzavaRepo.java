package rs.atekom.infosystem.server.a.drzava;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.drzava.ADrzava;

@Repository
public interface ADrzavaRepo extends PagingAndSortingRepository<ADrzava, Long> {
	
	@Query(value = "SELECT d FROM ADrzava AS d"
			+ " WHERE d.izbrisan='0'"
			+ " AND (:pretraga IS NULL OR (d.naziv IS NULL OR lower(d.naziv) like lower(concat('%',:pretraga,'%')))"
			+ " OR (d.sr IS NULL OR lower(d.sr) like lower(concat('%',:pretraga,'%')))"
			+ " OR (d.en IS NULL OR lower(d.en) like lower(concat('%',:pretraga,'%')))"
			+ " OR (d.pozivniBroj IS NULL OR lower(d.pozivniBroj) like lower(concat('%',:pretraga,'%')))"
			+ " OR (d.oznaka IS NULL OR lower(d.oznaka) like lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY d.podrazumevan DESC, d.naziv ASC")
	public List<ADrzava> pretraga(@Param("pretraga") String pretraga);
	
	public ADrzava findTopByPodrazumevanTrue();
	
	}
