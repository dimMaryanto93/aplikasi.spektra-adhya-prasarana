package app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@Component
public class HomeController implements BootInitializable {

	@FXML
	private BorderPane mainLayout;

	private ApplicationContext springContext;
	private Stage primaryStage;

	private void setLayout(Node anNode) {

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

	public void showEmployee() {
		try {
			EmployeeController employ = springContext.getBean(EmployeeController.class);
			setLayout(employ.initView());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showProfile() {
		try {
			ProfileController profil = springContext.getBean(ProfileController.class);
			setLayout(profil.initView());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
