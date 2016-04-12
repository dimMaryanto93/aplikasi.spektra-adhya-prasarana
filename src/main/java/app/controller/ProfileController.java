package app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInnerInitializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class ProfileController implements BootInnerInitializable {

	private Stage primaryStage;
	private ApplicationContext springContext;

	@Override
	public Scene initView() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStage(Stage stage) {
		this.primaryStage =stage;
		// TODO Auto-generated method stub

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
	public Node initNode() throws IOException {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/Profil.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

}
