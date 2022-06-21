package rs.atekom.infosystem.server.j.artikal;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.j.JArtikal;

@Repository("artikalRepo")
public interface JArtikalRepo extends PagingAndSortingRepository<JArtikal, Long>{

	public Page<JArtikal> findByPretplatnikAndIzbrisanFalseOrderBySifraAsc(DPretplatnik pretplatnik, Pageable pageable);
	
	public Page<JArtikal> findByPretplatnikAndIzbrisanFalseOrderByIdDesc(DPretplatnik pretplatnik, Pageable pageable);
	
	public List<JArtikal> findByPretplatnikAndSifraAndIzbrisanFalse(DPretplatnik pretplatnik, String sifra);
	
	@Query(value = "SELECT a from JArtikal AS a"
			+ " WHERE a.pretplatnik.id = :pretplatnikId AND a.izbrisan = 0"
			+ " AND (:pretraga IS NULL"
			+ " OR (lower(a.sifra) LIKE lower(concat('%',:pretraga,'%'))"
			+ " OR lower(a.naziv) LIKE lower(concat('%',:pretraga,'%'))"
			+ " OR lower(a.barcode) LIKE lower(concat('%',:pretraga,'%'))"
			+ " OR lower(a.opis) like lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY a.sifra ASC")
	public Page<JArtikal> pretragaArtikalaPretplatnika(@Param("pretplatnikId") Long pretplatnikId, @Param("pretraga") String pretraga, Pageable pageable);
	
}
