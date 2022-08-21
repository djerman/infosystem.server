package rs.atekom.infosystem.server.a.prava;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.prava.APrava;

@Repository(value = "pravaRepo")
public interface APravaRepo extends PagingAndSortingRepository<APrava, Long>{

	public List<APrava> findByIzbrisanFalse();
	
	//pretraga mora biti uskđena sa 'tip' - integer odnosno nivoom prava koje ima onaj koji pretražuje
	@Query("SELECT a FROM APrava AS a"
			+ " WHERE a.izbrisan='0'"
			+ " AND (:pretraga IS NULL OR (a.naziv IS NULL OR lower(a.naziv) like lower(concat('%',:pretraga,'%')))"
			+ " OR (a.putanja IS NULL OR lower(a.putanja) like lover(concat('%',:pretraga,'%')))"
			+ " OR (a.sr IS NULL OR lower(a.sr) like lover(concat('%',:pretraga,'%')))"
			+ " OR (a.en IS NULL OR lower(a.en) like lover(concat('%',:pretraga,'%')))"
			+ " OR (a.de IS NULL OR lower(a.de) like lover(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY a.naziv ASC")
	public List<APrava> pretraga(@Param("pretraga") String pretraga);
	
	public APrava findByPutanjaAndIzbrisanFalse(String putanja);
	
	public List<APrava> findByTipLessThanEqual(Integer tip);
	
	}
