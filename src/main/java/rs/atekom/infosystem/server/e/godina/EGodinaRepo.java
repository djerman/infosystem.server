package rs.atekom.infosystem.server.e.godina;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import rs.atekom.infosystem.baza.d.DPretplatnik;
import rs.atekom.infosystem.baza.e.godina.EGodina;

public interface EGodinaRepo extends PagingAndSortingRepository<EGodina, Long>{

	public EGodina findTop1ByPretplatnikAndAktivanTrueOrderByGodinaDesc(DPretplatnik pretplatnik);
	
	public List<EGodina> findByPretplatnikOrderByGodinaDesc(DPretplatnik pretplatnik);
	
	public List<EGodina> findByPretplatnikAndGodinaAndAktivanTrue(DPretplatnik pretplatnik, Integer godina);
	
	public List<EGodina> findAllByOrderByGodinaDesc();
	
	}
