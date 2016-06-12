package app.configs;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.controller.HomeController;
import app.entities.master.DataAkun;
import app.entities.master.DataJenisAkun;
import app.repositories.RepositoryAkun;

@Component
public class SecurityConfig {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RepositoryAkun akunRepo;
	
	@Autowired
	private HomeController homeController;

	DataAkun akun;

	@PostConstruct
	public void initAdmin() {
		if (akunRepo.findBySecurity(DataJenisAkun.ADMINISTRATOR) == null) {
			akun = new DataAkun();
			akun.setUsername("admin");
			akun.setPassword("admin");
			akun.setFullname("Administrator");
			akun.setEnabled(true);
			akun.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			akun.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			akun.setSecurity(DataJenisAkun.ADMINISTRATOR);
			akunRepo.save(akun);
			logger.info("Inisialisasi akun {} dengan password {}", akun.getSecurity(), akun.getPassword());
		}

		if (akunRepo.findBySecurity(DataJenisAkun.KEUANGAN) == null) {
			akun = new DataAkun();
			akun.setUsername("keuangan");
			akun.setPassword("keuangan");
			akun.setFullname("Keuangan");
			akun.setEnabled(true);
			akun.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			akun.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			akun.setSecurity(DataJenisAkun.KEUANGAN);
			akunRepo.save(akun);
			logger.info("Inisialisasi akun {} dengan password {}", akun.getSecurity(), akun.getPassword());
		}
		
		if (akunRepo.findBySecurity(DataJenisAkun.HRD) == null) {
			akun = new DataAkun();
			akun.setUsername("hrd");
			akun.setPassword("hrd");
			akun.setFullname("HRD");
			akun.setEnabled(true);
			akun.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			akun.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			akun.setSecurity(DataJenisAkun.HRD);
			akunRepo.save(akun);
			logger.info("Inisialisasi akun {} dengan password {}", akun.getSecurity(), akun.getPassword());
		}
		
		if (akunRepo.findBySecurity(DataJenisAkun.DIREKTUR) == null) {
			akun = new DataAkun();
			akun.setUsername("direktur");
			akun.setPassword("direktur");
			akun.setFullname("Direktur");
			akun.setEnabled(true);
			akun.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			akun.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			akun.setSecurity(DataJenisAkun.DIREKTUR);
			akunRepo.save(akun);
			logger.info("Inisialisasi akun {} dengan password {}", akun.getSecurity(), akun.getPassword());
		}
	}

}
