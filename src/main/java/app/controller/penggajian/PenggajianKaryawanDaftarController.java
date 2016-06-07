package app.controller.penggajian;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
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
import app.configs.StringFormatterFactory;
import app.configs.PrintConfig;
import app.entities.kepegawaian.Penggajian;
import app.entities.master.DataKaryawan;
import app.repositories.PenggajianService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class PenggajianKaryawanDaftarController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private DialogsFX notif;
	private ApplicationContext springContext;
	private ValidationSupport validation;

	@FXML
	private Button btnProccessed;
	@FXML
	private CheckBox chenkPrinted;
	@FXML
	private TableView<Penggajian> tableView;
	@FXML
	private TableColumn<Penggajian, String> columnNip;
	@FXML
	private TableColumn<Penggajian, String> columnNama;
	@FXML
	private TableColumn<Penggajian, Integer> columnHadir;
	@FXML
	private TableColumn<Penggajian, Integer> columnLembur;
	@FXML
	private TableColumn<Penggajian, Double> columnGajiPokok;
	@FXML
	private TextField txtTotalGajiPokok;
	@FXML
	private TextField txtTotalHadir;
	@FXML
	private TextField txtTotalLembur;
	@FXML
	private TextField totalSumHadirLembur;
	@FXML
	private TextField txtGrantTotal;
	@FXML
	private ChoiceBox<Month> txtMonth;
	@FXML
	private Spinner<Integer> txtYear;
	@FXML
	private Label txtPeriode;

	@Autowired
	private PenggajianService servicePenggajian;

	@Autowired
	private StringFormatterFactory stringFormatter;

	@Autowired
	private PrintConfig print;

	private IntegerSpinnerValueFactory yearValueFactory;

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/penggajian/Daftar.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {

	}

	@Autowired
	@Override
	public void setNotificationDialog(DialogsFX notif) {
		this.notif = notif;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtMonth.getItems().clear();
		txtMonth.getItems().addAll(Month.values());
		this.yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, 2050, Year.now().getValue(),
				1);
		this.txtYear.setValueFactory(yearValueFactory);
		this.txtYear.setEditable(true);
		this.txtYear.getEditor().setAlignment(Pos.CENTER_RIGHT);

		// table view && columns
		tableView.setSelectionModel(null);
		tableView.getItems().addListener(new ListChangeListener<Penggajian>() {

			private double totalGajiPokok;
			private double totalTransport;
			private double totalLembur;

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Penggajian> c) {
				totalGajiPokok = 0D;
				totalTransport = 0D;
				totalLembur = 0D;

				for (Penggajian gaji : c.getList()) {
					totalGajiPokok += gaji.getGajiPokok();
					totalTransport += gaji.getUangTransport();
					totalLembur += gaji.getUangLembur();
				}

				txtTotalGajiPokok.setText(stringFormatter.getCurrencyFormate(totalGajiPokok));
				txtTotalHadir.setText(stringFormatter.getCurrencyFormate(totalTransport));
				txtTotalLembur.setText(stringFormatter.getCurrencyFormate(totalLembur));
				totalSumHadirLembur.setText(stringFormatter.getCurrencyFormate(totalTransport + totalLembur));
				txtGrantTotal
						.setText(stringFormatter.getCurrencyFormate(totalGajiPokok + totalTransport + totalLembur));
			}
		});
		columnNip.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Penggajian, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Penggajian, String> param) {
						DataKaryawan karyawan = param.getValue().getKaryawan();
						if (karyawan != null) {
							return new SimpleStringProperty(karyawan.getNip());
						} else {
							return new SimpleStringProperty();
						}
					}
				});
		columnNama.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Penggajian, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Penggajian, String> param) {
						DataKaryawan karyawan = param.getValue().getKaryawan();
						if (karyawan != null) {
							return new SimpleStringProperty(karyawan.getNama());
						} else {
							return new SimpleStringProperty();
						}
					}
				});
		columnHadir.setCellValueFactory(new PropertyValueFactory<Penggajian, Integer>("jumlahKehadiran"));
		columnHadir.setCellFactory(new Callback<TableColumn<Penggajian, Integer>, TableCell<Penggajian, Integer>>() {

			@Override
			public TableCell<Penggajian, Integer> call(TableColumn<Penggajian, Integer> param) {
				return new TableCell<Penggajian, Integer>() {
					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						setAlignment(Pos.CENTER);
						if (empty) {
							setText(null);
						} else {
							setText(stringFormatter.getNumberIntegerOnlyFormate(item));
						}
					}
				};
			}
		});
		columnLembur.setCellValueFactory(new PropertyValueFactory<Penggajian, Integer>("jumlahLembur"));
		columnLembur.setCellFactory(new Callback<TableColumn<Penggajian, Integer>, TableCell<Penggajian, Integer>>() {

			@Override
			public TableCell<Penggajian, Integer> call(TableColumn<Penggajian, Integer> param) {
				return new TableCell<Penggajian, Integer>() {
					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);
						setAlignment(Pos.CENTER);
						if (empty) {
							setText(null);
						} else {
							setText(stringFormatter.getNumberIntegerOnlyFormate(item));
						}
					}
				};
			}
		});
		columnGajiPokok.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Penggajian, Double>, ObservableValue<Double>>() {

					@Override
					public ObservableValue<Double> call(CellDataFeatures<Penggajian, Double> param) {
						DataKaryawan karyawan = param.getValue().getKaryawan();
						if (karyawan != null) {
							return new SimpleObjectProperty<Double>(karyawan.getGajiPokok());
						} else {
							return new SimpleObjectProperty<Double>(0D);
						}
					}
				});
		columnGajiPokok.setCellFactory(new Callback<TableColumn<Penggajian, Double>, TableCell<Penggajian, Double>>() {

			@Override
			public TableCell<Penggajian, Double> call(TableColumn<Penggajian, Double> param) {
				return new TableCell<Penggajian, Double>() {
					@Override
					protected void updateItem(Double item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							setText(stringFormatter.getCurrencyFormate(item));
						}
					}
				};
			}
		});

		initValidator();
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
		this.validation.registerValidator(txtMonth,
				Validator.createEmptyValidator("Bulan belum dipilih!", Severity.ERROR));
		this.validation.registerValidator(txtYear.getEditor(), (Control c, String value) -> ValidationResult
				.fromErrorIf(c, "Tahun harus diisi!", value.matches("\\d")));
		this.validation.invalidProperty().addListener((b, old, value) -> {
			btnProccessed.setDisable(value);
		});
	}

	@FXML
	public void doProceess(ActionEvent event) {
		Month bulan = txtMonth.getSelectionModel().getSelectedItem();
		StringBuilder sb = new StringBuilder(txtYear.getValue().toString()).append("-")
				.append(bulan.getDisplayName(TextStyle.FULL, Locale.getDefault()));
		txtPeriode.setText(sb.toString());
		try {
			tableView.getItems().clear();
			tableView.getItems().addAll(servicePenggajian.findByTahunBulan(sb.toString()));

			printed(chenkPrinted.isSelected(), tableView.getItems());
		} catch (Exception e) {

			logger.error("Tidak dapat mendapatkan data penggajian pada bulan {}", sb.toString(), e);
			e.printStackTrace();
		}
	}

	private void printed(Boolean cetak, List<Penggajian> daftarPenggajian) {
		if (cetak) {
			try {
				this.print.setValue("/jasper/penggajian/DaftarGajiKaryawan.jrxml", null,
						new JRBeanCollectionDataSource(daftarPenggajian));
				this.print.doPrinted();

				// notifikasi cetak
				notif.setTitle("Cetak Laporan");
				notif.setText("Silahkan hubungkan dengan device printer, Untuk mencetak daftar gaji karyawan");
				notif.showNotificationInformation(notif.getText(), notif.getText());

			} catch (JRException e) {
				logger.error("Tidak dapat mencetak dokument daftar gaji karyawan pada bulan", e);
				e.printStackTrace();
			}
		}
	}

}
