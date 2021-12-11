package rs.atekom.infosystem.server.a.poreskatarifa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.atekom.infosystem.baza.a.poreskatarifa.APoreskaTarifa;

@Repository
public interface APoreskaTarifaRepo extends JpaRepository<APoreskaTarifa, Long> {

	public List<APoreskaTarifa> findAllByOrderBySifraAsc();
	
	}
