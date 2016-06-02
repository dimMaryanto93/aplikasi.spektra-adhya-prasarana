package app.controller.penggajian;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

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
import app.entities.kepegawaian.KehadiranKaryawan;
import app.entities.kepegawaian.Penggajian;
import app.entities.master.DataJabatan;
import app.entities.master.DataKaryawan;
import app.repositories.AbsensiService;
import app.repositories.KaryawanService;
import app.repositories.PenggajianService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class PenggajianKaryawanPencairanDanaController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private DialogsFX notif;
	private ApplicationContext springContext;

	@FXML
	private Button btnSave;
	@FXML
	private ComboBox<String> txtNip;
	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtJabatan;
	@FXML
	private TextField txtJenisKelamin;
	@FXML
	private TextField txtGajiPokok;
	@FXML
	private TextField txtTotalKehadiran;
	@FXML
	private Spinner<Double> txtBonusKehadiran;
	@FXML
	private TextField txtJumlahKehadiran;
	@FXML
	private TextField txtJumlahLembur;
	@FXML
	private Spinner<Double> txtBonusLembur;
	@FXML
	private TextField txtTotalLembur;
	@FXML
	private TextField txtCicilanKe;
	@FXML
	private TextField txtMerekMotor;
	@FXML
	private TextField txtUangPrestasi;
	@FXML
	private TextField txtTotal;
	@FXML
	private CheckBox checkValid;

	@Autowired
	private PenggajianService servicePenggajian;

	@Autowired
	private KaryawanService serviceKaryawan;

	@Autowired
	private FormatterFactory stringFormatter;

	@Autowired
	private AbsensiService serviceAbsen;

	private HashMap<String, DataKaryawan> mapKaryawan;
	private Penggajian penggajian;
	private List<KehadiranKaryawan> listTransport = new ArrayList<KehadiranKaryawan>();
	private List<KehadiranKaryawan> listLembur = new ArrayList<KehadiranKaryawan>();
	private SpinnerValueFactory.DoubleSpinnerValueFactory kehadiranValueFactory, lemburValueFactory;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.kehadiranValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, 0D, 0D, 0D);
		this.txtBonusKehadiran.setDisable(true);
		this.txtBonusKehadiran.setValueFactory(kehadiranValueFactory);
		this.txtBonusKehadiran.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtBonusKehadiran.setEditable(true);
		this.txtBonusKehadiran.getValueFactory().valueProperty().addListener((d, old, value) -> {
			this.penggajian.setUangTransport(listTransport.size() * value);
			txtTotalKehadiran.setText(stringFormatter.getCurrencyFormate(this.penggajian.getUangTransport()));
		});

		this.lemburValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, 0D, 0D, 0D);
		this.txtBonusLembur.setDisable(true);
		this.txtBonusLembur.setValueFactory(lemburValueFactory);
		this.txtBonusLembur.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.txtBonusLembur.setEditable(true);
		this.txtBonusLembur.getValueFactory().valueProperty().addListener((d, old, value) -> {
			this.penggajian.setUangLembur(listLembur.size() * value);
			txtTotalLembur.setText(stringFormatter.getCurrencyFormate(this.penggajian.getUangLembur()));
		});

		txtNip.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				return new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							DataKaryawan karyawan = mapKaryawan.get(item);
							StringBuilder sb = new StringBuilder(karyawan.getNip()).append(" (")
									.append(karyawan.getNama()).append(" bagian ")
									.append(karyawan.getJabatan().getNama()).append(")");
							setText(sb.toString());
						}
					}
				};
			}
		});
		txtNip.getSelectionModel().selectedItemProperty().addListener((s, old, value) -> {
			txtBonusKehadiran.setDisable(value == null);
			txtBonusLembur.setDisable(value == null);

			if (value != null) {
				setFields(mapKaryawan.get(value));

				this.kehadiranValueFactory.setMin(0D);
				this.kehadiranValueFactory.setMax(100000);
				this.kehadiranValueFactory.setAmountToStepBy(5000);
				this.kehadiranValueFactory.setValue(30000D);

				this.lemburValueFactory.setMin(0D);
				this.lemburValueFactory.setMax(100000);
				this.lemburValueFactory.setAmountToStepBy(5000);
				this.lemburValueFactory.setValue(30000D);
			} else {
				clearFields();

				this.kehadiranValueFactory.setMin(0D);
				this.kehadiranValueFactory.setMax(0D);
				this.kehadiranValueFactory.setAmountToStepBy(0D);
				this.kehadiranValueFactory.setValue(0D);

				this.lemburValueFactory.setMin(0D);
				this.lemburValueFactory.setMax(0D);
				this.lemburValueFactory.setAmountToStepBy(0D);
				this.lemburValueFactory.setValue(0D);
			}
		});

	}

	private void setFields(DataKaryawan karyawan) {
		LocalDate sekarang = LocalDate.now();
		LocalDate awalBulan = sekarang.withDayOfMonth(1);
		LocalDate akhirBulan = sekarang.withDayOfMonth(sekarang.lengthOfMonth());

		DataJabatan jabatan = karyawan.getJabatan();

		txtNama.setText(karyawan.getNama());
		txtJabatan.setText(jabatan.getNama());
		txtJenisKelamin.setText(karyawan.getJenisKelamin().toString());

		this.penggajian.setGajiPokok(karyawan.getGajiPokok());
		txtGajiPokok.setText(stringFormatter.getCurrencyFormate(karyawan.getGajiPokok()));

		try {
			this.listTransport.clear();
			for (KehadiranKaryawan hadir : serviceAbsen.findByKaryawanAndTanggalHadirBetweenAndHadir(karyawan,
					Date.valueOf(awalBulan), Date.valueOf(akhirBulan), true)) {
				listTransport.add(hadir);
			}
			txtJumlahKehadiran.setText(stringFormatter.getNumberIntegerOnlyFormate(listTransport.size()));
		} catch (Exception e) {
			logger.error("Tidak dapat mendapatkan data absensi karyawan atas nama {}", karyawan.getNama(), e);
		}

		try {
			this.listLembur.clear();
			for (KehadiranKaryawan lembur : serviceAbsen.findByKaryawanAndTanggalHadirBetweenAndLembur(karyawan,
					Date.valueOf(awalBulan), Date.valueOf(akhirBulan), true)) {
				this.listLembur.add(lembur);
			}
			txtJumlahLembur.setText(stringFormatter.getNumberIntegerOnlyFormate(listLembur.size()));
		} catch (Exception e) {
			logger.error("Tidak dapat mendapatkan data lembur karyawan atas nama {}", karyawan.getNama(), e);
		}
	}

	private void clearFields() {
		txtNama.clear();
		txtJabatan.clear();
		txtJenisKelamin.clear();

		txtGajiPokok.clear();
		txtJumlahKehadiran.clear();
		txtJumlahLembur.clear();
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/penggajian/PencairanDana.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {
		try {
			this.penggajian = new Penggajian();
			this.penggajian.setTanggal(Date.valueOf(LocalDate.now()));

			this.mapKaryawan = new HashMap<String, DataKaryawan>();
			txtNip.getItems().clear();

			LocalDate sekarang = LocalDate.now();
			LocalDate awalBulan = sekarang.withDayOfMonth(1);
			LocalDate akhirBulan = sekarang.withDayOfMonth(sekarang.lengthOfMonth());

			logger.info("{} s/d {}", awalBulan.toString(), akhirBulan.toString());

			for (DataKaryawan karyawan : serviceKaryawan.findAll()) {
				Penggajian gaji = servicePenggajian.findByKaryawanAndTanggalBetween(karyawan, Date.valueOf(awalBulan),
						Date.valueOf(akhirBulan));
				if (gaji == null) {
					logger.info("Karyawan atas nama {} belum menerima gaji pada {}", karyawan.getNama(),
							sekarang.toString());

					this.mapKaryawan.put(karyawan.getNip(), karyawan);
					this.txtNip.getItems().add(karyawan.getNip());
				} else {
					logger.warn("Karyawan atas nama {} telah menerima gaji!", karyawan.getNama());
				}
			}
		} catch (Exception e) {
			logger.error("Tidak dapat mendapatkan data karyawan yang belum menerima gaji pada bulan {}",
					LocalDate.now().toString());
			notif.showDefaultErrorLoad("Data karyawan", e);
		}
	}

	@Override
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

	}

	@FXML
	public void doSave(ActionEvent event) {
	}

	@FXML
	public void doBack(ActionEvent event) {
	}

}
