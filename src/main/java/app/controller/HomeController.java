package app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class HomeController implements BootInitializable {

	private ApplicationContext springContext;
	private Stage primaryStage;

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
	public Scene initView() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Home.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		Parent parent = loader.load();
		return new Scene(parent);
	}

	@Autowired
	@Override
	public void setStage(Stage stage) {
		this.primaryStage = stage;

	}

}
