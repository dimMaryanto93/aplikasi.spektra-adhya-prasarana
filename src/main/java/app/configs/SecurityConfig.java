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
	private HomeController ui;

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

	public void enabledMenuHome(Boolean disable) {
		ui.setMnuBarAplikasi(disable);

		ui.setMnuBarMaster(disable);
		ui.setMnuMasterKaryawan(disable);
		ui.setMnuMasterJabatan(disable);

		ui.setMnuBarKepegawaian(disable);
		ui.setMniKepegAbsensi(disable);

		ui.setMnuBarKepegKasbon(disable);
		ui.setMniKepegKasbonPengajuan(disable);
		ui.setMniKepegKasbonPersetujuan(disable);
		ui.setMniKepegKasbonPencairan(disable);
		ui.setMniKepegKasbonPembayaran(disable);

		ui.setMnuBarKepegAngsuran(disable);
		ui.setMniKepegAngsuranPengajuan(disable);
		ui.setMniKepegAngsuranPersetujuan(disable);

		ui.setMniKepegPenggajian(disable);

		ui.setMnuBarLaporan(disable);
		ui.setMniLaporanAbsensi(disable);
		ui.setMniLaporanKasbon(disable);
		ui.setMniLaporanAngsuran(disable);
		ui.setMniLaporanPenggajian(disable);

		ui.setMnuBarKeamanan(disable);

		ui.setMnuKeamananUser(disable);

		ui.setMniButtonHome(disable);
		ui.setMniButtonLogin(disable);
		ui.setMniButtonJabatan(disable);
		ui.setMniButtonKaryawan(disable);
		ui.setMniButtonAbsensi(disable);

		ui.setMnuButtonKasbon(disable);
		ui.setMniButtonKasbonPengajuan(disable);
		ui.setMniButtonKasbonPersetujuan(disable);
		ui.setMniButtonKasbonPencairan(disable);
		ui.setMniButtonKasbonPembayaran(disable);

		ui.setMnuButtonAnggsuran(disable);
		ui.setMniButtonAngsuranPengajuan(disable);
		ui.setMniButtonAngsuranPersetujuan(disable);

		ui.setMniButtonPenggajian(disable);

		ui.setMniButtonLogout(disable);
		ui.setMniBarKeamananProfile(disable);
		ui.setMniBarKeamananLogout(disable);
		ui.setMnuKeamananNotifikasi(disable);
	}

	public void isHRD(Boolean disable) {
		enabledMenuHome(true);
		ui.setMnuBarAplikasi(disable);

		ui.setMnuBarMaster(disable);
		ui.setMnuMasterKaryawan(disable);
		ui.setMnuMasterJabatan(disable);

		ui.setMnuBarKepegawaian(disable);
		ui.setMniKepegAbsensi(disable);

		ui.setMniButtonJabatan(disable);
		ui.setMniButtonKaryawan(disable);
		ui.setMniButtonAbsensi(disable);
	}

	public void isKeuangan(Boolean disable) {
		enabledMenuHome(true);
		ui.setMnuBarAplikasi(disable);
		ui.setMnuBarKepegawaian(disable);

		ui.setMnuButtonKasbon(disable);
		ui.setMniButtonKasbonPengajuan(disable);
		ui.setMniButtonKasbonPencairan(disable);
		ui.setMniButtonKasbonPembayaran(disable);

		ui.setMnuButtonAnggsuran(disable);
		ui.setMniButtonAngsuranPengajuan(disable);

		ui.setMniButtonPenggajian(disable);

		ui.setMnuBarKepegKasbon(disable);
		ui.setMniKepegKasbonPengajuan(disable);
		ui.setMniKepegKasbonPencairan(disable);
		ui.setMniKepegKasbonPembayaran(disable);

		ui.setMnuBarKepegAngsuran(disable);
		ui.setMniKepegAngsuranPengajuan(disable);

		ui.setMniKepegPenggajian(disable);

		ui.setMnuBarLaporan(disable);
		ui.setMniLaporanKasbon(disable);
		ui.setMniLaporanAngsuran(disable);
		ui.setMniLaporanPenggajian(disable);
	}

	public void isDirektur(Boolean disable) {
		enabledMenuHome(true);
		ui.setMnuBarAplikasi(disable);

		ui.setMnuBarKepegKasbon(disable);
		ui.setMniKepegKasbonPersetujuan(disable);

		ui.setMnuBarKepegAngsuran(disable);
		ui.setMniKepegAngsuranPersetujuan(disable);

		ui.setMnuBarLaporan(disable);
		ui.setMniLaporanAbsensi(disable);
		ui.setMniLaporanKasbon(disable);
		ui.setMniLaporanAngsuran(disable);
		ui.setMniLaporanPenggajian(disable);

		ui.setMnuButtonKasbon(disable);
		ui.setMniButtonKasbonPersetujuan(disable);

		ui.setMnuButtonAnggsuran(disable);
		ui.setMniButtonAngsuranPersetujuan(disable);

	}

}
