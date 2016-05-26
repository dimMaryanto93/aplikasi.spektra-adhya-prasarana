package app.controller.absensi;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.controlsfx.validation.ValidationSupport;
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
import javafx.scene.shape.Box;
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

	private void setFields(DataKaryawan karyawan) {
		txtNoInduk.setText(karyawan.getNik().toString());
		txtNama.setText(karyawan.getNama());
		txtJabatan.setText(karyawan.getJabatan().getNama());
		Integer hadir = 0;
		Integer lembur = 0;
		for (KehadiranKaryawan absen : absenService.findByKaryawan(karyawan)) {
			tabelKehadiran.getItems().add(absen);
			if (absen.getHadir()) {
				hadir += 1;
			}
			if (absen.getLembur()) {
				lembur += 1;
			}
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
						// TODO Auto-generated method stub
						LocalDate date = param.getValue().getTanggalHadir().toLocalDate();
						DateTimeFormatter time = DateTimeFormatter.ofPattern("E',' dd-MMM-yyyy", Locale.getDefault());
						return new SimpleStringProperty(time.format(date));
					}
				});
		columnHadir.setCellValueFactory(new PropertyValueFactory<KehadiranKaryawan, Boolean>("hadir"));
		columnHadir.setCellFactory(
				new Callback<TableColumn<KehadiranKaryawan, Boolean>, TableCell<KehadiranKaryawan, Boolean>>() {

					@Override
					public TableCell<KehadiranKaryawan, Boolean> call(TableColumn<KehadiranKaryawan, Boolean> param) {
						// TODO Auto-generated method stub
						return new TableCell<KehadiranKaryawan, Boolean>() {
							CheckBox box;

							@Override
							protected void updateItem(Boolean item, boolean empty) {
								// TODO Auto-generated method stub
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
						// TODO Auto-generated method stub
						return new TableCell<KehadiranKaryawan, Boolean>() {
							CheckBox box;

							@Override
							protected void updateItem(Boolean item, boolean empty) {
								// TODO Auto-generated method stub
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
						// TODO Auto-generated method stub
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
		loader.setLocation(getClass().getResource("/scenes/inner/absen/list.fxml"));
		loader.setController(springContainer.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	private void loadDataKaryawan() {
		try {
			tableKaryawan.getItems().clear();
			tableKaryawan.getItems().addAll(karyawan.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initConstuct() {
		loadDataKaryawan();
	}

	@Override
	@Autowired
	public void setNotificationDialog(DialogsFX notif) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		// TODO Auto-generated method stub

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
