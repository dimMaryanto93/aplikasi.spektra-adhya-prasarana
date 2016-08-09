package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
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
import app.entities.kepegawaian.PengajuanKasbon;
import app.entities.master.DataJabatan;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKaryawan;
import app.repositories.RepositoryPengajuanKasbonKaryawan;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
	@Autowired
	private HomeController homeController;

	@FXML
	private DatePicker txtTanggal;
	@FXML
	private Spinner<Double> txtPinjam;
	@FXML
	private Button btnSimpan;
	@FXML
	private CheckBox txtValid;
	@FXML
	private TextField txtCari;
	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, String> columnNip;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;
	@FXML
	private TableColumn<DataKaryawan, String> columnJabatan;
	@FXML
	private TableColumn<DataKaryawan, Boolean> columnStatus;
	@FXML
	private TableColumn<DataKaryawan, Double> columnNominal;

	private PengajuanKasbon pengajuanKasbon;

	private SpinnerValueFactory.DoubleSpinnerValueFactory doubleSpinnerValueFactory;

	private ObservableList<DataKaryawan> masterData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		FilteredList<DataKaryawan> filteredData = new FilteredList<>(masterData, p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(karyawan -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter
				// text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (karyawan.getNama().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else if (karyawan.getNip().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<DataKaryawan> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		tableView.setItems(sortedData);

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
				String namaKaryawan = tableView.getSelectionModel().getSelectedItem().getNama();
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

		this.tableView.getSelectionModel().selectedItemProperty().addListener((s, old, value) -> {
			txtPinjam.setDisable(value == null);
			doubleSpinnerValueFactory.setMin(0D);
			doubleSpinnerValueFactory.setValue(0D);
			if (value != null) {
				doubleSpinnerValueFactory.setMax(20000000);
				doubleSpinnerValueFactory.setAmountToStepBy(50000);
			} else {
				doubleSpinnerValueFactory.setMax(0D);
				doubleSpinnerValueFactory.setAmountToStepBy(0D);
			}
		});

		this.columnNip.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nip"));
		this.columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
		this.columnJabatan.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<DataKaryawan, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<DataKaryawan, String> param) {

						DataJabatan jab = param.getValue().getJabatan();
						if (jab != null) {
							return new SimpleStringProperty(jab.getNama());
						} else {
							return null;
						}
					}
				});
		this.columnStatus.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<DataKaryawan, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(CellDataFeatures<DataKaryawan, Boolean> param) {
						PengajuanKasbon kas = param.getValue().getPengajuanKasbon();
						if (kas != null) {
							if (kas.getAccepted()) {
								return new SimpleObjectProperty<Boolean>(true);
							} else {
								return new SimpleObjectProperty<Boolean>(false);
							}
						} else {
							return new SimpleObjectProperty<Boolean>(false);
						}
					}
				});
		this.columnStatus
				.setCellFactory(new Callback<TableColumn<DataKaryawan, Boolean>, TableCell<DataKaryawan, Boolean>>() {

					@Override
					public TableCell<DataKaryawan, Boolean> call(TableColumn<DataKaryawan, Boolean> param) {
						return new TableCell<DataKaryawan, Boolean>() {
							@Override
							protected void updateItem(Boolean item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
								} else {
									CheckBox box = new CheckBox();
									box.setSelected(item);
									box.setDisable(true);
									box.setOpacity(0.9);
									setGraphic(box);
								}
							}
						};
					}
				});

		this.columnNominal.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<DataKaryawan, Double>, ObservableValue<Double>>() {

					@Override
					public ObservableValue<Double> call(CellDataFeatures<DataKaryawan, Double> param) {
						PengajuanKasbon kasbon = param.getValue().getPengajuanKasbon();
						if (kasbon != null) {
							return new SimpleObjectProperty<Double>(kasbon.getNominal());
						} else {
							return new SimpleObjectProperty<Double>(0.0);
						}
					}
				});

		this.columnNominal
				.setCellFactory(new Callback<TableColumn<DataKaryawan, Double>, TableCell<DataKaryawan, Double>>() {

					@Override
					public TableCell<DataKaryawan, Double> call(TableColumn<DataKaryawan, Double> param) {
						return new TableCell<DataKaryawan, Double>() {
							@Override
							protected void updateItem(Double item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
								} else {
									setText(stringFormater.getCurrencyFormate(item));
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
			masterData.clear();
			for (DataKaryawan karyawan : this.repoKaryawan
					.findByPengajuanKasbonIsNullOrPengajuanKasbonAccepted(false)) {
				LocalDate hireDate = karyawan.getTanggalMulaiKerja().toLocalDate();
				Long tahunKerja = ChronoUnit.YEARS.between(hireDate, LocalDate.now());
				if (tahunKerja.intValue() >= 1 && karyawan.isAktifBekerja()) {
					masterData.add(karyawan);
				}
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
		homeController.showWellcome();
	}

	@FXML
	public void doSave(ActionEvent event) {
		DataKaryawan karyawan = tableView.getSelectionModel().getSelectedItem();
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
