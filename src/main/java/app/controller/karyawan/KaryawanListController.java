package app.controller.karyawan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.DialogsFX;
import app.configs.FormatterFactory;
import app.controller.HomeController;
import app.entities.master.DataJabatan;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

@Component
public class KaryawanListController implements BootInitializable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ApplicationContext springContext;

	@Autowired
	private KaryawanService service;

	@Autowired
	private HomeController homeController;

	@Autowired
	private KaryawanFormController formController;

	@Autowired
	private FormatterFactory stringFormater;

	@FXML
	TextField txtNama;
	@FXML
	TextField txtAgama;
	@FXML
	TextField txtTempatLahir;
	@FXML
	private TextField txtTanggalLahir;
	@FXML
	private TextArea txaAlamat;
	@FXML
	private TextField txtNik;
	@FXML
	private TextField txtJabatan;
	@FXML
	private TextField txtGapok;
	@FXML
	private TextField txtJk;
	@FXML
	private TextField txtCari;
	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, String> columnNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;
	@FXML
	private TableColumn<DataKaryawan, String> columnJabatan;
	@FXML
	private TableColumn<DataKaryawan, String> columnAksi;
	@FXML
	private Button btnRemoveEmployee;
	@FXML
	private TextField txtNip;
	@FXML
	private TextField txtHireDate;

	@FXML
	private Button btnUpdateEmployee;
	private DialogsFX notif;

	private void setFields(DataKaryawan anEmployee) {
		if (anEmployee != null) {
			txtNip.setText(anEmployee.getNip());
			txtHireDate.setText(stringFormater
					.getDateTimeFormatterWithDayAndDateMonthYear(anEmployee.getTanggalMulaiKerja().toLocalDate()));
			txtNama.setText(anEmployee.getNama());
			txtAgama.setText(anEmployee.getAgama().toString());
			txtTempatLahir.setText(anEmployee.getTempatLahir());
			txtTanggalLahir.setText(anEmployee.getTanggalLahir().toString());
			txaAlamat.setText(anEmployee.getAlamat());
			txtNik.setText(String.valueOf(anEmployee.getNik()));
			txtJabatan.setText(anEmployee.getJabatan().getNama());
			txtGapok.setText(this.stringFormater.getCurrencyFormate(anEmployee.getGaji()));
			txtJk.setText(anEmployee.getJenisKelamin().toString());
		} else {
			clearFields();
		}
	}

	private void clearFields() {
		txtNip.clear();
		txtNama.clear();
		txtAgama.clear();
		txtTempatLahir.clear();
		txtTanggalLahir.clear();
		txtHireDate.clear();
		txaAlamat.clear();
		txtNik.clear();
		txtJabatan.clear();
		txtGapok.clear();
		txtJk.clear();
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/karyawan/list.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends DataKaryawan> ob, DataKaryawan e, DataKaryawan newValue) -> {
					setFields(newValue);
					btnRemoveEmployee.setDisable(newValue == null);
					btnRemoveEmployee.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							doDelete(newValue);
						}
					});
					btnUpdateEmployee.setDisable(newValue == null);
					btnUpdateEmployee.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							doUpdate(newValue);
						}
					});

				});
		columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nip"));
		columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
		columnJabatan.setCellValueFactory(params -> {
			DataJabatan j = params.getValue().getJabatan();
			if (j != null) {
				return new SimpleStringProperty(j.getNama());
			} else {
				return new SimpleStringProperty();
			}
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public void initConstuct() {
		try {
			tableView.getItems().clear();
			tableView.getItems().addAll(service.findAll());
		} catch (Exception e) {
			logger.error("Tidak dapat menampilkan data karyawan", e);
			notif.showDefaultErrorLoad("Daftar Karyawan", e);
		}
	}

	@FXML
	public void doAddEmployee(ActionEvent event) {
		try {
			homeController.setLayout(formController.initView());
			formController.initConstuct();
		} catch (IOException e) {
			logger.error("Tidak dapat menampilkan form karyawan", e);
			notif.showDefaultErrorLoadForm("Data Karyawan", e);
		}
	}

	@FXML
	public void doClearSelection(ActionEvent e) {
		tableView.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefreshData(ActionEvent e) {
		initConstuct();
	}

	public void doUpdate(DataKaryawan employee) {
		try {
			homeController.setLayout(formController.initView());
			formController.initConstuct(employee);
		} catch (Exception e) {
			logger.error("Tidak dapat menampilkan form karyawan", e);
			notif.showDefaultErrorLoadForm("Data Karyawan", e);
		}
	}

	public void doDelete(DataKaryawan employee) {
		try {
			service.delete(employee);
			initConstuct();
		} catch (Exception e) {
			logger.error("Tidak dapat menghapus data karyawan", e);
			notif.showDefaultErrorDelete("Data Karyawan", e);
		}
	}

	@Override
	@Autowired
	public void setNotificationDialog(DialogsFX notif) {
		this.notif = notif;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		// TODO Auto-generated method stub

	}

}
