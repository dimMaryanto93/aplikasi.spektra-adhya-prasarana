package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
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
import app.entities.kepegawaian.PengajuanKasbon;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKaryawan;
import app.repositories.RepositoryPengajuanKasbonKaryawan;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

@Component
public class KasbonKaryawanPengajuanController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ApplicationContext springContext;
	private ValidationSupport validation;

	@Autowired
	private StringFormatterFactory stringFormater;

	@Autowired
	private RepositoryKaryawan repoKaryawan;

	@Autowired
	private RepositoryPengajuanKasbonKaryawan repoPengajuanKasbon;

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
	private TextField txtStatus;
	@FXML
	private TextField txtNominal;
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
		PengajuanKasbon kasbon = karyawan.getPengajuanKasbon();
		if (kasbon != null) {
			if (kasbon.getAccepted()) {
				txtStatus.setText("SUDAH");
			} else {
				txtStatus.setText("SEDANG DIPROSES");
			}
			txtNominal.setText(stringFormater.getCurrencyFormate(kasbon.getNominal()));
		} else {
			txtStatus.setText("BELUM");
			txtNominal.clear();
		}
	}

	private void clearFields() {
		txtNama.clear();
		txtJabatan.clear();
		txtStatus.clear();
		txtNominal.clear();
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
			txtValid.setSelected(false);
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
			}
		});

		this.txtKaryawan.getSelectionModel().selectedItemProperty().addListener((s, old, value) -> {
			txtPinjam.setDisable(value == null);
			doubleSpinnerValueFactory.setMin(0D);
			doubleSpinnerValueFactory.setValue(0D);
			if (value != null) {
				DataKaryawan karyawan = mapDataKaryawan.get(value);
				setFields(karyawan);
				doubleSpinnerValueFactory.setMax(20000000);
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

	}

	@Override
	public void initConstuct() {
		try {
			this.mapDataKaryawan = new HashMap<String, DataKaryawan>();
			this.txtKaryawan.getItems().clear();
			for (DataKaryawan karyawan : this.repoKaryawan
					.findByPengajuanKasbonIsNullOrPengajuanKasbonAccepted(false)) {
				mapDataKaryawan.put(karyawan.getNip(), karyawan);
				this.txtKaryawan.getItems().add(karyawan.getNip());
			}
			this.pengajuanKasbon = new PengajuanKasbon();
			this.pengajuanKasbon.setTanggal(Date.valueOf(LocalDate.now()));
		} catch (Exception e) {
			logger.error("Tidak dapat memuat data karyawan yang bisa melakukan pengajuan kasbon", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data pengajuan kasbon karyawan");
			ex.setHeaderText("Tidak dapat mendapatkan daftar data karyawan yang dapat melakukan pengajuan kasbon");
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}
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
		this.validation.registerValidator(txtPinjam.getEditor(),
				(Control c, String value) -> ValidationResult.fromErrorIf(c,
						"Minimal peminjaman lebih dari Rp50.000,00 dan maksimal Rp20.000.000,00",
						Double.valueOf(value) < Double.valueOf(50000)
								|| Double.valueOf(value) > Double.valueOf(20000000)));

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
			PengajuanKasbon oldKasbon = karyawan.getPengajuanKasbon();
			if (oldKasbon != null) {
				karyawan.setPengajuanKasbon(null);
				this.repoKaryawan.save(karyawan);
				this.repoPengajuanKasbon.delete(oldKasbon.getId());
			}

			this.pengajuanKasbon.setNominal(txtPinjam.getValueFactory().getValue());
			this.pengajuanKasbon.setTanggal(Date.valueOf(txtTanggal.getValue()));
			this.repoPengajuanKasbon.save(this.pengajuanKasbon);

			karyawan.setPengajuanKasbon(this.repoPengajuanKasbon.findOne(this.pengajuanKasbon.getId()));
			this.repoKaryawan.save(karyawan);

			StringBuilder saveMessage = new StringBuilder("Pengajuan kasbon karyawan atas nama ");
			saveMessage.append(karyawan.getNama()).append(" dengan NIP ").append(karyawan.getNip());
			saveMessage.append(" sebesar ").append(stringFormater.getCurrencyFormate(pengajuanKasbon.getNominal()))
					.append(", Berhasil disimpan");
			Notifications.create().title("Data pencairan kasbon karyawan").text(saveMessage.toString())
					.position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(4D)).showInformation();

			initConstuct();
		} catch (Exception e) {
			logger.error("Tidak bisa menyimpan data pengajuan karyawan atas nama karyawan {} sebesar {}",
					karyawan.getNama(), stringFormater.getCurrencyFormate(txtPinjam.getValueFactory().getValue()), e);

			StringBuilder errorMessage = new StringBuilder(
					"Tidak dapat menyimpan pengajuan kasbon karyawan atas nama ");
			errorMessage.append(karyawan.getNama()).append(" dengan NIP ").append(karyawan.getNip()).append(" sebesar ")
					.append(stringFormater.getCurrencyFormate(pengajuanKasbon.getNominal()));
			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data pengajuan kasbon karyawan");
			ex.setHeaderText(errorMessage.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}
	}

	@Override
	public void initIcons() {
		// TODO Auto-generated method stub
		
	}

}
