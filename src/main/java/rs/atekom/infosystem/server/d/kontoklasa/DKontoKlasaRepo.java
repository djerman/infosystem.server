package rs.atekom.infosystem.server.d.kontoklasa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.kontoklasa.DKontoKlasa;

@Repository
public interface DKontoKlasaRepo extends PagingAndSortingRepository<DKontoKlasa, Long>{

	public List<DKontoKlasa> findAllByIzbrisanFalseOrderBySifraAsc();
	
	@Query("SELECT k FROM DKontoKlasa AS k"
			+ " WHERE k.izbrisan='0'"
			+ " AND (:pretraga IS NULL OR (lower(k.naziv) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(k.sifra) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(k.sr) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(k.en) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(k.de) like lower(concat('%',:pretraga,'%'))))"
			+ " ORDER BY k.sifra ASC")
	public List<DKontoKlasa> pretraga(@Param("pretraga") String pretraga);
	
	}
