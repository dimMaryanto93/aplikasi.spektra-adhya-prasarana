package app.controller.laporan.penggajian;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.validation.ValidationSupport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootFormInitializable;
import app.configs.DialogsFX;
import app.entities.kepegawaian.Penggajian;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

@Component
public class LaporanDaftarPenggajian implements BootFormInitializable {

	private DialogsFX notif;
	private ApplicationContext springContext;
	private ValidationSupport validation;

	@FXML
	private Button btnProccessed;
	@FXML
	private GridPane chenkPrinted;
	@FXML
	private TableView<Penggajian> tableView;
	@FXML
	private TableColumn<Penggajian, String> columnNip;
	@FXML
	private TableColumn<Penggajian, String> columnNama;
	@FXML
	private TableColumn<Penggajian, String> columnHadir;
	@FXML
	private TableColumn<Penggajian, String> columnLembur;
	@FXML
	private TableColumn<Penggajian, Double> columnGajiPokok;
	@FXML
	private TextField txtTotalGajiPokok;
	@FXML
	private TextField txtTotalHadir;
	@FXML
	private TextField txtTotalLembur;
	@FXML
	private TextField totalSumHadirLembur;
	@FXML
	private TextField txtGrantTotal;

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/laporan/penggajian/Daftar.fxml"));
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
	@Autowired
	public void setNotificationDialog(DialogsFX notif) {
		this.notif = notif;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
		this.validation = new ValidationSupport();
	}

	@FXML
	public void doProceess(ActionEvent event) {
	}

}
