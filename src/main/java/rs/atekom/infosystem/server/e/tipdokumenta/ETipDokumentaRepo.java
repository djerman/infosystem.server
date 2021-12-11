package rs.atekom.infosystem.server.e.tipdokumenta;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.e.tipdokumenta.ETipDokumenta;

@Repository
public interface ETipDokumentaRepo extends PagingAndSortingRepository<ETipDokumenta, Long>{

	public List<ETipDokumenta> findByIzbrisanFalseOrderBySifraAsc();
	
	@Query("SELECT d FROM ETipDokumenta AS d"
			+ " WHERE d.izbrisan = '0'"
			+ " AND (:pretplatnikId IS NULL OR d.pretplatnik.id = : pretplatnikId)"
			+ " AND (:pretraga IS NULL OR"
			+ " ((lower(d.sifra) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(d.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(d.dokument) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(d.nalog) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(d.sr) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(d.en) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(d.de) LIKE lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ ")"
			+ " ORDER BY d.sifra ASC")
	public List<ETipDokumenta> pretraga(@Param("pretraga") String pretraga, @Param("pretplatnikId") Long pretplatnikId);
}
