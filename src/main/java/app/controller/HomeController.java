package app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.dialog.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.DialogsFX;
import app.controller.absensi.AbsensiFormController;
import app.controller.absensi.AbsensiListController;
import app.controller.jabatan.JabatanListController;
import app.controller.karyawan.KaryawanListController;
import app.controller.peminjaman.karyawan.KasbonKaryawanListController;
import app.controller.peminjaman.karyawan.KasbonKaryawanPembayaranController;
import app.controller.peminjaman.karyawan.KasbonKaryawanPencairanDanaController;
import app.controller.peminjaman.karyawan.KasbonKaryawanPengajuanController;
import app.controller.peminjaman.karyawan.KasbonKaryawanPersetujuanDirekturController;
import app.controller.penggajian.PenggajianKaryawanDaftarController;
import app.controller.penggajian.PenggajianKaryawanPencairanDanaController;
import app.controller.prestasi.CicilanListController;
import app.controller.prestasi.PengajuanFormController;
import app.controller.prestasi.PersetujuanFormController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@Component
public class HomeController implements BootInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@FXML
	private BorderPane mainLayout;

	@Autowired
	private KaryawanListController listKaryawan;

	@Autowired
	private JabatanListController listJabatan;

	@Autowired
	private AbsensiListController listAbsen;

	@Autowired
	private KasbonKaryawanListController listKasbon;

	@Autowired
	private KasbonKaryawanPengajuanController formKasbonPengajuan;

	@Autowired
	private KasbonKaryawanPersetujuanDirekturController formKasbonPersetujuan;

	@Autowired
	private KasbonKaryawanPencairanDanaController formKasbonPencairan;

	@Autowired
	private KasbonKaryawanPembayaranController formKasbonPembayaran;

	@Autowired
	private PengajuanFormController formPengajuanCicilan;

	@Autowired
	private AbsensiFormController formAbsensi;

	@Autowired
	private PersetujuanFormController formPersetujuanCicilanMotor;

	@Autowired
	private PenggajianKaryawanPencairanDanaController formPencairanDanaGaji;

	@Autowired
	private CicilanListController listCicilanMotor;

	@Autowired
	private PenggajianKaryawanDaftarController listPenggajian;

	private ApplicationContext springContext;

	public void setLayout(Node anNode) {
		mainLayout.setCenter(anNode);
		mainLayout.getCenter().autosize();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.springContext = arg0;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Home.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Autowired
	@Override
	public void setStage(Stage stage) {

	}

	@FXML
	public void closed() {
		Platform.exit();
	}

	@FXML
	public void showEmployee() {
		try {
			setLayout(listKaryawan.initView());
			listKaryawan.initConstuct();
		} catch (Exception e) {
			logger.error("Tidak dapat menampilkan daftar karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar Karyawan");
			ex.setHeaderText("Tidak dapat menampilkan daftar karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showDepartment() {
		try {
			setLayout(listJabatan.initView());
			listJabatan.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan daftar jabatan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar Jabatan");
			ex.setHeaderText("Tidak dapat menampilkan daftar jabatan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@Override
	public void initConstuct() {
	}

	@Override
	public void setNotificationDialog(DialogsFX notif) {

	}

	@Override
	public void setMessageSource(MessageSource messageSource) {

	}

	@FXML
	public void showPengajuanCicilan(ActionEvent event) {
		try {
			setLayout(formPengajuanCicilan.initView());
			formPengajuanCicilan.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan form pengajuan angsuran prestasi", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data pengajuan angsuran prestasi");
			ex.setHeaderText("Tidak dapat menampilkan form pengajuan angsuran prestasi");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showPersetujuanCicilan(ActionEvent event) {
		try {
			setLayout(formPersetujuanCicilanMotor.initView());
			formPersetujuanCicilanMotor.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan form persetujuan angsuran prestasi ", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data persetujuan angsuran prestasi");
			ex.setHeaderText("Tidak dapat menampilkan form persetujuan angsuran prestasi");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showFormAbsensi(ActionEvent event) {
		try {
			setLayout(formAbsensi.initView());
			formAbsensi.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan daftar absensi karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data absensi karyawan");
			ex.setHeaderText("Tidak dapat menampilkan form absensi untuk karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showPengajuanKasbon(ActionEvent event) {
		try {
			setLayout(formKasbonPengajuan.initView());
			formKasbonPengajuan.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan form pengajuan kasbon karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data pengajuan kasbon karyawan");
			ex.setHeaderText("Tidak dapat menampilkan form pengajuan kasbon karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showPersetujuanKasbon(ActionEvent event) {
		try {
			setLayout(formKasbonPersetujuan.initView());
			formKasbonPersetujuan.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan form persetujuan kasbon karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data persetujuan kasbon karyawan");
			ex.setHeaderText("Tidak dapat menampilkan form persetujuan kasbon karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showPencairanDanaKasbon(ActionEvent event) {
		try {
			setLayout(formKasbonPencairan.initView());
			formKasbonPencairan.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan form pencairan dana kasbon karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data pencairan dana kasbon karyawan");
			ex.setHeaderText("Tidak dapat menampilkan form pencairan dana kasbon untuk karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showPembayaranKasbon(ActionEvent event) {
		try {
			setLayout(formKasbonPembayaran.initView());
			formKasbonPembayaran.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan form pembayaran kasbon karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data pembayaran kasbon karyawan");
			ex.setHeaderText("Tidak dapat menampilkan form pembayaran kasbon karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showDaftarKehadiranKaryawan(ActionEvent event) {
		try {
			setLayout(listAbsen.initView());
			listAbsen.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan daftar hadir karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar absensi karyawan");
			ex.setHeaderText("Tidak dapat menampilkan daftar absensi seluruh karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showDaftarKasbonKaryawan(ActionEvent event) {
		try {
			setLayout(listKasbon.initView());
			listKasbon.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan daftar kasbon karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar kasbon karyawan");
			ex.setHeaderText("Tidak dapat menampilkan daftar kasbon karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showDaftarPembayaranCicilanMotor(ActionEvent event) {
		try {
			setLayout(listCicilanMotor.initView());
			listCicilanMotor.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan daftar pembayaran angsuran prestasi", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar angsuran prestasi");
			ex.setHeaderText("Tidak dapat menampilkan daftar angsuran prestasi untuk semua karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showFormPencairanDanaGaji(ActionEvent event) {
		try {
			setLayout(formPencairanDanaGaji.initView());
			formPencairanDanaGaji.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan form pencairan dana gaji karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data penggajian karyawan");
			ex.setHeaderText("Tidak dapat menampilkan form persetujuan pencairan dana gaji dan bonus untuk karyawan");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	public void showDaftarPenggajianKaryawan(ActionEvent event) {
		try {
			setLayout(listPenggajian.initView());
			listPenggajian.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan daftar penggajian karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar penggajian karyawan");
			ex.setHeaderText(
					"Tidak dapat menampilkan daftar semua karyawan yang telah menirima gaji pada periode tertentu");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

}
