package rs.atekom.infosystem.server.db.kontoracun;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.da.kontogrupa.DAKontoGrupa;
import rs.atekom.infosystem.baza.db.kontoracun.DBKontoRacun;

@Repository
public interface DBKontoRacunRepo extends PagingAndSortingRepository<DBKontoRacun, Long>{

	public List<DBKontoRacun> findByGrupaAndIzbrisanFalseOrderBySifraAsc(DAKontoGrupa grupa);
	
	public List<DBKontoRacun> findAllByOrderBySifraAsc();
	
	@Query("SELECT r FROM DBKontoRacun AS r"
			+ " WHERE r.izbrisan = '0'"
			+ " AND (:grupaId IS NULL OR r.grupa.id = :grupaId)"
			+ " AND (:pretraga IS NULL OR ((lower(r.sifra) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(r.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(r.sr) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(r.en) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(r.de) LIKE lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ ")"
			+ " ORDER BY r.sifra ASC")
	public List<DBKontoRacun> pretraga(@Param("pretraga") String pretraga, @Param("grupaId") Long grupaId);
	
	@Query("SELECT r FROM DBKontoRacun AS r"
			+ " WHERE r.izbrisan = '0'"
			+ " AND (:grupaId IS NULL OR r.grupa.id = :grupaId)"
			+ " AND r.sifra LIKE :sifra"
			+ " ORDER BY r.sifra ASC")
	public List<DBKontoRacun> pretragaPoSifri(@Param("sifra") String sifra, @Param("grupaId") Long grupaId);
	
	
	}
