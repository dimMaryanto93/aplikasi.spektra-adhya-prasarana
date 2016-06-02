package app.controller.penggajian;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.HashMap;
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
import app.entities.kepegawaian.Penggajian;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.PenggajianService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
import javafx.scene.control.CheckBox;

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

	private HashMap<String, DataKaryawan> mapKaryawan;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
