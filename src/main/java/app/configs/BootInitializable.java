/**
 * 
 */
package app.configs;

import java.io.IOException;

import org.springframework.context.ApplicationContextAware;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author dimMaryanto
 *
 */
public interface BootInitializable extends Initializable, ApplicationContextAware {

	public Scene initView() throws IOException;

	public void setStage(Stage stage);

}
