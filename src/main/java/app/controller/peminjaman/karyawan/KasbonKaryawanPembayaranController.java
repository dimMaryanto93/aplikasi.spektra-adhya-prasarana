package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.ProgressDialog;
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
import app.configs.PrintConfig;
import app.configs.StringFormatterFactory;
import app.controller.HomeController;
import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKaryawan;
import app.repositories.RepositoryKasbonKaryawan;
import app.service.ServiceKasbonKaryawan;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

@Component
public class KasbonKaryawanPembayaranController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
	private StringFormatterFactory stringFormatter;
	@Autowired
	private RepositoryKaryawan repoKaryawan;
	@Autowired
	private RepositoryKasbonKaryawan repoKasbon;
	@Autowired
	private ServiceKasbonKaryawan serviceKasbon;
	@Autowired
	private PrintConfig configPrint;
	@Autowired
	private HomeController homeController;

	private KasbonKaryawan kasbon;

	public void setFields(DataKaryawan karyawan) {
		try {
			txtNama.setText(karyawan.getNama());
			txtJabatan.setText(karyawan.getJabatan().getNama());
			tableView.getItems().addAll(repoKasbon.findByKaryawanOrderByCreatedDateAsc(karyawan));

			bayarSpinnerValueFactory.setMin(0D);
			bayarSpinnerValueFactory.setAmountToStepBy(50000);
			bayarSpinnerValueFactory.setMax(serviceKasbon.getSaldoTerakhir(karyawan));
			bayarSpinnerValueFactory.setValue(0D);

			checkValid.setOpacity(1D);
		} catch (Exception e) {
			logger.error("Tidak dapat mendapatkan data kasbon karyawan atas nama {}", karyawan.getNama(), e);

			StringBuilder sb = new StringBuilder("Tidak dapat mendapatkan daftar data kasbon karyawan atas nama ");
			sb.append(karyawan.getNama()).append(" dengan nip ").append(karyawan.getNip());

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Form pembayaran kasbon karyawan");
			ex.setHeaderText(sb.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
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
		txtBayar.getEditor().setAlignment(Pos.CENTER_RIGHT);
		txtBayar.setEditable(true);
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
			for (DataKaryawan karyawan : repoKaryawan.findAll()) {
				this.mapKaryawan.put(karyawan.getNip(), karyawan);
				txtNip.getItems().add(karyawan.getNip());
			}
		} catch (Exception e) {
			logger.info("Tidak dapat memuat data karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar kasbon karyawan");
			ex.setHeaderText("Tidak dapat mendapatkan daftar data karyawan!");
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
		homeController.showWellcome();
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

				Double saldoAkhir = serviceKasbon.getSaldoTerakhir(dataKaryawan);
				kasbon.setSaldoTerakhir(saldoAkhir - kasbon.getPembayaran());

				dataKaryawan.getDaftarKasbon().add(kasbon);

				Task<Object> task = new Task<Object>() {

					@Override
					protected Object call() throws Exception {
						for (int i = 0; i < 100; i++) {
							Thread.sleep(10);
							updateProgress(i, 99);
							updateMessage("Menyiapkan data untuk menyimpan...");
						}
						repoKaryawan.save(dataKaryawan);

						for (int i = 0; i < 100; i++) {
							Thread.sleep(10);
							updateProgress(i, 99);
							updateMessage("Memproses untuk mencetak...");
						}
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("nip", dataKaryawan.getNip());
						map.put("nama", dataKaryawan.getNama());
						map.put("bayar", kasbon.getPembayaran());
						map.put("saldo", saldoAkhir);
						configPrint.setValue("/jasper/peminjaman/KasbonPembayaran.jasper", map);
						configPrint.doPrinted();
						succeeded();
						return null;
					}

					@Override
					protected void succeeded() {
						try {
							for (int i = 0; i < 100; i++) {
								Thread.sleep(10);
								updateProgress(i, 99);
								updateMessage("Menyelesaikan proses...");
							}
							super.succeeded();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				};
				task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

					@Override
					public void handle(WorkerStateEvent event) {
						StringBuilder pesanSimpan = new StringBuilder("Karyawan atas nama ");
						pesanSimpan.append(dataKaryawan.getNama()).append(" dengan nip ").append(dataKaryawan.getNip());
						pesanSimpan.append(" Melakukan pembayaran kasbon sebesar ")
								.append(stringFormatter.getCurrencyFormate(kasbon.getPembayaran()));

						Notifications.create().title("Data pembayaran kasbon").text(pesanSimpan.toString())
								.position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(3D)).showInformation();

						initConstuct();
					}
				});
				ProgressDialog progres = new ProgressDialog(task);
				progres.setTitle("Form pembayaran kasbon karyawan");
				progres.setHeaderText("Menyimpan dan mencetak data pembayaran kasbon karyawan");
				progres.initModality(Modality.APPLICATION_MODAL);
				progres.show();

				Thread th = new Thread(task);
				th.setDaemon(true);
				th.start();

			} catch (Exception e) {
				logger.error("Tidak dapat menyimpan pembayaran untuk peminjaman karyawan dengan nama {}",
						kasbon.getKaryawan().getNama(), e);

				StringBuilder pesanError = new StringBuilder("Tidak dapat menyimpan data kasbon karyawan atas nama ");
				pesanError.append(dataKaryawan.getNama()).append(" dengan nip ").append(dataKaryawan.getNip());
				pesanError.append(" sebesar ").append(stringFormatter.getCurrencyFormate(kasbon.getPembayaran()));

				ExceptionDialog ex = new ExceptionDialog(e);
				ex.setTitle("Daftar kasbon karyawan");
				ex.setHeaderText(pesanError.toString());
				ex.setContentText(e.getMessage());
				ex.initModality(Modality.APPLICATION_MODAL);
				ex.show();
			}
		} else {
			logger.warn("Data karyawan belum diseleksi pada tabel view");

			Alert ex = new Alert(AlertType.WARNING);
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.setTitle("Daftar kasbon karyawan");
			ex.setHeaderText("Data karyawan belum dipilih!");
			ex.setContentText("Pada tabel daftar karyawan harus diseleksi terlebih dahulu");
			ex.show();
		}
	}

	@Override
	public void initIcons() {
		// TODO Auto-generated method stub

	}

}
