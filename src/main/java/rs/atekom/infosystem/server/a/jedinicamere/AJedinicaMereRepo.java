package rs.atekom.infosystem.server.a.jedinicamere;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.atekom.infosystem.baza.a.jedinicamere.AJedinicaMere;

@Repository
public interface AJedinicaMereRepo extends JpaRepository<AJedinicaMere, Long>{

	public List<AJedinicaMere> findByIzbrisanFalse();
	
	@Query(value = "SELECT j FROM AJedinicaMere AS j "
			+ " WHERE j.izbrisan='0'"
			+ " AND (j.naziv=:naziv OR j.oznaka=:oznaka)")
	public List<AJedinicaMere> pretraga(@Param("naziv") String naziv, @Param("oznaka") String oznaka);
	
	}
