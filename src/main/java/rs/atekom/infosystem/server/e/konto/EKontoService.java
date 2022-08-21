package rs.atekom.infosystem.server.e.konto;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.atekom.infosystem.baza.e.konto.EKontoOdgovor;
import rs.atekom.infosystem.server.OsnovniService;

@Service
public class EKontoService extends OsnovniService{

	@Autowired
	private EKontoRepo repo;
	
	public EKontoOdgovor lista() {
		return new EKontoOdgovor(repo.findByIzbrisanFalseOrderBySifraAsc());
		}
	
	public EKontoOdgovor pretraga(Optional<String> pretraga, Optional<Long> podgrupa, Optional<Long> pretplatnik) {
		String pojam = null;
		if(pretraga != null && pretraga.isPresent()) {
			pojam= pretraga.get();
			}
		Long podgrupaId = null;
		if(podgrupa != null && podgrupa.isPresent()) {
			podgrupaId = podgrupa.get();
			}
		Long pretplatnikId = null;
		if(pretplatnik != null && pretplatnik.isPresent()) {
			pretplatnikId = pretplatnik.get();
			}
		return new EKontoOdgovor(repo.pretraga(pojam, podgrupaId, pretplatnikId));
		}
	
	}
