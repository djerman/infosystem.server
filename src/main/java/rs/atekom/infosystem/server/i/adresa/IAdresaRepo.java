package rs.atekom.infosystem.server.i.adresa;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import rs.atekom.infosystem.baza.g.GPartner;
import rs.atekom.infosystem.baza.i.IAdresa;

public interface IAdresaRepo extends PagingAndSortingRepository<IAdresa, Long>{

	List<IAdresa> findByPartner(GPartner partner);

	}
