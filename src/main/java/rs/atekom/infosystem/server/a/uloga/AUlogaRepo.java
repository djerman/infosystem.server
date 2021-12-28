package rs.atekom.infosystem.server.a.uloga;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.uloga.AUloga;

@Repository
public interface AUlogaRepo extends PagingAndSortingRepository<AUloga, Long>{

	public AUloga findByIdAndIzbrisanFalse(Long id);
	
	}
