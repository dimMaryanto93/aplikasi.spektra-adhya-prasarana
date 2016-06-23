package app.controller.jabatan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
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
import app.configs.StringFormatterFactory;
import app.controller.HomeController;
import app.entities.master.DataJabatan;
import app.repositories.RepositoryJabatan;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

@Component
public class JabatanFormController implements BootFormInitializable {

	private DataJabatan jabatan;
	private Boolean update;
	private ValidationSupport validation;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ApplicationContext springContext;
	@FXML
	private TextField txtNama;
	@FXML
	private TextArea txtKeterangan;
	@FXML
	private TextField txtKode;
	@FXML
	private Spinner<Double> spinGapok;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;
	@FXML
	private TextArea txtPernyataan;
	@FXML
	private CheckBox txtValid;

	@Autowired
	private RepositoryJabatan repo;
	@Autowired
	private HomeController homeController;
	@Autowired
	private StringFormatterFactory stringFormater;

	public Boolean isUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.btnSave.setDisable(true);
		this.txtPernyataan.setDisable(false);
		this.txtPernyataan.setEditable(false);

		this.spinGapok.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.valueOf(0),
				Double.MAX_VALUE, Double.valueOf(0), 500000));
		this.spinGapok.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.spinGapok.setEditable(true);
		this.spinGapok.valueProperty().addListener((d, old, newValue) -> {
			this.txtValid.setSelected(false);

			StringBuilder sb = new StringBuilder();
			sb.append("Gaji pokok");
			sb.append(" sebesar ");
			sb.append(stringFormater.getCurrencyFormate(spinGapok.getValueFactory().getValue()));
			sb.append(" untuk nama jabatan ");
			sb.append(txtNama.getText());
			txtPernyataan.setText(sb.toString());
		});

		initValidator();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;

	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/jabatan/Form.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {
		setUpdate(false);
		this.txtKode.setEditable(true);
		this.jabatan = new DataJabatan();
	}

	public void initConstuct(DataJabatan j) {
		setUpdate(true);
		this.jabatan = j;
		this.txtKode.setEditable(false);
		txtKode.setText(j.getKodeJabatan());
		txtNama.setText(j.getNama());
		txtKeterangan.setText(j.getKeterangan());
		spinGapok.getValueFactory().setValue(j.getGapok());
	}

	private void newDataJabatan() {
		try {
			jabatan.setKodeJabatan(txtKode.getText());
			jabatan.setNama(txtNama.getText());
			jabatan.setKeterangan(txtKeterangan.getText());
			jabatan.setGapok(spinGapok.getValueFactory().getValue());
			repo.save(jabatan);

			// pesan simpan
			StringBuilder sb = new StringBuilder("Kode jabatan ");
			sb.append(jabatan.getKodeJabatan());
			sb.append(", Berhasil disimpan");

			// menampilkan notifikasi
			Notifications.create().title("Data jabatan").text(sb.toString()).hideAfter(Duration.seconds(3D))
					.position(Pos.BOTTOM_RIGHT).showInformation();

			// log ke console
			logger.info("Berhasil menyimpan data jabatan");

			homeController.showDaftarJabatan();
		} catch (Exception e) {
			logger.error("Tidak dapat menyimpan data jabatan dengan nama: {}", jabatan.getNama());

			StringBuilder sb = new StringBuilder("Tidak dapat menyimpan data jabatan dengan kode ");
			sb.append(jabatan.getKodeJabatan());

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data jabatan");
			ex.setHeaderText(sb.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}

	}

	private void existJabatan() {
		try {
			jabatan.setKodeJabatan(txtKode.getText());
			jabatan.setNama(txtNama.getText());
			jabatan.setKeterangan(txtKeterangan.getText());
			jabatan.setGapok(spinGapok.getValueFactory().getValue());
			repo.save(jabatan);

			// pesan simpan
			StringBuilder sb = new StringBuilder("Kode jabatan ");
			sb.append(jabatan.getKodeJabatan());
			sb.append(", Berhasil diperbaharui");

			// menampilkan notifikasi
			Notifications.create().title("Data jabatan").text(sb.toString()).hideAfter(Duration.seconds(3D))
					.position(Pos.BOTTOM_RIGHT).showInformation();

			// log ke console
			logger.info("Berhasil menyimpan data jabatan");

			homeController.showDaftarJabatan();
		} catch (Exception e) {
			logger.error("Tidak dapat melakukan perubahan data jabatan dengan nama: {}", jabatan.getKodeJabatan());

			StringBuilder sb = new StringBuilder("Tidak dapat melakukan perubahan data jabatan dengan kode ");
			sb.append(jabatan.getKodeJabatan());

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data jabatan");
			ex.setHeaderText(sb.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}
	}

	@FXML
	public void doSave(ActionEvent event) {
		if (isUpdate()) {
			existJabatan();
		} else {
			newDataJabatan();
		}
	}

	@FXML
	public void doCancel(ActionEvent event) {
		homeController.showDaftarJabatan();
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {

	}

	@Override
	public void initValidator() {
		this.validation = new ValidationSupport();
		this.validation.registerValidator(txtKode, (Control c, String value) -> ValidationResult.fromErrorIf(c,
				"Format kode jabatan tidak sesuai, hanya terdiri dari 3 angka", !value.matches("[\\d]{3}")));
		this.validation.registerValidator(txtNama,
				Validator.createEmptyValidator("Nama jabatan tidak boleh kosong!", Severity.ERROR));
		this.validation.registerValidator(txtKeterangan, (Control c, String value) -> ValidationResult.fromMessageIf(c,
				"Keterangan masih kosong!", Severity.WARNING, value.isEmpty()));
		this.validation.registerValidator(spinGapok.getEditor(),
				(Control c, String value) -> ValidationResult.fromErrorIf(c,
						"Nominal minimal " + stringFormater.getCurrencyFormate(500000),
						Double.valueOf(value) < 500000));
		this.validation.registerValidator(txtValid, (Control c, Boolean value) -> ValidationResult.fromErrorIf(c,
				"Anda belum menyetujui data diatas", !value));
		this.validation.invalidProperty()
				.addListener((ObservableValue<? extends Boolean> values, Boolean oldValue, Boolean newValue) -> {
					this.btnSave.setDisable(newValue);
				});
	}

	@Override
	public void initIcons() {

	}

}
