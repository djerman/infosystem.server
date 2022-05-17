package rs.atekom.infosystem.server.f.preduzece;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.f.preduzece.FPreduzece;


@Repository
public interface FPreduzeceRepo extends PagingAndSortingRepository<FPreduzece, Long>{

	
	}
