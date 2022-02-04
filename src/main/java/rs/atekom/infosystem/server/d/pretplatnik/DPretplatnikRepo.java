package rs.atekom.infosystem.server.d.pretplatnik;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.agencija.AAgencija;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;

@Repository
public interface DPretplatnikRepo extends PagingAndSortingRepository<DPretplatnik, Long>{

	@Query("SELECT p FROM DPretplatnik AS p"
			+ " WHERE p.izbrisan='0'"
			+ " AND (:agencija IS NULL OR p.agencija=:agencija)"
			+ " AND (:pretraga IS NULL OR (lower(p.naziv) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(p.punNaziv) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(p.mb) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(p.pib) like lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY p.naziv ASC")
	public List<DPretplatnik> pretraga(@Param("pretraga") String pretraga, @Param("agencija") AAgencija agencija);
	
	@Query("SELECT p FROM DPretplatnik AS p"
			+ " WHERE :pretraga IS NULL OR (lower(p.naziv) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(p.punNaziv) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(p.mb) like lower(concat('%',:pretraga,'%'))"
			+ " OR lower(p.pib) like lower(concat('%',:pretraga,'%')))"
			+ " ORDER BY p.naziv ASC")
	public List<DPretplatnik> pretragaSvih(@Param("pretraga") String pretraga);
	
	public DPretplatnik findByIdAndAgencijaId(Long id, Long agencija);
	
	}
