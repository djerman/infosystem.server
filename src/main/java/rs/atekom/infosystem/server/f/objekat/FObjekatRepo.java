package rs.atekom.infosystem.server.f.objekat;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.e.organizacija.EOrganizacija;
import rs.atekom.infosystem.baza.f.objekat.FObjekat;

@Repository
public interface FObjekatRepo extends PagingAndSortingRepository<FObjekat, Long>{

	public List<FObjekat> findByPretplatnikAndIzbrisanFalse(DPretplatnik pretplatnik);
	
	public List<FObjekat> findByOrganizacijaAndIzbrisanFalse(EOrganizacija organizacija);
	
	@Query(value = "SELECT o from FObjekat AS o"
			+ " WHERE o.izbrisan = 0"
			+ " AND o.pretplatnik.id = :pretplatnikId"
			+ " AND (:pretraga IS NULL"
			+ " OR lower(o.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ " ORDER BY o.naziv ASC")
	public List<FObjekat> pretragaObjekata(@Param("pretraga") String pretraga, @Param("pretplatnikId") Long pretplatnikId);
	
	public List<FObjekat> findByPretplatnikAndIzbrisanFalseAndNaziv(DPretplatnik pretplatnik, String naziv);
	
}
