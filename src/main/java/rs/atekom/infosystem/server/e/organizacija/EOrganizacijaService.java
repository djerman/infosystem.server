package rs.atekom.infosystem.server.e.organizacija;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.server.OsnovniService;

@Service
public class EOrganizacijaService extends OsnovniService{

	@Autowired
	EOrganizacijaRepo repo;
	
	}
