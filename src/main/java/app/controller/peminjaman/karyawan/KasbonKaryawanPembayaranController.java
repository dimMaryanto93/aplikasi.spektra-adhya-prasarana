package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
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
import app.configs.FormatterFactory;
import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.KasbonService;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class KasbonKaryawanPembayaranController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private DialogsFX notif;
	private ApplicationContext springContext;
	private ValidationSupport validation;
	private HashMap<String, DataKaryawan> mapKaryawan;
	private SpinnerValueFactory.DoubleSpinnerValueFactory bayarSpinnerValueFactory;

	@FXML
	private DatePicker txtTanggal;
	@FXML
	private Spinner<Double> txtBayar;
	@FXML
	private ComboBox<String> txtNip;
	@FXML
	private TextField txtNama;
	@FXML
	private TableView<KasbonKaryawan> tableView;
	@FXML
	private TableColumn<KasbonKaryawan, Date> columnTanggal;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnPinjam;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnBayar;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnSaldo;
	@FXML
	private TextField txtJabatan;
	@FXML
	private CheckBox checkValid;
	@FXML
	private Button btnSimpan;

	@Autowired
	private FormatterFactory stringFormatter;

	@Autowired
	private KaryawanService serviceKaryawan;

	@Autowired
	private KasbonService serviceKasbon;

	private KasbonKaryawan kasbon;

	public void setFields(DataKaryawan karyawan) {
		try {
			txtNama.setText(karyawan.getNama());
			txtJabatan.setText(karyawan.getJabatan().getNama());
			tableView.getItems().addAll(serviceKasbon.findByKaryawanOrderByCreatedDateAsc(karyawan));

			bayarSpinnerValueFactory.setMin(0D);
			bayarSpinnerValueFactory.setAmountToStepBy(50000);
			bayarSpinnerValueFactory.setMax(karyawan.getTotalSaldoTerakhir());
			bayarSpinnerValueFactory.setValue(0D);

			checkValid.setOpacity(1D);
		} catch (Exception e) {
			logger.error("Tidak dapat mendapatkan data kasbon karyawan atas nama {}", karyawan.getNama(), e);
			notif.showDefaultErrorLoad("Daftar kasbon karyawan", e);
		}
	}

	public void clearFields() {
		txtNama.clear();
		txtJabatan.clear();

		bayarSpinnerValueFactory.setMin(0D);
		bayarSpinnerValueFactory.setAmountToStepBy(0D);
		bayarSpinnerValueFactory.setMax(0D);
		bayarSpinnerValueFactory.setValue(0D);

		checkValid.setOpacity(0D);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkValid.setText(stringFormatter.getCurrencyFormate(0) + " Belum ada pembayaran!");
		checkValid.setDisable(true);
		checkValid.setOpacity(0D);

		txtTanggal.setValue(LocalDate.now());

		this.bayarSpinnerValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, 0D, 0D, 0D);
		txtBayar.setValueFactory(this.bayarSpinnerValueFactory);
		txtBayar.getValueFactory().valueProperty().addListener((d, old, value) -> {
			checkValid.setText("Karyawan tersebut, telah melakukan pembayaran sebesar "
					+ stringFormatter.getCurrencyFormate(value));
			this.checkValid.setSelected(false);
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
									.append(karyawan.getJabatan().getNama()).append(" )");
							setText(sb.toString());
						}
					}
				};
			}
		});
		txtNip.getSelectionModel().selectedItemProperty().addListener((s, old, value) -> {

			checkValid.setDisable(value == null);
			txtBayar.setDisable(value == null);
			tableView.getItems().clear();
			if (value != null) {
				DataKaryawan karyawan = mapKaryawan.get(value);
				setFields(karyawan);
			} else {
				clearFields();
			}
		});

		columnTanggal.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Date>("tanggalPinjam"));
		columnTanggal
				.setCellFactory(new Callback<TableColumn<KasbonKaryawan, Date>, TableCell<KasbonKaryawan, Date>>() {

					@Override
					public TableCell<KasbonKaryawan, Date> call(TableColumn<KasbonKaryawan, Date> param) {
						return new TableCell<KasbonKaryawan, Date>() {
							@Override
							protected void updateItem(Date item, boolean empty) {
								setAlignment(Pos.CENTER);
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
								} else {
									setText(stringFormatter.getDateIndonesianFormatter(item.toLocalDate()));
								}
							}
						};
					}
				});

		columnBayar.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Double>("pembayaran"));
		columnBayar
				.setCellFactory(new Callback<TableColumn<KasbonKaryawan, Double>, TableCell<KasbonKaryawan, Double>>() {

					@Override
					public TableCell<KasbonKaryawan, Double> call(TableColumn<KasbonKaryawan, Double> param) {
						return new TableCell<KasbonKaryawan, Double>() {
							@Override
							protected void updateItem(Double item, boolean empty) {
								super.updateItem(item, empty);
								setAlignment(Pos.CENTER_RIGHT);
								if (empty) {
									setText(null);
								} else {
									setText(stringFormatter.getCurrencyFormate(item));
								}
							}
						};
					}
				});

		columnPinjam.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Double>("pinjaman"));
		columnPinjam
				.setCellFactory(new Callback<TableColumn<KasbonKaryawan, Double>, TableCell<KasbonKaryawan, Double>>() {

					@Override
					public TableCell<KasbonKaryawan, Double> call(TableColumn<KasbonKaryawan, Double> param) {
						return new TableCell<KasbonKaryawan, Double>() {
							@Override
							protected void updateItem(Double item, boolean empty) {
								super.updateItem(item, empty);
								setAlignment(Pos.CENTER_RIGHT);
								if (empty) {
									setText(null);
								} else {
									setText(stringFormatter.getCurrencyFormate(item));
								}
							}
						};
					}
				});

		columnSaldo.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Double>("saldoTerakhir"));
		columnSaldo
				.setCellFactory(new Callback<TableColumn<KasbonKaryawan, Double>, TableCell<KasbonKaryawan, Double>>() {

					@Override
					public TableCell<KasbonKaryawan, Double> call(TableColumn<KasbonKaryawan, Double> param) {
						return new TableCell<KasbonKaryawan, Double>() {
							@Override
							protected void updateItem(Double item, boolean empty) {
								super.updateItem(item, empty);
								setAlignment(Pos.CENTER_RIGHT);
								if (empty) {
									setText(null);
								} else {
									if (item == 0D) {
										setTextFill(Color.GREEN);
									} else {
										setTextFill(Color.RED);
									}
									setText(stringFormatter.getCurrencyFormate(item));
								}
							}
						};
					}
				});

		tableView.setSelectionModel(null);

		initValidator();
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/peminjaman/karyawan/Pembayaran.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {
		try {
			txtNip.getItems().clear();
			this.mapKaryawan = new HashMap<String, DataKaryawan>();
			for (DataKaryawan karyawan : serviceKaryawan.findAll()) {
				this.mapKaryawan.put(karyawan.getNip(), karyawan);
				txtNip.getItems().add(karyawan.getNip());
			}
		} catch (Exception e) {
			logger.info("Tidak dapat memuat data karyawan", e);
			notif.showDefaultErrorLoad("Data karyawan", e);
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
		this.validation.registerValidator(txtNip, true,
				Validator.createEmptyValidator("Karyawan belum dipilih!", Severity.ERROR));
		this.validation
				.registerValidator(txtTanggal,
						(Control c, LocalDate value) -> ValidationResult.fromErrorIf(c,
								"Tanggal tidak boleh lebih dari "
										+ stringFormatter.getDateIndonesianFormatter(LocalDate.now()),
								value.isAfter(LocalDate.now())));
		this.validation.registerValidator(txtBayar.getEditor(),
				(Control c, String value) -> ValidationResult.fromErrorIf(c,
						"Nominal pembayaran harus lebih besar dari " + stringFormatter.getCurrencyFormate(50000),
						Double.valueOf(value) < 50000));
		this.validation.registerValidator(checkValid, (Control c, Boolean value) -> ValidationResult.fromErrorIf(c,
				"Anda belum melakukan persetujuan perjanjian!", !value));

		this.validation.invalidProperty().addListener((b, old, value) -> {
			btnSimpan.setDisable(value);
		});
	}

	@FXML
	public void doBack(ActionEvent event) {
	}

	@FXML
	public void doSave(ActionEvent event) {
		DataKaryawan dataKaryawan = mapKaryawan.get(txtNip.getSelectionModel().getSelectedItem());
		if (dataKaryawan != null) {

			try {
				this.kasbon = new KasbonKaryawan();

				kasbon.setKaryawan(dataKaryawan);
				kasbon.setTanggalPinjam(Date.valueOf(txtTanggal.getValue()));
				kasbon.setPembayaran(txtBayar.getValueFactory().getValue());
				kasbon.setPinjaman(0D);

				kasbon.setSaldoTerakhir(dataKaryawan.getTotalSaldoTerakhir() - kasbon.getPembayaran());

				dataKaryawan.getDaftarKasbon().add(kasbon);
				serviceKaryawan.save(dataKaryawan);

				this.notif.showDefaultSave("Data Pembayaran Kasbon Karyawan");
				initConstuct();
			} catch (Exception ex) {
				logger.error("Tidak dapat menyimpan pembayaran untuk peminjaman karyawan dengan nama {}",
						kasbon.getKaryawan().getNama(), ex);
				notif.showDefaultErrorSave("Data Pembayaran Kasbon Karyawan", ex);
			}
		} else {
			logger.warn("Data karyawan belum diseleksi pada tabel view");
			notif.setTitle("Tabel data karyawan");
			notif.setText("Karyawan belum dipilih!");
			notif.setHeader("Tabel Data Karyawan");
			notif.showDialogInformation(notif.getTitle(), notif.getHeader(), notif.getText());
		}
	}

}
