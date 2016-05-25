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
import app.configs.FormatterFactory;
import app.configs.NotificationDialogs;
import app.entities.kepegawaian.uang.prestasi.Motor;
import app.entities.kepegawaian.uang.prestasi.PembayaranCicilanMotor;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.MotorRepository;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

@Component
public class PersetujuanFormController implements BootInitializable {

	private ApplicationContext springContext;
	@FXML
	private Button btnSetuju;
	@FXML
	private Button btnBatal;
	@FXML
	private CheckBox check;
	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, Integer> columnNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;
	@FXML
	private TextField txtNik;
	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtTanggal;
	@FXML
	private TextField txtMerekMotor;
	@FXML
	private TextField txtCicilan;
	@FXML
	private TextField txtAngsuran;
	@FXML
	private TextField txtUangMuka;
	@FXML
	private TextField txtJabatan;
	@FXML
	private TextField txtGajiPokok;

	@Autowired
	private KaryawanService serviceKaryawan;
	@Autowired
	private FormatterFactory formater;

	@Autowired
	private MotorRepository serviceMotor;

	private void clearFields() {
		txtNik.clear();
		txtNama.clear();
		txtTanggal.clear();
		txtMerekMotor.clear();
		txtCicilan.clear();
		txtAngsuran.clear();
		txtUangMuka.clear();
		txtJabatan.clear();
		txtGajiPokok.clear();
	}

	private void setFields(DataKaryawan karyawan) {
		txtNik.setText(karyawan.getNik().toString());
		txtNama.setText(karyawan.getNama());
		txtTanggal.setText(karyawan.getTanggalMulaiKerja().toString());
		txtJabatan.setText(karyawan.getJabatan().getNama());

		Motor motor = karyawan.getNgicilMotor();
		txtMerekMotor.setText(motor.getMerkMotor());
		txtCicilan.setText(formater.getCurrencyFormate(motor.getPembayaran()));
		txtAngsuran.setText(formater.getNumberIntegerOnlyFormate(motor.getTotalAngsuran()));
		txtUangMuka.setText(formater.getCurrencyFormate(motor.getDp()));
		txtGajiPokok.setText(formater.getCurrencyFormate(karyawan.getGaji()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.btnSetuju.setDisable(true);

		this.check.setOpacity(0D);
		this.check.setText("");

		this.check.selectedProperty()
				.addListener((ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) -> {
					btnSetuju.setDisable(!newValue);
				});
		tableView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
					this.btnSetuju.setOnAction(e -> {
						doSave(e, newValue);
					});
					if (newValue != null) {
						setFields(newValue);
						this.check.setOpacity(0.9);
						this.check.setText("Saya bersedia memberikan Uang Muka sebesar "
								+ formater.getCurrencyFormate(newValue.getNgicilMotor().getDp()));
					} else {
						this.check.setOpacity(0D);
						this.check.setText("");
						clearFields();
					}
				});
		columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, Integer>("nik"));
		columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
	}

	private void doSave(ActionEvent e, DataKaryawan newValue) {
		Motor m = newValue.getNgicilMotor();
		m.setSetuju(true);
		PembayaranCicilanMotor cicilanMotor = new PembayaranCicilanMotor();
		cicilanMotor.setMotor(m);
		cicilanMotor.setTanggalBayar(Date.valueOf(LocalDate.now()));
		cicilanMotor.setBayar(m.getDp());
		cicilanMotor.setAngsuranKe(1);
		m.getDaftarCicilan().add(cicilanMotor);

		serviceMotor.save(m);
		initConstuct();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {

	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/prestasi/persetujuan.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initConstuct() {
		tableView.getItems().clear();
		for (DataKaryawan karyawan : serviceKaryawan.findAll()) {
			if (karyawan.isGettingCililanMotorUntukDisetujui()) {
				tableView.getItems().add(karyawan);
			}
		}
	}

	@Override
	public void setNotificationDialog(NotificationDialogs notif) {
		// TODO Auto-generated method stub

	}

}