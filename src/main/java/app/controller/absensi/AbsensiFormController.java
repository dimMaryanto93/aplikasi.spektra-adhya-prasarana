package app.controller.absensi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.DialogsFX;
import app.entities.kepegawaian.KehadiranKaryawan;
import app.entities.master.DataKaryawan;
import app.entities.master.DataTidakHadir;
import app.repositories.AbsensiService;
import app.repositories.KaryawanService;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.stage.Stage;

@Component
public class AbsensiFormController implements BootInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private KaryawanService karyawanRepository;
	@Autowired
	private AbsensiService absensiRepository;

	private ApplicationContext springContext;

	@FXML
	private Label txtTanggal;
	@FXML
	private TableView<KehadiranKaryawan> tableView;
	@FXML
	private TableColumn<KehadiranKaryawan, Integer> columnNik;
	@FXML
	private TableColumn<KehadiranKaryawan, String> columnNama;
	@FXML
	private TableColumn<KehadiranKaryawan, Boolean> columnAbsen;

	private DialogsFX notif;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableView.setSelectionModel(null);
		columnNik.setCellValueFactory(v -> {
			DataKaryawan e = v.getValue().getKaryawan();

			if (e != null) {
				return new SimpleObjectProperty<Integer>(e.getNik());
			} else {
				return new SimpleObjectProperty<Integer>(0);
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
		loader.setLocation(getClass().getResource("/scenes/inner/absen/form.fxml"));
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
			notif.showDefaultErrorLoad("Data absensi karyawan", e);
		}
	}

	@FXML
	public void doSave(ActionEvent event) {
		try {
			absensiRepository.save(tableView.getItems());
			notif.showDefaultSave("Data absensi karyawan");

			logger.info("Data absensi berhasil disimpan atau diperbaharui");
			initConstuct();
		} catch (Exception e) {
			logger.error("Tidak dapat mengimpan dan perubahan data absensi karyawan", e);
			notif.showDefaultErrorSave("Data absensi karyawan", e);
		}
	}

	@FXML
	public void doCancel(ActionEvent event) {
		initConstuct();
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
			// TODO Auto-generated method stub
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
	@Autowired
	public void setNotificationDialog(DialogsFX notif) {
		this.notif = notif;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		// TODO Auto-generated method stub

	}

}
