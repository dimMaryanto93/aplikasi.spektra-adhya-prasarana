package app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.controller.absensi.AbsensiListController;
import app.controller.jabatan.JabatanListController;
import app.controller.karyawan.KaryawanListController;
import app.controller.kasbon.KasbonListController;
import app.controller.kasbon.KasbonPeminjamanController;
import app.controller.kasbon.KasbonPengembalianController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@Component
public class HomeController implements BootInitializable {

	@FXML
	private BorderPane mainLayout;

	@Autowired
	private KaryawanListController listKaryawan;

	@Autowired
	private JabatanListController listJabatan;

	@Autowired
	private AbsensiListController listAbsen;

	@Autowired
	private KasbonListController listKasbon;
	
	@Autowired
	private KasbonPeminjamanController formPeminjaman;
	
	@Autowired
	private KasbonPengembalianController formPengembalian;

	private ApplicationContext springContext;
	private Stage primaryStage;

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

	@FXML
	public void showAttendance(ActionEvent e) {
		try {
			setLayout(listAbsen.initView());
			listAbsen.initConstuct();
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

}
