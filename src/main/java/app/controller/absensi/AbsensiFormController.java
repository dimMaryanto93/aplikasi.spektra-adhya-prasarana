package app.controller.absensi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.StringFormatterFactory;
import app.controller.HomeController;
import app.entities.kepegawaian.KehadiranKaryawan;
import app.entities.master.DataKaryawan;
import app.entities.master.DataTidakHadir;
import app.repositories.RepositoryAbsensi;
import app.repositories.RepositoryKaryawan;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class AbsensiFormController implements BootInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RepositoryKaryawan karyawanRepository;
	@Autowired
	private RepositoryAbsensi absensiRepository;
	@Autowired
	private HomeController homeController;

	private ApplicationContext springContext;

	@FXML
	private Label txtTanggal;
	@FXML
	private TableView<KehadiranKaryawan> tableView;
	@FXML
	private TableColumn<KehadiranKaryawan, String> columnNik;
	@FXML
	private TableColumn<KehadiranKaryawan, String> columnNama;
	@FXML
	private TableColumn<KehadiranKaryawan, Boolean> columnAbsen;

	@Autowired
	private StringFormatterFactory stringFormat;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableView.setSelectionModel(null);
		columnNik.setCellValueFactory(v -> {
			DataKaryawan e = v.getValue().getKaryawan();

			if (e != null) {
				return new SimpleStringProperty(e.getNip());
			} else {
				return new SimpleStringProperty();
			}
		});

		columnNama.setCellValueFactory(v -> {
			DataKaryawan e = v.getValue().getKaryawan();
			if (e != null) {
				return new SimpleStringProperty(e.getNama());
			} else {
				return new SimpleStringProperty();
			}
		});

		columnAbsen.setCellFactory(v -> new CheckboxInTableView(tableView.getItems()));

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/absen/Form.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {
		try {
			txtTanggal.setText(LocalDate.now().toString());
			tableView.getItems().clear();
			for (DataKaryawan karyawan : karyawanRepository.findAll()) {

				KehadiranKaryawan absen;
				absen = absensiRepository.findByKaryawanAndTanggalHadir(karyawan, Date.valueOf(LocalDate.now()));
				if (absen == null) {
					logger.info("karyawan {} belum hadir", karyawan.getNama());
					absen = new KehadiranKaryawan();
					absen.setHadir(false);
					absen.setLembur(false);
					absen.setKaryawan(karyawan);
					absen.setTanggalHadir(Date.valueOf(LocalDate.now()));
				}
				tableView.getItems().add(absen);
			}

		} catch (Exception e) {
			logger.error("Tidak dapat memuat data absensi yang dicari berdasarkan karyawan dan tanggal", e);

			// menampmilkan pesan
			StringBuilder sb = new StringBuilder(
					"Tidak dapat mendapatkan daftar karyawan untuk melakukan absensi pada tanggal ");
			sb.append(stringFormat.getDateTimeFormatterWithDayAndDateMonthYear(LocalDate.now()));

			// menampilkan dialog exception
			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar karyawan");
			ex.setHeaderText(sb.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}
	}

	@FXML
	public void doSave(ActionEvent event) {
		try {
			absensiRepository.save(tableView.getItems());

			// tampilkan notifikasi berhasil disimpan
			logger.info("Data absensi berhasil disimpan atau diperbaharui");
			Notifications.create().title("Data absen")
					.text("Daftar absensi karyawan pada tanggal " + LocalDate.now().toString() + ", Berhasil disimpan!")
					.showInformation();

			// kemabalikan atau refresh data setelah disimpan
			initConstuct();
		} catch (Exception e) {
			logger.error("Tidak dapat mengimpan dan perubahan data absensi karyawan", e);

			StringBuilder sb = new StringBuilder("Tidak dapat menyimpan perubahan data absensi karyawan pada tanggal ");
			sb.append(stringFormat.getDateTimeFormatterWithDayAndDateMonthYear(LocalDate.now()));

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data absensi karyawan");
			ex.setHeaderText(sb.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}
	}

	@FXML
	public void doCancel(ActionEvent event) {
		this.homeController.showWellcome();
	}

	public class CheckboxInTableView extends TableCell<KehadiranKaryawan, Boolean> {

		private CheckBox hadir;
		private CheckBox lembur;
		private ObservableList<KehadiranKaryawan> daftarAbsen;
		private ChoiceBox<DataTidakHadir> keterangan;

		public CheckboxInTableView(ObservableList<KehadiranKaryawan> items) {
			this.daftarAbsen = items;
		}

		@Override
		protected void updateItem(Boolean item, boolean empty) {
			super.updateItem(item, empty);
			if (!empty) {
				KehadiranKaryawan absen = daftarAbsen.get(getIndex());

				this.hadir = new CheckBox("Kehadiran");
				this.lembur = new CheckBox("Lembur");
				this.keterangan = new ChoiceBox<DataTidakHadir>();
				this.keterangan.getItems().addAll(DataTidakHadir.values());

				if (!absen.getHadir()) {
					this.lembur.setDisable(true);
				} else {
					this.keterangan.setDisable(false);
				}

				this.keterangan.getSelectionModel().selectedItemProperty()
						.addListener((ObservableValue<? extends DataTidakHadir> values, DataTidakHadir oldValue,
								DataTidakHadir newValue) -> {
							absen.setKet(newValue);
						});
				this.hadir.selectedProperty().addListener(
						(ObservableValue<? extends Boolean> values, Boolean oldValue, Boolean newValue) -> {
							lembur.setDisable(!newValue);
							keterangan.setDisable(newValue);

							absen.setHadir(newValue);

							if (newValue) {
								this.keterangan.getSelectionModel().clearSelection();
								logger.info("karyawan atas nama {} hadir", absen.getKaryawan().getNama());
							} else {
								this.lembur.setSelected(false);
								logger.warn("karyawan atas nama {} tidak hadir", absen.getKaryawan().getNama());
							}

						});

				this.lembur.selectedProperty().addListener(
						(ObservableValue<? extends Boolean> values, Boolean oldValue, Boolean newValue) -> {
							absen.setLembur(newValue);

							if (!this.hadir.isSelected()) {
								this.lembur.setDisable(true);
							}

							if (newValue) {
								logger.info("karyawan atas nama {} akan lembur!", absen.getKaryawan().getNama());
							}
						});
				this.hadir.setSelected(absen.getHadir());
				this.lembur.setSelected(absen.getLembur());
				this.keterangan.getSelectionModel().select(absen.getKet());
				HBox box = new HBox();
				box.setSpacing(10);
				box.getChildren().add(hadir);
				box.getChildren().add(lembur);
				box.getChildren().add(new Separator(Orientation.VERTICAL));
				box.getChildren().add(keterangan);
				setGraphic(box);
			} else {
				setGraphic(null);
			}
		}
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {

	}

	@Override
	public void initIcons() {
		// TODO Auto-generated method stub

	}

}
