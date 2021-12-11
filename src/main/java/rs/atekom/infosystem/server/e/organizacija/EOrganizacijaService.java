package rs.atekom.infosystem.server.e.organizacija;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.e.EOrganizacija;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.d.pretplatnik.DPretplatnikRepo;

@Service
public class EOrganizacijaService extends OsnovniService{

	@Autowired
	EOrganizacijaRepo repo;
	@Autowired
	DPretplatnikRepo repoPretplatnik;
	
	public List<EOrganizacija> poPretplatniku(Long pretplathnikId){
		return repo.findByPretplatnik(repoPretplatnik.findById(pretplathnikId).get());
		}
	
	}
