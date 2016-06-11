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
import app.configs.FontIconFactory;
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
import app.controller.prestasi.AngsuranPrestasiListController;
import app.controller.prestasi.AngsuranPrestasiPengajuanFormController;
import app.controller.prestasi.AngsuranPrestasiPersetujuanFormController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

@Component
public class HomeController implements BootInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
	private AngsuranPrestasiPengajuanFormController formPengajuanCicilan;
	@Autowired
	private AbsensiFormController formAbsensi;
	@Autowired
	private AngsuranPrestasiPersetujuanFormController formPersetujuanCicilanMotor;
	@Autowired
	private PenggajianKaryawanPencairanDanaController formPencairanDanaGaji;
	@Autowired
	private AngsuranPrestasiListController listCicilanMotor;
	@Autowired
	private PenggajianKaryawanDaftarController listPenggajian;
	@Autowired
	private LoginController loginForm;
	@Autowired
	private WellcomeController wellcomeForm;

	@FXML
	private BorderPane mainLayout;
	@FXML
	private MenuBar mnuBarAplikasi;
	@FXML
	private Menu mnuBarMaster;
	@FXML
	private MenuItem mnuMasterKaryawan;
	@FXML
	private MenuItem mnuMasterJabatan;
	@FXML
	private Menu mnuBarKepegawaian;
	@FXML
	private MenuItem mniKepegAbsensi;
	@FXML
	private Menu mnuBarKepegKasbon;
	@FXML
	private MenuItem mniKepegKasbonPengajuan;
	@FXML
	private MenuItem mniKepegKasbonPersetujuan;
	@FXML
	private MenuItem mniKepegKasbonPencairan;
	@FXML
	private MenuItem mniKepegKasbonPembayaran;
	@FXML
	private Menu mnuBarKepegAngsuran;
	@FXML
	private MenuItem mniKepegAngsuranPengajuan;
	@FXML
	private MenuItem mniKepegAngsuranPersetujuan;
	@FXML
	private MenuItem mniKepegPenggajian;
	@FXML
	private Menu mnuBarLaporan;
	@FXML
	private MenuItem mniLaporanAbsensi;
	@FXML
	private MenuItem mniLaporanKasbon;
	@FXML
	private MenuItem mniLaporanAngsuran;
	@FXML
	private MenuItem mniLaporanPenggajian;
	@FXML
	private MenuBar mnuBarKeamanan;
	@FXML
	private Menu mnuKeamananUser;
	@FXML
	private Button mniButtonHome;
	@FXML
	private Button mniButtonLogin;
	@FXML
	private Button mniButtonJabatan;
	@FXML
	private Button mniButtonKaryawan;
	@FXML
	private Button mniButtonAbsensi;
	@FXML
	private MenuButton mnuButtonKasbon;
	@FXML
	private MenuItem mniButtonKasbonPengajuan;
	@FXML
	private MenuItem mniButtonKasbonPersetujuan;
	@FXML
	private MenuItem mniButtonKasbonPencairan;
	@FXML
	private MenuItem mniButtonKasbonPembayaran;
	@FXML
	private MenuButton mnuButtonAnggsuran;
	@FXML
	private MenuItem mniButtonAngsuranPengajuan;
	@FXML
	private MenuItem mniButtonAngsuranPersetujuan;
	@FXML
	private Button mniButtonPenggajian;
	@FXML
	private Button mniButtonLogout;
	@FXML
	private Button mniButtonExit;
	@FXML
	private MenuItem mniBarKeamananProfile;
	@FXML
	private MenuItem mniBarKeamananLogout;
	@FXML
	private MenuItem mniBarKeamananExit;
	@FXML
	private Menu mnuKeamananNotifikasi;

	private ApplicationContext springContext;

	public void setLayout(Node anNode) {
		DoubleProperty opacity = anNode.opacityProperty();
		anNode.setOpacity(0.0);
		mainLayout.setCenter(anNode);
		Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
				new KeyFrame(new Duration(1000), new KeyValue(opacity, 1.0)));
		fadeIn.play();
		mainLayout.getCenter().autosize();

	}

	private void initIcons() {
		FontIconFactory icon = springContext.getBean(FontIconFactory.class);
		// left side navigation
		icon.createFontAwesomeIcon32px(mniButtonHome, FontAwesomeIcon.HOME);
		icon.createFontAwesomeIcon32px(mniButtonLogin, FontAwesomeIcon.SIGN_IN);
		icon.createFontAwesomeIcon32px(mniButtonJabatan, FontAwesomeIcon.BUILDING_ALT);
		icon.createFontAwesomeIcon32px(mniButtonKaryawan, FontAwesomeIcon.MALE);
		icon.createFontAwesomeIcon32px(mniButtonAbsensi, FontAwesomeIcon.BOOK);

		icon.createFontAwesomeIcon32px(mnuButtonKasbon, FontAwesomeIcon.BRIEFCASE);
		// menu item kasbon
		icon.createFontAwesomeIcon24px(mniButtonKasbonPengajuan, FontAwesomeIcon.ENVELOPE);
		icon.createFontAwesomeIcon24px(mniButtonKasbonPersetujuan, FontAwesomeIcon.LEGAL);
		icon.createFontAwesomeIcon24px(mniButtonKasbonPencairan, FontAwesomeIcon.MAIL_FORWARD);
		icon.createFontAwesomeIcon24px(mniButtonKasbonPembayaran, FontAwesomeIcon.MAIL_REPLY);

		icon.createFontAwesomeIcon32px(mnuButtonAnggsuran, FontAwesomeIcon.CC_VISA);
		// menu item angsuran
		icon.createFontAwesomeIcon24px(mniButtonAngsuranPersetujuan, FontAwesomeIcon.LEGAL);
		icon.createFontAwesomeIcon24px(mniButtonAngsuranPengajuan, FontAwesomeIcon.ENVELOPE);

		icon.createFontAwesomeIcon32px(mniButtonPenggajian, FontAwesomeIcon.DOLLAR);
		icon.createFontAwesomeIcon32px(mniButtonLogout, FontAwesomeIcon.SIGN_OUT);
		icon.createFontAwesomeIcon32px(mniButtonExit, FontAwesomeIcon.POWER_OFF);

		// top-side navigation left
		icon.createFontAwesomeIcon18px(mnuBarMaster, FontAwesomeIcon.DATABASE);
		// menu item data master
		icon.createFontAwesomeIcon18px(mnuMasterKaryawan, FontAwesomeIcon.MALE);
		icon.createFontAwesomeIcon18px(mnuMasterJabatan, FontAwesomeIcon.BUILDING_ALT);

		icon.createFontAwesomeIcon18px(mnuBarKepegawaian, FontAwesomeIcon.UNIVERSITY);
		icon.createFontAwesomeIcon18px(mniKepegAbsensi, FontAwesomeIcon.BOOK);
		icon.createFontAwesomeIcon18px(mnuBarKepegKasbon, FontAwesomeIcon.GOOGLE_WALLET);
		// menu item kepegawaian kasbon
		icon.createFontAwesomeIcon18px(mniKepegKasbonPengajuan, FontAwesomeIcon.ENVELOPE);
		icon.createFontAwesomeIcon18px(mniKepegKasbonPersetujuan, FontAwesomeIcon.LEGAL);
		icon.createFontAwesomeIcon18px(mniKepegKasbonPencairan, FontAwesomeIcon.MAIL_FORWARD);
		icon.createFontAwesomeIcon18px(mniKepegKasbonPembayaran, FontAwesomeIcon.MAIL_REPLY);
		// menu item kepegawaian angsuran
		icon.createFontAwesomeIcon18px(mnuBarKepegAngsuran, FontAwesomeIcon.CC_VISA);
		icon.createFontAwesomeIcon18px(mniKepegAngsuranPersetujuan, FontAwesomeIcon.LEGAL);
		icon.createFontAwesomeIcon18px(mniKepegAngsuranPengajuan, FontAwesomeIcon.ENVELOPE);

		icon.createFontAwesomeIcon18px(mniKepegPenggajian, FontAwesomeIcon.DOLLAR);
		icon.createFontAwesomeIcon18px(mnuBarLaporan, FontAwesomeIcon.PRINT);
		// menu item laporan

		// top-side navigation right
		icon.createFontAwesomeIcon18px(mnuKeamananNotifikasi, FontAwesomeIcon.BELL_ALT);

		icon.createFontAwesomeIcon18px(mnuKeamananUser, FontAwesomeIcon.USER);
		icon.createFontAwesomeIcon18px(mniBarKeamananProfile, FontAwesomeIcon.COGS);
		icon.createFontAwesomeIcon18px(mniBarKeamananExit, FontAwesomeIcon.POWER_OFF);
		icon.createFontAwesomeIcon18px(mniBarKeamananLogout, FontAwesomeIcon.SIGN_OUT);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initIcons();

		/**
		 * tampilnotifkasi ketika diklik langsung hilang
		 */
		for (int i = 0; i < 10; i++) {
			MenuItem menu = new MenuItem("Menu ke-" + (i + 1));
			menu.setOnAction(e -> {
				mnuKeamananNotifikasi.getItems().remove(menu);
			});
			mnuKeamananNotifikasi.getItems().add(menu);

		}

	}

	public void setMnuBarAplikasi(Boolean disable) {
		this.mnuBarAplikasi.setDisable(disable);
	}

	public void setMnuBarMaster(Boolean disable) {
		this.mnuBarMaster.setDisable(disable);
	}

	public void setMnuMasterKaryawan(Boolean disable) {
		this.mnuMasterKaryawan.setDisable(disable);
	}

	public void setMnuMasterJabatan(Boolean disable) {
		this.mnuMasterJabatan.setDisable(disable);
	}

	public void setMnuBarKepegawaian(Boolean disable) {
		this.mnuBarKepegawaian.setDisable(disable);
	}

	public void setMniKepegAbsensi(Boolean disable) {
		this.mniKepegAbsensi.setDisable(disable);
	}

	public void setMnuBarKepegKasbon(Boolean disable) {
		this.mnuBarKepegKasbon.setDisable(disable);
	}

	public void setMniKepegKasbonPengajuan(Boolean disable) {
		this.mniKepegKasbonPengajuan.setDisable(disable);
	}

	public void setMniKepegKasbonPersetujuan(Boolean disable) {
		this.mniKepegKasbonPersetujuan.setDisable(disable);
	}

	public void setMniKepegKasbonPencairan(Boolean disable) {
		this.mniKepegKasbonPencairan.setDisable(disable);
	}

	public void setMniKepegKasbonPembayaran(Boolean disable) {
		this.mniKepegKasbonPembayaran.setDisable(disable);
	}

	public void setMnuBarKepegAngsuran(Boolean disable) {
		this.mnuBarKepegAngsuran.setDisable(disable);
	}

	public void setMniKepegAngsuranPengajuan(Boolean disable) {
		this.mniKepegAngsuranPengajuan.setDisable(disable);
	}

	public void setMniKepegAngsuranPersetujuan(Boolean disable) {
		this.mniKepegAngsuranPersetujuan.setDisable(disable);
	}

	public void setMniKepegPenggajian(Boolean disable) {
		this.mniKepegPenggajian.setDisable(disable);
	}

	public void setMnuBarLaporan(Boolean disable) {
		this.mnuBarLaporan.setDisable(disable);
	}

	public void setMniLaporanAbsensi(Boolean disable) {
		this.mniLaporanAbsensi.setDisable(disable);
	}

	public void setMniLaporanKasbon(Boolean disable) {
		this.mniLaporanKasbon.setDisable(disable);
	}

	public void setMniLaporanAngsuran(Boolean disable) {
		this.mniLaporanAngsuran.setDisable(disable);
	}

	public void setMniLaporanPenggajian(Boolean disable) {
		this.mniLaporanPenggajian.setDisable(disable);
	}

	public void setMnuBarKeamanan(Boolean disable) {
		this.mnuBarKeamanan.setDisable(disable);
	}

	public void setMnuKeamananUser(Boolean disable) {
		this.mnuKeamananUser.setDisable(disable);
	}

	public void setMniButtonHome(Boolean disable) {
		this.mniButtonHome.setDisable(disable);
	}

	public void setMniButtonLogin(Boolean disable) {
		this.mniButtonLogin.setDisable(disable);
	}

	public void setMniButtonJabatan(Boolean disable) {
		this.mniButtonJabatan.setDisable(disable);
	}

	public void setMniButtonKaryawan(Boolean disable) {
		this.mniButtonKaryawan.setDisable(disable);
	}

	public void setMniButtonAbsensi(Boolean disable) {
		this.mniButtonAbsensi.setDisable(disable);
	}

	public void setMnuButtonKasbon(Boolean disable) {
		this.mnuButtonKasbon.setDisable(disable);
	}

	public void setMniButtonKasbonPengajuan(Boolean disable) {
		this.mniButtonKasbonPengajuan.setDisable(disable);
	}

	public void setMniButtonKasbonPersetujuan(Boolean disable) {
		this.mniButtonKasbonPersetujuan.setDisable(disable);
	}

	public void setMniButtonKasbonPencairan(Boolean disable) {
		this.mniButtonKasbonPencairan.setDisable(disable);
	}

	public void setMniButtonKasbonPembayaran(Boolean disable) {
		this.mniButtonKasbonPembayaran.setDisable(disable);
	}

	public void setMnuButtonAnggsuran(Boolean disable) {
		this.mnuButtonAnggsuran.setDisable(disable);
	}

	public void setMniButtonAngsuranPengajuan(Boolean disable) {
		this.mniButtonAngsuranPengajuan.setDisable(disable);
	}

	public void setMniButtonAngsuranPersetujuan(Boolean disable) {
		this.mniButtonAngsuranPersetujuan.setDisable(disable);
	}

	public void setMniButtonPenggajian(Boolean disable) {
		this.mniButtonPenggajian.setDisable(disable);
	}

	public void setMniButtonLogout(Boolean disable) {
		this.mniButtonLogout.setDisable(disable);
	}

	public void setMniBarKeamananProfile(Boolean disable) {
		this.mniBarKeamananProfile.setDisable(disable);
	}

	public void setMniBarKeamananLogout(Boolean disable) {
		this.mniBarKeamananLogout.setDisable(disable);
	}

	public void setMnuKeamananNotifikasi(Boolean disable) {
		this.mnuKeamananNotifikasi.setDisable(disable);
	}

	public void enabledMenu(Boolean disable) {
		setMnuBarAplikasi(disable);
		setMnuBarMaster(disable);
		setMnuMasterKaryawan(disable);
		setMnuMasterJabatan(disable);
		setMnuBarKepegawaian(disable);
		setMniKepegAbsensi(disable);
		setMnuBarKepegKasbon(disable);
		setMniKepegKasbonPengajuan(disable);
		setMniKepegKasbonPersetujuan(disable);
		setMniKepegKasbonPencairan(disable);
		setMniKepegKasbonPembayaran(disable);
		setMnuBarKepegAngsuran(disable);
		setMniKepegAngsuranPengajuan(disable);
		setMniKepegAngsuranPersetujuan(disable);
		setMniKepegPenggajian(disable);
		setMnuBarLaporan(disable);
		setMniLaporanAbsensi(disable);
		setMniLaporanKasbon(disable);
		setMniLaporanAngsuran(disable);
		setMniLaporanPenggajian(disable);
		setMnuBarKeamanan(disable);
		setMnuKeamananUser(disable);
		setMniButtonHome(disable);
		setMniButtonLogin(disable);
		setMniButtonJabatan(disable);
		setMniButtonKaryawan(disable);
		setMniButtonAbsensi(disable);
		setMnuButtonKasbon(disable);
		setMniButtonKasbonPengajuan(disable);
		setMniButtonKasbonPersetujuan(disable);
		setMniButtonKasbonPencairan(disable);
		setMniButtonKasbonPembayaran(disable);
		setMnuButtonAnggsuran(disable);
		setMniButtonAngsuranPengajuan(disable);
		setMniButtonAngsuranPersetujuan(disable);
		setMniButtonPenggajian(disable);
		setMniButtonLogout(disable);
		setMniBarKeamananProfile(disable);
		setMniBarKeamananLogout(disable);
		setMnuKeamananNotifikasi(disable);

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

	@Override
	public void setStage(Stage stage) {

	}

	@FXML
	public void closed() {
		Platform.exit();
	}

	public void doLogout() {
		enabledMenu(true);
		showLoginForm();
		setMniButtonLogout(true);
	}

	@FXML
	public void showLoginForm() {
		try {
			setLayout(loginForm.initView());
			setMniButtonHome(true);
			setMniButtonLogin(false);
			loginForm.initConstuct();
		} catch (Exception e) {
			logger.error("Tidak dapat menampilkan form login", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Login form");
			ex.setHeaderText("Tidak dapat menampilkan form login!");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showWellcome() {
		try {
			setLayout(wellcomeForm.initView());
			wellcomeForm.initConstuct();
		} catch (Exception e) {
			logger.error("Tidak dapat menampilkan form login", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Login form");
			ex.setHeaderText("Tidak dapat menampilkan form login!");
			ex.setContentText(e.getMessage());
			ex.show();
		}
	}

	@FXML
	public void showDaftarKaryawan() {
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
	public void showDaftarJabatan() {
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
