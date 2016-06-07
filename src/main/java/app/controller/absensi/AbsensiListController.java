package app.controller.absensi;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.controlsfx.dialog.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.DialogsFX;
import app.configs.StringFormatterFactory;
import app.entities.kepegawaian.KehadiranKaryawan;
import app.entities.master.DataKaryawan;
import app.entities.master.DataTidakHadir;
import app.repositories.AbsensiService;
import app.repositories.KaryawanService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;

@Component
public class AbsensiListController implements BootInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AbsensiService absenService;
	@Autowired
	private KaryawanService karyawan;

	private ApplicationContext springContainer;
	@FXML
	private TableView<DataKaryawan> tableKaryawan;
	@FXML
	private TableColumn<DataKaryawan, String> columnkaryawanNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnKaryawanNama;

	@FXML
	private TableView<KehadiranKaryawan> tabelKehadiran;
	@FXML
	private TableColumn<KehadiranKaryawan, String> columnTanggal;
	@FXML
	private TableColumn<KehadiranKaryawan, Boolean> columnHadir;
	@FXML
	private TableColumn<KehadiranKaryawan, Boolean> columnLembur;
	@FXML
	private TableColumn<KehadiranKaryawan, String> columnKeterangan;
	@FXML
	private TextField txtNoInduk;
	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtJabatan;
	@FXML
	private TextField txtHadir;
	@FXML
	private TextField txtLembur;

	@Autowired
	private StringFormatterFactory stringFormatter;

	private void setFields(DataKaryawan karyawan) {
		txtNoInduk.setText(karyawan.getNik().toString());
		txtNama.setText(karyawan.getNama());
		txtJabatan.setText(karyawan.getJabatan().getNama());
		Integer hadir = 0;
		Integer lembur = 0;
		try {
			for (KehadiranKaryawan absen : absenService.findByKaryawan(karyawan)) {
				tabelKehadiran.getItems().add(absen);
				if (absen.getHadir()) {
					hadir += 1;
				}
				if (absen.getLembur()) {
					lembur += 1;
				}
			}
		} catch (Exception e) {
			logger.error("Tidak dapat memuat data absensi karyawan", e);

			StringBuilder sb = new StringBuilder("Tidak dapat mendapatkan data absensi karyawan atas nama ");
			sb.append(karyawan.getNama());
			sb.append(" dengan NIP ");
			sb.append(karyawan.getNip());

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data absensi karyawan");
			ex.setHeaderText(sb.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}

		txtHadir.setText(hadir.toString());
		txtLembur.setText(lembur.toString());
	}

	private void clearFields() {
		txtNoInduk.clear();
		txtNama.clear();
		txtJabatan.clear();
		txtHadir.clear();
		txtLembur.clear();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tabelKehadiran.setSelectionModel(null);
		columnkaryawanNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nik"));
		columnKaryawanNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
		tableKaryawan.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
					tabelKehadiran.getItems().clear();
					if (newValue != null) {
						setFields(newValue);
					} else {
						clearFields();
					}
				});

		columnTanggal.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KehadiranKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KehadiranKaryawan, String> param) {
						LocalDate date = param.getValue().getTanggalHadir().toLocalDate();
						return new SimpleStringProperty(
								stringFormatter.getDateTimeFormatterWithDayAndDateMonthYear(date));
					}
				});
		columnHadir.setCellValueFactory(new PropertyValueFactory<KehadiranKaryawan, Boolean>("hadir"));
		columnHadir.setCellFactory(
				new Callback<TableColumn<KehadiranKaryawan, Boolean>, TableCell<KehadiranKaryawan, Boolean>>() {

					@Override
					public TableCell<KehadiranKaryawan, Boolean> call(TableColumn<KehadiranKaryawan, Boolean> param) {
						return new TableCell<KehadiranKaryawan, Boolean>() {
							CheckBox box;

							@Override
							protected void updateItem(Boolean item, boolean empty) {
								setAlignment(Pos.CENTER);
								super.updateItem(item, empty);
								if (!empty) {
									box = new CheckBox();
									box.setDisable(true);
									box.setOpacity(1.0);
									box.setSelected(item);
									setGraphic(box);
								} else {
									setGraphic(null);
								}
							}
						};
					}
				});
		columnLembur.setCellValueFactory(new PropertyValueFactory<KehadiranKaryawan, Boolean>("lembur"));
		columnLembur.setCellFactory(
				new Callback<TableColumn<KehadiranKaryawan, Boolean>, TableCell<KehadiranKaryawan, Boolean>>() {

					@Override
					public TableCell<KehadiranKaryawan, Boolean> call(TableColumn<KehadiranKaryawan, Boolean> param) {
						return new TableCell<KehadiranKaryawan, Boolean>() {
							CheckBox box;

							@Override
							protected void updateItem(Boolean item, boolean empty) {
								setAlignment(Pos.CENTER);
								super.updateItem(item, empty);
								if (!empty) {
									box = new CheckBox();
									box.setDisable(true);
									box.setOpacity(1.0);
									box.setSelected(item);
									setGraphic(box);
								} else {
									setGraphic(null);
								}
							}
						};
					}
				});

		columnKeterangan.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KehadiranKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KehadiranKaryawan, String> param) {
						DataTidakHadir gakHadir = param.getValue().getKet();
						if (gakHadir != null) {
							return new SimpleStringProperty(gakHadir.toString());
						} else {
							if (param.getValue().getHadir()) {
								return new SimpleStringProperty("-");
							} else {
								return new SimpleStringProperty("Tanpa Keterangan!");
							}
						}
					}
				});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContainer = applicationContext;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/absen/List.fxml"));
		loader.setController(springContainer.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
	}

	private void loadDataKaryawan() {
		try {
			tableKaryawan.getItems().clear();
			tableKaryawan.getItems().addAll(karyawan.findAll());
		} catch (Exception e) {
			logger.error("Tidak dapat memuat data karyawan", e);

			StringBuilder sb = new StringBuilder("Tidak dapat mendapatkan daftar data karyawan!");

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data Karyawan");
			ex.setHeaderText(sb.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}
	}

	@Override
	public void initConstuct() {
		loadDataKaryawan();
	}

	@Override
	@Autowired
	public void setNotificationDialog(DialogsFX notif) {
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {

	}

	@FXML
	public void doClear(ActionEvent event) {
		tableKaryawan.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefresh(ActionEvent event) {
		initConstuct();
	}

}
