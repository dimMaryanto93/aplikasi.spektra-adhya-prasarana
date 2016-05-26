package app.configs;

import java.io.IOException;

import org.controlsfx.validation.ValidationSupport;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSourceAware;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

public interface BootInitializable extends Initializable, ApplicationContextAware, MessageSourceAware {

	public Node initView() throws IOException;

	public void setStage(Stage stage);

	public void initConstuct();

	/**
	 * harus ditambahkan @Autowired secara manual
	 * 
	 * @param notif
	 *            digunakan untuk menampilkan dialog atau notifikasi
	 */
	public void setNotificationDialog(DialogsFX notif);

}
