package rs.atekom.infosystem.server.da.kontogrupa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.kontoklasa.DKontoKlasa;
import rs.atekom.infosystem.baza.da.kontogrupa.DAKontoGrupa;

@Repository
public interface DAKontoGrupaRepo extends PagingAndSortingRepository<DAKontoGrupa, Long>{

	public List<DAKontoGrupa> findByKlasaAndIzbrisanFalseOrderBySifraAsc(DKontoKlasa klasa);
	
	public List<DAKontoGrupa> findAllByOrderBySifraAsc();
	
	@Query("SELECT g FROM DAKontoGrupa AS g"
			+ " WHERE g.izbrisan = '0'"
			+ " AND (:klasaId IS NULL OR g.klasa.id = :klasaId)"
			+ " AND (:pretraga IS NULL OR ((lower(g.sifra) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(g.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(g.sr) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(g.en) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(g.de) LIKE lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ ")"
			+ " ORDER BY g.sifra ASC")
	public List<DAKontoGrupa> pretraga(@Param("pretraga") String pretraga, @Param("klasaId") Long klasaId);
	}
