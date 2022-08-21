package rs.atekom.infosystem.server.i.adresa;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.e.organizacija.EOrganizacija;
import rs.atekom.infosystem.baza.g.GPartner;
import rs.atekom.infosystem.baza.i.IAdresa;

@Repository
public interface IAdresaRepo extends PagingAndSortingRepository<IAdresa, Long>{

	public List<IAdresa> findByPartnerAndIzbrisanFalse(GPartner partner);
	
	public IAdresa findTopByPartnerAndSedisteTrueAndIzbrisanFalse(GPartner partner);

	public List<IAdresa> findByPartnerAndIzbrisanFalseAndSedisteFalse(GPartner partner);
	
	public IAdresa findTopByOrganizacijaAndIzbrisanFalseAndSedisteTrue(EOrganizacija organizacija);
	
	}
