package rs.atekom.infosystem.server.e.konto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.db.kontoracun.DBKontoRacun;
import rs.atekom.infosystem.baza.e.konto.EKonto;

@Repository
public interface EKontoRepo extends PagingAndSortingRepository<EKonto, Long>{

	public List<EKonto> findByIzbrisanFalseOrderBySifraAsc();
	
	public List<EKonto> findByPretplatnikAndIzbrisanFalseOrderBySifraAsc(DPretplatnik pretplatnik);
	
	public List<EKonto> findByPretplatnikOrPretplatnikIsNullAndIzbrisanFalseOrderBySifraAsc(DPretplatnik pretplatnik);
	
	public List<EKonto> findByPodgrupaAndIzbrisanFalseOrderBySifraAsc(DBKontoRacun podgrupa);
	
	@Query("SELECT k FROM EKonto AS k"
			+ " WHERE k.izbrisan = '0'"
			+ " AND (:pretplatnikId IS NULL OR k.pretplatnik.id = :pretplatnikId)"
			+ " AND (:podgrupaId IS NULL OR k.podgrupa.id = :podgrupaId)"
			+ " AND (:pretraga IS NULL OR "
			+ "((lower(k.sifra) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(k.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(k.sr) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(k.en) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(k.de) LIKE lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ ")"
			+ " ORDER BY k.sifra ASC")
	public List<EKonto> pretraga(@Param("pretraga") String pretraga, @Param("podgrupaId") Long podgrupaId, @Param("pretplatnikId") Long pretplatnikId);
	
	}
