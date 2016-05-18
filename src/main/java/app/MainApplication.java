package app;

import java.io.IOException;
import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import app.controller.HomeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SpringBootApplication
public class MainApplication extends Application {

	private ConfigurableApplicationContext springContext;

	private static String[] args;

	@Bean()
	public Stage getStage() {
		Stage newStage = new Stage(StageStyle.DECORATED);
		newStage.setTitle("PT. Spektra Adhya Prasarana");
		return newStage;
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		Platform.exit();
		springContext.close();
	}

	public static void main(String[] args) {
		Locale.setDefault(new Locale("in", "ID"));
		MainApplication.args = args;
		launch(MainApplication.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Task<Object> worker = new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				springContext = SpringApplication.run(MainApplication.class, MainApplication.args);
				return null;
			}
		};

		worker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				try {
					HomeController scene = springContext.getBean(HomeController.class);
					Stage stage = springContext.getBean(Stage.class);
					Parent parent = (Parent) scene.initView();
					stage.setScene(new Scene(parent));
					stage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		worker.setOnFailed(e -> {
			System.err.println("---------------------------------------------------");
			System.err.println("Aplikasi dihentika secara paksa! karena berikut ini:\n" + e.getSource().getException());
			System.exit(0);
		});
		worker.run();
	}

}
