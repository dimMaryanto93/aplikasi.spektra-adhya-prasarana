package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

@SpringBootApplication
public class MainApplication extends Application {

	private ConfigurableApplicationContext springContext;

	private static String[] args;

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		Platform.exit();
		springContext.close();
	}

	public static void main(String[] args) {
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
				// TODO javafx runner
				primaryStage.show();
			}
		});
		worker.run();
	}

}
