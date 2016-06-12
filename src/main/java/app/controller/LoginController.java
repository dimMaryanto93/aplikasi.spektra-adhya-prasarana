package app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootFormInitializable;
import app.configs.FontIconFactory;
import app.entities.master.DataAkun;
import app.repositories.RepositoryAkun;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

@Component
public class LoginController implements BootFormInitializable {

	private ApplicationContext springContext;

	@Autowired
	private FontIconFactory iconFactory;
	@Autowired
	private HomeController homeController;
	@Autowired
	private RepositoryAkun akunRepository;

	@FXML
	Button btnLogin;
	@FXML
	TextField txtUsername;
	@FXML
	PasswordField txtPassword;
	@FXML
	Label labelSuccess;
	@FXML
	Button btnExit;

	private ValidationSupport validation;

	private DataAkun akun;

	public DataAkun getAkun() {
		return akun;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/Login.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initValidator();
		iconFactory.createFontAwesomeIcon18px(btnLogin, FontAwesomeIcon.SIGN_IN);
		iconFactory.createFontAwesomeIcon18px(btnExit, FontAwesomeIcon.POWER_OFF);

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {

	}

	@Override
	public void initValidator() {
		this.labelSuccess.setOpacity(0);
		this.validation = new ValidationSupport();
		this.validation.registerValidator(txtUsername,
				Validator.createEmptyValidator("Username tidak boleh kosong!", Severity.ERROR));
		this.validation.registerValidator(txtPassword,
				Validator.createEmptyValidator("Password tidak boleh kosong!", Severity.ERROR));
		this.validation.invalidProperty().addListener((v, old, value) -> {
			btnLogin.setDisable(value);
		});

	}

	@FXML
	public void doSignIn(ActionEvent event) {
		DataAkun akun = akunRepository.findByUsernameAndPasswordAndEnabledIsTrue(txtUsername.getText(),
				txtPassword.getText());
		if (akun != null) {
			this.akun = akun;
			homeController.enabledMenu(false);
			homeController.setMniButtonHome(false);
			homeController.setMniButtonLogout(false);
			homeController.setMniButtonLogin(true);
			homeController.showWellcome();
		} else {
			this.akun = null;
			
			labelSuccess.setText("Username atau password salah!");
			txtUsername.clear();
			txtPassword.clear();
			txtUsername.requestFocus();
		}
	}

	@FXML
	public void doExit(ActionEvent event) {
		Platform.exit();
	}

}
