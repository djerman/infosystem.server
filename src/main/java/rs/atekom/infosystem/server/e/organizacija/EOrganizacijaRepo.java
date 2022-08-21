package rs.atekom.infosystem.server.e.organizacija;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.e.organizacija.EOrganizacija;

@Repository
public interface EOrganizacijaRepo extends PagingAndSortingRepository<EOrganizacija, Long>{

	public List<EOrganizacija> findByPretplatnikAndIzbrisanFalse(DPretplatnik pretplatnik);
	
	public EOrganizacija findTopByPretplatnikAndSedisteTrueAndIzbrisanFalse(DPretplatnik pretplatnik);
	
	@Query(value = "SELECT o from EOrganizacija AS o"
			+ " WHERE o.izbrisan = 0"
			+ " AND o.pretplatnik.id = :pretplatnikId"
			+ " AND (:pretraga IS NULL"
			+ " OR (lower(o.sifra) LIKE lower(concat('%',:pretraga,'%'))"
			+ " OR lower(o.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY o.sifra ASC")
	public List<EOrganizacija> pretragaOrganizacija(@Param("pretraga") String pretraga, @Param("pretplatnikId") Long pretplatnikId);
	
	public List<EOrganizacija> findByPretplatnikAndIzbrisanFalseAndSifraOrNaziv(DPretplatnik pretplatnik, String sifra, String naziv);
	
	}
