package rs.atekom.infosystem.server.i.grupaartikala;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.i.grupaartikala.IGrupaArtikala;

@Repository
public interface IGrupaArtikalaRepo extends PagingAndSortingRepository<IGrupaArtikala, Long>{

	public List<IGrupaArtikala> findByPretplatnikAndIzbrisanFalseOrderByNazivAsc(DPretplatnik pretplatnik);
	
	public List<IGrupaArtikala> findByPretplatnikAndIzbrisanFalseAndSifraAndNaziv(DPretplatnik pretplatnik, String sifra, String naziv);
	
	}
