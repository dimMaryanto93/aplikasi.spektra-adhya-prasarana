package app;

import java.io.IOException;
import java.util.Locale;

import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import app.controller.HomeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

@SpringBootApplication
public class MainApplication extends Application {

    private final Logger loger = LoggerFactory.getLogger(this.getClass());
    private ConfigurableApplicationContext springContext;
    private static String[] args;

    @Bean
    @Scope(value = "prototype")
    public ValidationSupport validation() {
        return new ValidationSupport();
    }

    @Bean()
    public Stage getStage() {
        Stage newStage = new Stage(StageStyle.DECORATED);
        newStage.setTitle("PT. Spektra Adhya Prasarana");
        return newStage;
    }

    @Override
    public void stop() throws Exception {
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
        worker.run();

        worker.setOnSucceeded((WorkerStateEvent event) -> {
            try {
                loger.info("JavaFX loading...");

                HomeController scene = springContext.getBean(HomeController.class);
                Stage stage = springContext.getBean(Stage.class);

                Parent parent = (Parent) scene.initView();
                stage.setScene(new Scene(parent));
                stage.setResizable(false);
                stage.show();

                scene.showLoginForm();
                loger.info("JavaFX started, have nice day sir!");

                Notifications.create().title("Spektra Adhya Prasarana")
                        .text("SIPeg, Sistem Informasi Penggajian pada PT. Spektra Adhya Prasarana...")
                        .position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(5)).showInformation();
            } catch (IOException e) {
                loger.error("Gagal load JavaFX Application", e);
            }
        });
        worker.setOnFailed(e -> {
            try {
                this.loger.info("Application force stoped!");
                Alert ex = new Alert(AlertType.ERROR);
                ex.setTitle("PT.Spektra Adhya Prasarana");
                ex.setHeaderText("Tidak dapat menjalankan aplikasi");
                ex.setContentText("Aplikasi akan dihetikan otomatis!, silahkan hubungi developer");
                ex.show();
            } catch (Exception e1) {
                this.loger.error("Gagal menghentikan aplikasi secara paksa!", e1);
            }
        });
    }

}
