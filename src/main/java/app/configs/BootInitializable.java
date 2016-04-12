package app.configs;

import java.io.IOException;

import org.springframework.context.ApplicationContextAware;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

public interface BootInitializable extends Initializable, ApplicationContextAware {

	public Node initView() throws IOException;

	public void setStage(Stage stage);

}
