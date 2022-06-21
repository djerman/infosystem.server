package rs.atekom.infosystem.server.a.tipbrojaca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.atekom.infosystem.baza.a.tipbrojaca.ATipBrojaca;

@Repository
public interface ATipBrojacaRepo extends JpaRepository<ATipBrojaca, Long>{

	public ATipBrojaca findByTip(Integer tip);
	
}
