package rs.atekom.infosystem.server.a.tipbrojaca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.a.tipbrojaca.ATipBrojaca;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class ATipBrojacaService extends OsnovniService{

	@Autowired
	private ATipBrojacaRepo repo;
	
	public List<ATipBrojaca> lista(){
		return repo.findAll();
	}
	
	public ATipBrojaca vratiTipBrojaca(Long id) {
		return repo.getById(id);
	}
	
	public ATipBrojaca vratiPoTipu(Integer tip) {
		return repo.findByTip(tip);
	}
}
