package app.controller.prestasi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootFormInitializable;
import app.configs.DialogsFX;
import app.configs.StringFormatterFactory;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
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
public class AngsuranPrestasiPengajuanFormController implements BootFormInitializable {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ApplicationContext springContext;
	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, String> columnNik;
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
	@FXML
	private Label nominalCicilan;
	@FXML
	private Label jumlahCicilan;
	@FXML
	private Label totalPengeluaran;
	@FXML
	private Label totalUangMuka;
	@FXML
	private CheckBox checkValid;

	@Autowired
	private MotorRepository serviceMotor;

	@Autowired
	private KaryawanService serviceKaryawan;

	@Autowired
	private StringFormatterFactory stringFormater;

	private Motor motor;

	private SpinnerValueFactory.DoubleSpinnerValueFactory spinnerCicilanValueFactory;

	private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerJumlahCicilanValueFactory;

	private SpinnerValueFactory.DoubleSpinnerValueFactory spinnerUangMukaValueFactory;
	private ValidationSupport validation;
	private DialogsFX notif;

	private void clearFields() {
		txtKarywan.clear();
		txtNik.clear();
		txtMerek.clear();
		this.checkValid.setSelected(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.totalUangMuka.setText(this.stringFormater.getCurrencyFormate(0));
		this.nominalCicilan.setText(this.stringFormater.getCurrencyFormate(0));
		this.jumlahCicilan.setText(this.stringFormater.getNumberIntegerOnlyFormate(0));
		this.totalPengeluaran.setText(this.stringFormater.getCurrencyFormate(0));

		this.btnSave.setDisable(true);

		this.spinnerCicilanValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, 0D);
		this.spinnerJumlahCicilanValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0);
		this.spinnerUangMukaValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, 0D);

		this.txtUangMuka.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtUangMuka.setValueFactory(spinnerUangMukaValueFactory);
		this.txtUangMuka.setDisable(true);
		this.txtUangMuka.getValueFactory().valueProperty().addListener((b, old, value) -> {
			this.totalUangMuka.setText(this.stringFormater.getCurrencyFormate(value));
		});

		this.txtCicilan.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtCicilan.setValueFactory(spinnerCicilanValueFactory);
		this.txtCicilan.setDisable(true);
		this.txtCicilan.getValueFactory().valueProperty().addListener((d, old, value) -> {
			this.nominalCicilan.setText(this.stringFormater.getCurrencyFormate(value));
			this.totalPengeluaran.setText(
					this.stringFormater.getCurrencyFormate(value * this.txtJumlahCicilan.getValueFactory().getValue()));
		});

		this.txtJumlahCicilan.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtJumlahCicilan.setValueFactory(spinnerJumlahCicilanValueFactory);
		this.txtJumlahCicilan.setDisable(true);
		this.txtJumlahCicilan.getValueFactory().valueProperty().addListener((d, old, value) -> {
			this.jumlahCicilan.setText(this.stringFormater.getNumberIntegerOnlyFormate(value));
			this.totalPengeluaran.setText(
					this.stringFormater.getCurrencyFormate(value * this.txtCicilan.getValueFactory().getValue()));
		});

		this.tableView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
					this.btnSave.setOnAction(e -> {
						doSave(e, newValue);
					});

					this.txtJumlahCicilan.setDisable(newValue == null);
					this.txtCicilan.setDisable(newValue == null);
					this.txtUangMuka.setDisable(newValue == null);

					this.txtJumlahCicilan.setEditable(newValue != null);
					this.txtCicilan.setEditable(newValue != null);
					this.txtUangMuka.setEditable(newValue != null);

					if (newValue != null) {
						txtKarywan.setText(newValue.getNama());
						txtNik.setText(newValue.getNik().toString());
						this.spinnerCicilanValueFactory.setMax(Double.MAX_VALUE);
						this.spinnerCicilanValueFactory.setMin(0D);
						this.spinnerCicilanValueFactory.setAmountToStepBy(50000);
						this.spinnerCicilanValueFactory.setValue(Double.valueOf(500000));

						this.spinnerJumlahCicilanValueFactory.setMax(100);
						this.spinnerJumlahCicilanValueFactory.setMin(0);
						this.spinnerJumlahCicilanValueFactory.setAmountToStepBy(5);
						this.spinnerJumlahCicilanValueFactory.setValue(30);

						this.spinnerUangMukaValueFactory.setMax(Double.MAX_VALUE);
						this.spinnerUangMukaValueFactory.setMin(0D);
						this.spinnerUangMukaValueFactory.setAmountToStepBy(50000);
						this.spinnerUangMukaValueFactory.setValue(Double.valueOf(3000000));
					} else {
						clearFields();

						this.spinnerCicilanValueFactory.setValue(0D);
						this.spinnerCicilanValueFactory.setMax(0D);
						this.spinnerCicilanValueFactory.setMin(0D);
						this.spinnerCicilanValueFactory.setAmountToStepBy(0D);

						this.spinnerJumlahCicilanValueFactory.setValue(0);
						this.spinnerJumlahCicilanValueFactory.setMax(0);
						this.spinnerJumlahCicilanValueFactory.setMin(0);
						this.spinnerCicilanValueFactory.setAmountToStepBy(0);

						this.spinnerUangMukaValueFactory.setValue(0D);
						this.spinnerUangMukaValueFactory.setMax(0D);
						this.spinnerUangMukaValueFactory.setMin(0D);
						this.spinnerUangMukaValueFactory.setAmountToStepBy(0D);
					}
				});

		this.columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nip"));
		this.columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
		this.columnHireDate.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<DataKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<DataKaryawan, String> param) {
						return new SimpleStringProperty(param.getValue().getTanggalLahir().toString());
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
		this.initValidator();
	}

	private void doSave(ActionEvent e, DataKaryawan karyawan) {
		try {
			this.motor.setMerkMotor(txtMerek.getText());
			this.motor.setDp(txtUangMuka.getValueFactory().getValue());
			this.motor.setTotalAngsuran(txtJumlahCicilan.getValueFactory().getValue());
			this.motor.setPembayaran(txtCicilan.getValueFactory().getValue());
			serviceMotor.save(this.motor);
			DataKaryawan dataKaryawan = serviceKaryawan.findOne(karyawan.getIndex());
			dataKaryawan.setNgicilMotor(serviceMotor.findOne(this.motor.getId()));
			this.serviceKaryawan.save(dataKaryawan);

			notif.showDefaultSave("Data Pengajuan Uang Prestasi");

			initConstuct();
		} catch (Exception e1) {
			logger.error("Tidak dapat menyimpan data pengajuan uang prestasi untuk karyawan dengan nama {}",
					karyawan.getNama(), e);
			notif.showDefaultErrorSave("Data Pengajuan Uang Prestasi", e1);
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
		loader.setLocation(getClass().getResource("/scenes/inner/prestasi/Pengajuan.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initConstuct() {
		try {
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
		} catch (Exception e) {
			logger.error("Tidak dapat mendapatkan data karayawan yang diperbolehkan mengajukan cicilan motor", e);
			notif.showDefaultErrorLoad("Data Pengajuan Uang Prestasi", e);
		}
	}

	@Override
	@Autowired
	public void setNotificationDialog(DialogsFX notif) {
		this.notif = notif;
	}

	@FXML
	public void doCancel(ActionEvent event) {
	}

	@Override
	public void initValidator() {
		this.validation = new ValidationSupport();
		this.validation.registerValidator(txtNik,
				Validator.createEmptyValidator("Karyawan belum dipilih", Severity.ERROR));
		this.validation.registerValidator(txtMerek,
				Validator.createEmptyValidator("Nama merek kendaraan bermotor", Severity.ERROR));
		this.validation.registerValidator(txtUangMuka.getEditor(),
				(Control c, String value) -> ValidationResult.fromErrorIf(c,
						"Uang muka minimal harus lebih dari " + stringFormater.getCurrencyFormate(100),
						Double.valueOf(value) < 100));
		this.validation.registerValidator(txtCicilan.getEditor(),
				(Control c, String value) -> ValidationResult.fromErrorIf(c,
						"Cicilam motor minimal harus lebih dari " + stringFormater.getCurrencyFormate(100),
						Double.valueOf(value) < 100));
		this.validation.registerValidator(txtJumlahCicilan.getEditor(),
				(Control c, String value) -> ValidationResult.fromErrorIf(c,
						"Jumlah cicilan minamal harus lebih dari " + stringFormater.getNumberIntegerOnlyFormate(5),
						Integer.valueOf(value) < 5));
		this.validation.registerValidator(checkValid, (Control c, Boolean value) -> ValidationResult.fromErrorIf(c,
				"Silahkan ceklisk jika data diatas telah sesuia!", value == false));
		this.validation.invalidProperty().addListener((o, old, newValue) -> {
			btnSave.setDisable(newValue);
		});
	}

	@FXML
	public void doClear(ActionEvent event) {
		tableView.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefresh(ActionEvent event) {
		initConstuct();
	}

}
