package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.validation.ValidationSupport;
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
import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.kepegawaian.PengajuanKasbon;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.PengajuanKasbonService;
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
public class KasbonKaryawanPencairanDanaController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private DialogsFX notif;
	private ApplicationContext springContext;
	private ValidationSupport validation;

	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, String> columnNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;
	@FXML
	private TextField txtNip;
	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtTanggalPengajuan;
	@FXML
	private TextField txtNominalPengajuan;
	@FXML
	private CheckBox checkValid;
	@FXML
	private Button btnSave;

	@Autowired
	private KaryawanService serviceKaryawan;

	@Autowired
	private PengajuanKasbonService servicePengajuanKasbon;

	@Autowired
	private FormatterFactory stringFormatter;

	private KasbonKaryawan kasbon;

	private void setFields(DataKaryawan karyawan) {
		txtNip.setText(karyawan.getNip());
		txtNama.setText(karyawan.getNama());

		PengajuanKasbon pengajuan = karyawan.getPengajuanKasbon();
		txtTanggalPengajuan.setText(stringFormatter.getDateIndonesianFormatter(pengajuan.getTanggal().toLocalDate()));
		txtNominalPengajuan.setText(stringFormatter.getCurrencyFormate(pengajuan.getNominal()));
		this.checkValid.setOpacity(1D);

		StringBuilder sb = new StringBuilder("Dengan ini, saya setuju memberikan pinjaman dana sebesar ")
				.append(stringFormatter.getCurrencyFormate(pengajuan.getNominal()));
		this.checkValid.setText(sb.toString());
	}

	private void cleanFields() {
		txtNip.clear();
		txtNama.clear();
		txtTanggalPengajuan.clear();
		txtNominalPengajuan.clear();

		checkValid.setSelected(false);
		this.checkValid.setOpacity(0D);
		this.checkValid.setText("");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnSave.setDisable(false);

		columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nik"));
		columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
		tableView.getSelectionModel().selectedItemProperty().addListener((d, old, value) -> {
			if (value != null) {
				setFields(value);
			} else {
				cleanFields();
			}
		});
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/peminjaman/karyawan/PencairanDana.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {
		try {
			tableView.getItems().clear();
			tableView.getItems()
					.addAll(this.serviceKaryawan.findByPengajuanKasbonIsNotNullAndPengajuanKasbonAccepted(true));
		} catch (Exception e) {
			logger.error("Tidak dapat memuat data karyawan", e);
			notif.showDefaultErrorLoad("Data Karyawan", e);
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
	}

	@FXML
	public void doSave(ActionEvent event) {
		try {
			DataKaryawan dataKaryawan = tableView.getSelectionModel().getSelectedItem();
			if (dataKaryawan != null) {
				this.kasbon = new KasbonKaryawan();

				kasbon.setKaryawan(dataKaryawan);

				PengajuanKasbon pengajuan = dataKaryawan.getPengajuanKasbon();

				kasbon.setTanggalPinjam(pengajuan.getTanggal());
				kasbon.setPinjaman(pengajuan.getNominal());
				kasbon.setPembayaran(0D);

				kasbon.setSaldoTerakhir(dataKaryawan.getTotalSaldoTerakhir() + kasbon.getPinjaman());

				dataKaryawan.setPengajuanKasbon(null);
				dataKaryawan.getDaftarKasbon().add(kasbon);

				serviceKaryawan.save(dataKaryawan);

				servicePengajuanKasbon.delete(pengajuan);

				notif.showDefaultSave("Data peminjaman karyawan");
				initConstuct();
			} else {
				logger.warn("Data karyawan belum diseleksi pada tabel view");
				notif.setTitle("Tabel data karyawan");
				notif.setText("Karyawan belum dipilih!");
				notif.setHeader("Tabel Data Karyawan");
				notif.showDialogInformation(notif.getTitle(), notif.getHeader(), notif.getText());
			}
		} catch (Exception e1) {
			logger.error("Tidak dapat mengimpan dan melakukan perubahan data peminjaman karyawan", e1);
			notif.showDefaultErrorSave("Data peminjaman karyawan", e1);
		}
	}

	@FXML
	public void doBack(ActionEvent event) {
	}

}
