package rs.atekom.infosystem.server.e.tipdokumenta;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.e.tipdokumenta.ETipDokumentaOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class ETipDokumentaService extends OsnovniService{

	@Autowired
	ETipDokumentaRepo repo;
	
	public ETipDokumentaOdgovor lista() {
		return new ETipDokumentaOdgovor(repo.findByIzbrisanFalseOrderBySifraAsc());
		}
	
	public ETipDokumentaOdgovor pretraga(Optional<String> pretraga, Optional<Long> pretplatnik) {
		String pojam = null;
		if(pretraga != null && pretraga.isPresent()) {
			pojam= pretraga.get();
			}
		Long pretplatnikId = null;
		if(pretplatnik != null && pretplatnik.isPresent()) {
			pretplatnikId = pretplatnik.get();
			}
		return new ETipDokumentaOdgovor(repo.pretraga(pojam, pretplatnikId));
		}
	
	}
