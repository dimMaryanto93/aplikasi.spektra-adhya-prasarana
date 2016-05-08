package app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.controller.absensi.ListAbsensiController;
import app.controller.jabatan.JabatanListController;
import app.controller.karyawan.EmployeeController;
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
			EmployeeController employee = springContext.getBean(EmployeeController.class);
			setLayout(employee.initView());
			employee.initConstuct();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void showProfile() {
		try {
			ProfileController profil = springContext.getBean(ProfileController.class);
			setLayout(profil.initView());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void showDepartment(ActionEvent e){
		try {
			JabatanListController jab = springContext.getBean(JabatanListController.class);
			setLayout(jab.initView());
			jab.initConstuct();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@FXML
	public void showAttendance(ActionEvent e){
		try {
			ListAbsensiController list = springContext.getBean(ListAbsensiController.class);
			setLayout(list.initView());
			list.initConstuct();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void initConstuct() {
	}

}
