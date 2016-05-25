package app.controller.prestasi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.NotificationDialogs;
import app.entities.kepegawaian.uang.prestasi.Motor;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.MotorRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class PengajuanFormController implements BootInitializable {

	private ApplicationContext springContext;
	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, Integer> columnNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;
	@FXML
	private TableColumn<DataKaryawan, String> columnHireDate;
	@FXML
	private TableColumn<DataKaryawan, String> columnJabatan;
	@FXML
	private TableColumn<DataKaryawan, String> columnJenisKelamin;
	@FXML
	private TextField txtMerek;
	@FXML
	private Spinner<Double> txtCicilan;
	@FXML
	private Spinner<Integer> txtJumlahCicilan;
	@FXML
	private Spinner<Double> txtUangMuka;
	@FXML
	private Button btnSave;
	@FXML
	private TextField txtKarywan;
	@FXML
	private TextField txtNik;

	@Autowired
	private MotorRepository serviceMotor;

	@Autowired
	private KaryawanService serviceKaryawan;

	private Motor motor;

	private SpinnerValueFactory<Double> cicilan;

	private SpinnerValueFactory<Integer> jumlahCicilan;

	private SpinnerValueFactory<Double> uangMuka;

	private void clearFields() {
		txtKarywan.clear();
		txtNik.clear();
		txtMerek.clear();
		txtCicilan.getValueFactory().setValue(0D);
		txtJumlahCicilan.getValueFactory().setValue(0);
		txtUangMuka.getValueFactory().setValue(0D);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.cicilan = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, Double.MAX_VALUE, 0D, 500);
		this.jumlahCicilan = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 5);
		this.uangMuka = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, Double.MAX_VALUE, 0D, 500);

		this.txtUangMuka.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtUangMuka.setEditable(true);
		this.txtUangMuka.setValueFactory(uangMuka);
		
		this.txtCicilan.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtCicilan.setEditable(true);
		this.txtCicilan.setValueFactory(cicilan);
		
		this.txtJumlahCicilan.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtJumlahCicilan.setEditable(true);
		this.txtJumlahCicilan.setValueFactory(jumlahCicilan);

		this.tableView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
					this.btnSave.setOnAction(e -> {
						doSave(e, newValue);
					});
					if (newValue != null) {
						txtKarywan.setText(newValue.getNama());
						txtNik.setText(newValue.getNik().toString());
					} else {
						clearFields();
					}
				});
		this.columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, Integer>("nik"));
		this.columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
		this.columnHireDate.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<DataKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<DataKaryawan, String> param) {
						return new SimpleStringProperty(param.getValue().gettLahir().toString());
					}
				});
		this.columnJabatan.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<DataKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<DataKaryawan, String> param) {
						return new SimpleStringProperty(param.getValue().getJabatan().getNama());
					}
				});
		this.columnJenisKelamin.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<DataKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<DataKaryawan, String> param) {
						return new SimpleStringProperty(param.getValue().getJenisKelamin().toString());
					}
				});

	}

	private void doSave(ActionEvent e, DataKaryawan karyawan) {
		try {
			this.motor.setMerkMotor(txtMerek.getText());
			this.motor.setDp(txtUangMuka.getValueFactory().getValue());
			this.motor.setTotalAngsuran(txtJumlahCicilan.getValueFactory().getValue());
			this.motor.setPembayaran(txtCicilan.getValueFactory().getValue());
			serviceMotor.save(this.motor);
			DataKaryawan dataKaryawan = serviceKaryawan.findOne(karyawan.getId());
			dataKaryawan.setNgicilMotor(serviceMotor.findOne(this.motor.getId()));
			this.serviceKaryawan.save(dataKaryawan);
			initConstuct();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		loader.setLocation(getClass().getResource("/scenes/inner/prestasi/pengajuan.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initConstuct() {
		this.motor = new Motor();
		this.motor.setTanggalPesan(Date.valueOf(LocalDate.now()));
		this.motor.setNoPolisi("-");
		this.motor.setSetuju(false);
		this.motor.setSudahDiterima(false);
		tableView.getItems().clear();
		for (DataKaryawan data : serviceKaryawan.findAll()) {
			if (data.isGettingCicilanMotor()) {
				tableView.getItems().add(data);
			}
		}
	}

	@Override
	@Autowired
	public void setNotificationDialog(NotificationDialogs notif) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void doCancel(ActionEvent event) {
	}

}
