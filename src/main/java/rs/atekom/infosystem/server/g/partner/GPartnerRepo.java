package rs.atekom.infosystem.server.g.partner;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.atekom.infosystem.baza.g.GPartner;

@Repository(value = "partnerRepo")
public interface GPartnerRepo extends PagingAndSortingRepository<GPartner, Long>{

	//public List<GPartner> findByPretplatnikAndTipAndIzbrisanFalse(DPretplatnik pretplatnik, Integer tip);
	
	//public List<GPartner> findByPretplatnikAndTipAndKupacTrueAndIzbrisanFalse(DPretplatnik pretplatnik, Integer tip);
	
	//public List<GPartner> findByPretplatnikAndTipAndDobavljacTrueAndIzbrisanFalse(DPretplatnik pretplatnik, Integer tip);
	
	@Query("SELECT p FROM GPartner AS p"
			+ " WHERE p.izbrisan = '0'"
			+ " AND p.pretplatnik.id = :pretplatnikId"
			+ " AND p.preduzece IS NOT NULL"
			+ " AND (:pretraga IS NULL OR (lower(p.sifra) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.pib) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.mb) LIKE lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY p.id DESC")
	public List<GPartner> pretragaPreduzeca(@Param("pretraga") String pretraga, @Param("pretplatnikId") Long pretplatnikId);
	
	@Query("SELECT p FROM GPartner AS p"
			+ " WHERE p.izbrisan = '0'"
			+ " AND p.pretplatnik.id = :pretplatnikId"
			+ " AND p.preduzece IS NOT NULL"
			+ " AND kupac = '1'"
			+ " AND (:pretraga IS NULL OR (lower(p.sifra) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.pib) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.mb) LIKE lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY p.id DESC")
	public List<GPartner> pretragaPreduzecaKupaca(@Param("pretraga") String pretraga, @Param("pretplatnikId") Long pretplatnikId);
	
	@Query("SELECT p FROM GPartner AS p"
			+ " WHERE p.izbrisan = '0'"
			+ " AND p.pretplatnik.id = :pretplatnikId"
			+ " AND p.preduzece IS NOT NULL"
			+ " AND dobavljac = '1'"
			+ " AND (:pretraga IS NULL OR (lower(p.sifra) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.naziv) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.pib) LIKE lower(concat('%',:pretraga,'%')))"
			+ " OR (lower(p.preduzece.mb) LIKE lower(concat('%',:pretraga,'%')))"
			+ ")"
			+ " ORDER BY p.id DESC")
	public List<GPartner> pretragaPreduzecaDobavljaca(@Param("pretraga") String pretraga, @Param("pretplatnikId") Long pretplatnikId);
	
	@Query("SELECT p FROM GPartner AS p"
			+ " WHERE p.izbrisan = '0'"
			+ " AND p.pretplatnik.id = :pretplatnikId"
			+ " AND p.preduzece IS NOT NULL"
			+ " AND ((lower(p.preduzece.pib) LIKE lower(:pib))"
			+ " OR (lower(p.preduzece.mb) LIKE lower(:mb)))")
	public List<GPartner> proveraPreduzeca(@Param("pretplatnikId") Long pretplatnikId, @Param("pib") String pib, @Param("mb") String mb);
	
	}
