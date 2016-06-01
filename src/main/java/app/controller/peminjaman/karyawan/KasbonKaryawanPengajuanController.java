package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
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
import app.configs.FormatterFactory;
import app.entities.kepegawaian.PengajuanKasbon;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.PengajuanKasbonService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class KasbonKaryawanPengajuanController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private DialogsFX notif;
	private ApplicationContext springContext;
	private ValidationSupport validation;

	@Autowired
	private FormatterFactory stringFormater;

	@Autowired
	private KaryawanService serviceKaryawan;

	@Autowired
	private PengajuanKasbonService servicePengajuan;

	@FXML
	private DatePicker txtTanggal;
	@FXML
	private Spinner<Double> txtPinjam;
	@FXML
	private ComboBox<String> txtKaryawan;
	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtJabatan;
	@FXML
	private Button btnSimpan;
	@FXML
	private CheckBox txtValid;

	private PengajuanKasbon pengajuanKasbon;

	private HashMap<String, DataKaryawan> mapDataKaryawan;

	private SpinnerValueFactory.DoubleSpinnerValueFactory doubleSpinnerValueFactory;

	private void setFields(DataKaryawan karyawan) {
		txtNama.setText(karyawan.getNama());
		txtJabatan.setText(karyawan.getJabatan().getNama());
	}

	private void clearFields() {
		txtNama.clear();
		txtJabatan.clear();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.txtValid.setDisable(true);

		this.txtTanggal.setValue(LocalDate.now());
		this.doubleSpinnerValueFactory = new DoubleSpinnerValueFactory(0D, 0D, 0D, 0D);

		this.txtPinjam.setValueFactory(this.doubleSpinnerValueFactory);
		this.txtPinjam.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtPinjam.setEditable(true);
		this.txtPinjam.setDisable(true);
		this.txtPinjam.valueProperty().addListener((d, old, value) -> {
			txtValid.setDisable(value <= 0D);
			if (value > 0D) {
				String namaKaryawan = mapDataKaryawan.get(txtKaryawan.getSelectionModel().getSelectedItem()).getNama();
				StringBuilder sb = new StringBuilder(
						"Dengan ini, saya menyatakan permohonan kasbon karyawan atas nama ").append(namaKaryawan)
								.append(" sebesar ")
								.append(stringFormater.getCurrencyFormate(txtPinjam.getValueFactory().getValue()));
				txtValid.setText(sb.toString());
				txtValid.setOpacity(1D);
			} else {
				txtValid.setText("");
				txtValid.setOpacity(0D);
				txtValid.setSelected(false);
			}
		});

		this.txtKaryawan.getSelectionModel().selectedItemProperty().addListener((s, old, value) -> {
			txtPinjam.setDisable(value == null);
			doubleSpinnerValueFactory.setMin(0D);
			doubleSpinnerValueFactory.setValue(0D);
			if (value != null) {
				DataKaryawan karyawan = mapDataKaryawan.get(value);
				setFields(karyawan);
				doubleSpinnerValueFactory.setMax(Double.MAX_VALUE);
				doubleSpinnerValueFactory.setAmountToStepBy(50000);

			} else {
				clearFields();
				doubleSpinnerValueFactory.setMax(0D);
				doubleSpinnerValueFactory.setAmountToStepBy(0D);
			}
		});
		this.txtKaryawan.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				return new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						// TODO Auto-generated method stub
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							DataKaryawan karyawan = mapDataKaryawan.get(item);
							StringBuilder sb = new StringBuilder(karyawan.getNip()).append(" (")
									.append(karyawan.getNama()).append(" bagian ")
									.append(karyawan.getJabatan().getNama()).append(")");
							setText(sb.toString());
						}
					}
				};
			}
		});

		initValidator();
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/peminjaman/karyawan/Pengajuan.fxml"));
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
			this.txtKaryawan.getItems().clear();
			this.mapDataKaryawan = new HashMap<String, DataKaryawan>();
			for (DataKaryawan karyawan : this.serviceKaryawan.findByPengajuanKasbonIsNull()) {
				mapDataKaryawan.put(karyawan.getNip(), karyawan);
				this.txtKaryawan.getItems().add(karyawan.getNip());
			}
			this.pengajuanKasbon = new PengajuanKasbon();
			this.pengajuanKasbon.setTanggal(Date.valueOf(LocalDate.now()));
		} catch (Exception e) {
			logger.error("Tidak dapat memuat data karyawan yang bisa melakukan pengajuan kasbon", e);
			notif.showDefaultErrorLoad("Data Pengajuan Kasbon Karyawan", e);
		}
	}

	@Override
	@Autowired
	public void setNotificationDialog(DialogsFX notif) {
		this.notif = notif;
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
		this.validation.registerValidator(txtTanggal,
				(Control c, LocalDate value) -> ValidationResult.fromErrorIf(c,
						"Tanggal tidak boleh lebih dari tanggal "
								+ stringFormater.getDateIndonesianFormatter(LocalDate.now()),
						value.isBefore(LocalDate.now())));
		this.validation.registerValidator(txtKaryawan,
				Validator.createEmptyValidator("No induk karyawan belum pilih!", Severity.ERROR));
		this.validation.registerValidator(txtValid, (Control c, Boolean value) -> ValidationResult.fromErrorIf(c,
				"Anda belum menyutujui perjanjain!", !value));

		this.validation.invalidProperty().addListener((b, old, value) -> {
			btnSimpan.setDisable(value);
		});
	}

	@FXML
	public void doBack(ActionEvent event) {
	}

	@FXML
	public void doSave(ActionEvent event) {
		DataKaryawan karyawan = mapDataKaryawan.get(txtKaryawan.getValue());
		try {
			this.pengajuanKasbon.setNominal(txtPinjam.getValueFactory().getValue());
			this.pengajuanKasbon.setTanggal(Date.valueOf(txtTanggal.getValue()));
			this.servicePengajuan.save(this.pengajuanKasbon);

			karyawan.setPengajuanKasbon(this.servicePengajuan.findOne(this.pengajuanKasbon.getId()));
			this.serviceKaryawan.save(karyawan);

			notif.showDefaultSave("Data pengajuan kasbon karyawan");
			initConstuct();
		} catch (Exception e) {
			logger.error("Tidak bisa menyimpan data pengajuan karyawan atas nama karyawan {} sebesar {}",
					karyawan.getNama(), stringFormater.getCurrencyFormate(txtPinjam.getValueFactory().getValue()));
			notif.showDefaultErrorSave("Data pengajuan kasbon karyawan", e);
		}
	}

}
