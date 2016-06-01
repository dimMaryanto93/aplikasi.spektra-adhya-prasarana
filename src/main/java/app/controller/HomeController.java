package app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import app.controller.kasbon.KasbonPeminjamanController;
import app.controller.kasbon.KasbonPengembalianController;
import app.controller.peminjaman.karyawan.KasbonKaryawanListController;
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
	private KasbonPeminjamanController formPeminjaman;

	@Autowired
	private KasbonPengembalianController formPengembalian;

	@Autowired
	private PengajuanFormController formPengajuanCicilan;

	@Autowired
	private AbsensiFormController formAbsensi;

	@Autowired
	private PersetujuanFormController formPersetujuanCicilanMotor;

	@Autowired
	private CicilanListController listCicilanMotor;

	private ApplicationContext springContext;
	private Stage primaryStage;

	private DialogsFX notificationDialogs;

	public void setLayout(Node anNode) {
		mainLayout.setCenter(anNode);
		mainLayout.getCenter().autosize();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
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
		this.primaryStage = stage;

	}

	@FXML
	public void closed() {
		System.out.println(notificationDialogs.toString());
		Platform.exit();
	}

	@FXML
	public void showEmployee() {
		try {
			setLayout(listKaryawan.initView());
			listKaryawan.initConstuct();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void showProfile() {

	}

	@FXML
	public void showDepartment() {
		try {
			setLayout(listJabatan.initView());
			listJabatan.initConstuct();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void initConstuct() {
	}

	@FXML
	public void showKasbon(ActionEvent event) {
		try {
			setLayout(listKasbon.initView());
			listKasbon.initConstuct();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void showPeminjaman(ActionEvent event) {
		try {
			setLayout(formPeminjaman.initView());
			formPeminjaman.initConstuct();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void showPengembalian(ActionEvent event) {
		try {
			setLayout(formPengembalian.initView());
			formPengembalian.initConstuct();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	@Autowired
	public void setNotificationDialog(DialogsFX notif) {
		this.notificationDialogs = notif;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void showPengajuanCicilan(ActionEvent event) {
		try {
			setLayout(formPengajuanCicilan.initView());
			formPengajuanCicilan.initConstuct();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void showPersetujuanCicilan(ActionEvent event) {
		try {
			setLayout(formPersetujuanCicilanMotor.initView());
			formPersetujuanCicilanMotor.initConstuct();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	public void showPembayaranCicilan(ActionEvent event) {
		try {
			setLayout(listCicilanMotor.initView());
			listCicilanMotor.initConstuct();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	public void showDaftarHadirKaryawan(ActionEvent event) {
		try {
			setLayout(listAbsen.initView());
			listAbsen.initConstuct();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@FXML
	public void showFormAbsensi(ActionEvent event) {
		try {
			setLayout(formAbsensi.initView());
			formAbsensi.initConstuct();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
