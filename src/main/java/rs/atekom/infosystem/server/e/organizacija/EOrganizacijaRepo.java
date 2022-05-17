package rs.atekom.infosystem.server.e.organizacija;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.e.organizacija.EOrganizacija;

@Repository
public interface EOrganizacijaRepo extends PagingAndSortingRepository<EOrganizacija, Long>{

	public List<EOrganizacija> findByPretplatnik(DPretplatnik pretplatnik);
	
	public EOrganizacija findTopByPretplatnikAndSedisteTrue(DPretplatnik pretplatnik);
	
	}
