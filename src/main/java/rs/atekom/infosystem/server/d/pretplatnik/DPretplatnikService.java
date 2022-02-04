package rs.atekom.infosystem.server.d.pretplatnik;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import rs.atekom.infosystem.baza.a.agencija.AAgencija;
import rs.atekom.infosystem.baza.d.pretplatnik.DPodaciZaPretplatnikaOdgovor;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnik;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnikOdgovor;
import rs.atekom.infosystem.baza.d.pretplatnik.DPretplatnikPodaciOdgovor;
import rs.atekom.infosystem.baza.h.HKontakt;
import rs.atekom.infosystem.server.OsnovniService;
import rs.atekom.infosystem.server.a.agencija.AAgencijaRepo;
import rs.atekom.infosystem.server.a.drzava.ADrzavaRepo;
import rs.atekom.infosystem.server.c.mesto.CMestoRepo;
import rs.atekom.infosystem.server.e.organizacija.EOrganizacijaRepo;
import rs.atekom.infosystem.server.h.HKontaktRepo;

@Service(value = "pretplatnikService")
public class DPretplatnikService extends OsnovniService{

	@Autowired
	DPretplatnikRepo repo;
	@Autowired
	AAgencijaRepo repoAgencija;
	@Autowired
	CMestoRepo repoMesta;
	@Autowired
	ADrzavaRepo repoDrzava;
	@Autowired
	EOrganizacijaRepo repoOrganizacija;
	@Autowired
	HKontaktRepo repoKontakt;
	
	//String RESOURCES_DIR = DPretplatnikService.class.getResource("/").getPath();
	//String RESOURCES_DIR = DPretplatnikService.class.getResource("/D/serverslike/").getPath();
	String RESOURCES_DIR = "\\D\\serverslike\\";
	
	public DPretplatnikOdgovor lista(Optional<String> pretraga, Optional<Long> agencijaId) {
		DPretplatnikOdgovor odgovor = new DPretplatnikOdgovor();
		try {
			String pojam = null;
			if(pretraga != null && pretraga.isPresent()) {
				pojam = pretraga.get();
				}
			Long id = null;
			AAgencija agencija = null;
			if(agencijaId != null && agencijaId.isPresent()) {
				id = agencijaId.get();
				agencija = repoAgencija.findById(id).get();
				}
			List<DPretplatnik> pretplatnici = repo.pretraga(pojam, agencija);
			List<DPretplatnikPodaciOdgovor> saPodacima = new ArrayList<DPretplatnikPodaciOdgovor>();
			for(DPretplatnik pr : pretplatnici) {
				DPretplatnikPodaciOdgovor podaci = new DPretplatnikPodaciOdgovor();
				postaviLogo(pr, pr.getLogo());
				podaci.setPretplatnik(pr);
				podaci.setOrganizacija(repoOrganizacija.findTopByPretplatnikAndSedisteTrue(pr));
				saPodacima.add(podaci);
				}
			odgovor.setListaSaPodacima(saPodacima);
			}catch (Exception e) {
				e.printStackTrace();
				}
		return odgovor;
		}
	
	public DPodaciZaPretplatnikaOdgovor podaciZaPretplatnika() {
		DPodaciZaPretplatnikaOdgovor odgovor = new DPodaciZaPretplatnikaOdgovor();
		odgovor.setAgencije(repoAgencija.pretraga(null));
		odgovor.setMesta(repoMesta.findByDrzavaAndIzbrisanFalseOrderByNazivAsc(repoDrzava.findTopByPodrazumevanTrue()));
		return odgovor;
		}
	
	public DPretplatnik sacuvajPretplatnika(DPretplatnik pretplatnik) {
		try {
			//System.out.println("logo " + pretplatnik.getSlika().length);
			if(pretplatnik.getSlika() != null) {
				pretplatnik.setLogo(sacuvajSliku(pretplatnik.getSlika(), pretplatnik.getSlikaIme()));
				}
			return repo.save(pretplatnik);
			}catch (Exception e) {
				e.printStackTrace();
				return repo.save(pretplatnik);
				}
		}
	
	public String sacuvajSliku(byte[] content, String imageName){
		/*URI uri = ClassLoader.getSystemResource("com/stackoverflow/json").toURI();
		String mainPath = Paths.get(uri).toString();
		Path path = Paths.get(mainPath ,"Movie.class");*/
		//URL url = getClass().getResource("/");
		//Path dest = Paths.get(url.toURI());
       
        //unix Scanner s = new Scanner(new File("/home/me/java/ex.txt"));
		if(content != null) {
			try {
				Path newFile = Paths.get(RESOURCES_DIR + new Date().getTime() + "-" + imageName);
		        Files.createDirectories(newFile.getParent());
		        String[] naziv = imageName.split("\\.");
		        System.out.println( "lokacija: " + Files.write(newFile, content) + " prazno " + (content == null));
		        ByteArrayInputStream bis = new ByteArrayInputStream(content);
		        BufferedImage bImage2 = ImageIO.read(bis);
		        ImageIO.write(bImage2, naziv[naziv.length - 1], new File(newFile.toString()));
		        return newFile.toString();
		        }catch (Exception e) {
					e.printStackTrace();
					return null;
					}
			}else {
				return null;
				}
        }
	
	public DPretplatnik postaviLogo(DPretplatnik pretplatnik,  String lokacija) {
		try {
			FileSystemResource file = new FileSystemResource(Paths.get(lokacija));
			pretplatnik.setSlikaIme(file.getFilename());
			pretplatnik.setSlika(file.getInputStream().readAllBytes());
			return pretplatnik;
			}catch (Exception e) {
				//e.printStackTrace();
				return pretplatnik;
				}
		}
	
	public boolean proveraZaAgenciju(String korisnicko, Long pretplatnikId) {
		boolean provera = false;
		HKontakt korisnik = repoKontakt.findByKorisnickoAndIzbrisanFalseAndAktivanTrueAndPristupTrue(korisnicko);
		if(korisnik != null) {
			AAgencija agencija = korisnik.getAgencija();
			if(agencija != null) {
				DPretplatnik pretplatnik = repo.findByIdAndAgencijaId(pretplatnikId, agencija.getId());
				if(pretplatnik != null) {
					provera = true;
				}
			}
		}
		
		return provera;
	}
	
	}
