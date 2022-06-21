package rs.atekom.infosystem.server.f.brojac;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.tipbrojaca.ATipBrojaca;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.f.brojac.FBrojac;

@Repository
public interface FBrojacRepo extends JpaRepository<FBrojac, Long>{

	public List<FBrojac> findByPretplatnik(DPretplatnik pretplatnik);
	
	public FBrojac findByPretplatnikAndTip(DPretplatnik pretplatnik, ATipBrojaca tip);
	
}
