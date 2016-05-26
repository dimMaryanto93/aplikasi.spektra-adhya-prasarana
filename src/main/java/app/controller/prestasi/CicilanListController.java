package app.controller.prestasi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import org.controlsfx.validation.ValidationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.FormatterFactory;
import app.configs.DialogsFX;
import app.entities.kepegawaian.uang.prestasi.Motor;
import app.entities.kepegawaian.uang.prestasi.PembayaranCicilanMotor;
import app.entities.master.DataKaryawan;
import app.repositories.CicilanMotorRepository;
import app.repositories.KaryawanService;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;

@Component
public class CicilanListController implements BootInitializable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ApplicationContext springContext;
	@FXML
	private TableView<DataKaryawan> tableKaryawan;
	@FXML
	private TableColumn<DataKaryawan, Integer> columnNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;
	@FXML
	private TextField txtNik;
	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtMerek;
	@FXML
	private TextField txtPlat;
	@FXML
	private TextField txtCicilan;
	@FXML
	private TextField txtAngsuran;
	@FXML
	private TableView<PembayaranCicilanMotor> tableCicilan;
	@FXML
	private TableColumn<PembayaranCicilanMotor, String> columnResi;
	@FXML
	private TableColumn<PembayaranCicilanMotor, Integer> columnAngsuran;
	@FXML
	private TableColumn<PembayaranCicilanMotor, Double> columnBIaya;
	@FXML
	private TableColumn<PembayaranCicilanMotor, Date> columnTanggal;

	@Autowired
	private KaryawanService serviceKaryawan;

	@Autowired
	private CicilanMotorRepository serviceCicilanMotor;

	@Autowired
	private FormatterFactory formater;

	private void setFields(DataKaryawan karyawan) {
		txtNik.setText(karyawan.getNik().toString());
		txtNama.setText(karyawan.getNama());

		Motor motor = karyawan.getNgicilMotor();

		txtMerek.setText(motor.getMerkMotor());
		txtPlat.setText(motor.getNoPolisi());
		txtCicilan.setText(formater.getCurrencyFormate(motor.getPembayaran()));
		txtAngsuran.setText(formater.getNumberIntegerOnlyFormate(motor.getTotalAngsuran()));
	}

	private void clearFields() {
		txtNik.clear();
		txtNama.clear();

		txtMerek.clear();
		txtPlat.clear();
		txtCicilan.clear();
		txtAngsuran.clear();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableCicilan.setSelectionModel(null);

		tableKaryawan.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
					tableCicilan.getItems().clear();
					if (newValue != null) {
						setFields(newValue);
						tableCicilan.getItems().addAll(serviceCicilanMotor.findByMotor(newValue.getNgicilMotor()));
					} else {
						clearFields();
					}
				});
		columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, Integer>("nik"));
		columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));

		columnResi.setCellValueFactory(new PropertyValueFactory<PembayaranCicilanMotor, String>("id"));
		columnAngsuran.setCellValueFactory(new PropertyValueFactory<PembayaranCicilanMotor, Integer>("angsuranKe"));
		columnAngsuran.setCellFactory(
				new Callback<TableColumn<PembayaranCicilanMotor, Integer>, TableCell<PembayaranCicilanMotor, Integer>>() {

					@Override
					public TableCell<PembayaranCicilanMotor, Integer> call(
							TableColumn<PembayaranCicilanMotor, Integer> param) {
						return new TableCell<PembayaranCicilanMotor, Integer>() {
							@Override
							protected void updateItem(Integer item, boolean empty) {
								setAlignment(Pos.CENTER);
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
								} else {
									setText(formater.getNumberIntegerOnlyFormate(item));
								}
							}
						};
					}
				});
		columnBIaya.setCellValueFactory(new PropertyValueFactory<PembayaranCicilanMotor, Double>("bayar"));
		columnBIaya.setCellFactory(
				new Callback<TableColumn<PembayaranCicilanMotor, Double>, TableCell<PembayaranCicilanMotor, Double>>() {

					@Override
					public TableCell<PembayaranCicilanMotor, Double> call(
							TableColumn<PembayaranCicilanMotor, Double> param) {
						return new TableCell<PembayaranCicilanMotor, Double>() {
							@Override
							protected void updateItem(Double item, boolean empty) {
								setAlignment(Pos.CENTER_RIGHT);
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
								} else {
									setText(formater.getCurrencyFormate(item));
								}
							}
						};
					}
				});
		columnTanggal.setCellValueFactory(new PropertyValueFactory<PembayaranCicilanMotor, Date>("tanggalBayar"));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		// TODO Auto-generated method stub

	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/prestasi/list.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initConstuct() {
		tableKaryawan.getItems().clear();
		for (DataKaryawan karyawan : serviceKaryawan.findAll()) {
			if (karyawan.isGettingCicilanMotorDisetujui()) {
				tableKaryawan.getItems().add(karyawan);
			}
		}
	}

	@Override
	public void setNotificationDialog(DialogsFX notif) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void doClear(ActionEvent event) {
		tableKaryawan.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefresh(ActionEvent event) {
		initConstuct();
	}

}
