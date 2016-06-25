package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.FontIconFactory;
import app.configs.PrintConfig;
import app.configs.StringFormatterFactory;
import app.controller.HomeController;
import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKaryawan;
import app.repositories.RepositoryKasbonKaryawan;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class KasbonKaryawanListController implements BootInitializable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ApplicationContext springContext;

	@Autowired
	private RepositoryKaryawan serviceKaryawan;
	@Autowired
	private RepositoryKasbonKaryawan kasbonKaryawanService;
	@Autowired
	private StringFormatterFactory stringFormater;
	@Autowired
	private FontIconFactory iconFactory;
	@Autowired
	private PrintConfig configPrint;
	@Autowired
	private HomeController homeController;

	@FXML
	private ListView<DataKaryawan> listView;
	@FXML
	private TableView<KasbonKaryawan> tableView;
	@FXML
	private TableColumn<KasbonKaryawan, String> columnTanggal;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnPeminjaman;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnPembayaran;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnSaldo;
	@FXML
	private TableColumn<KasbonKaryawan, Boolean> columnCetak;
	@FXML
	private TextField txtNip;
	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtJabatan;
	@FXML
	private TextField txtTanggal;
	@FXML
	private TextField txtSaldoAkhir;
	@FXML
	private Button btnCetak;

	private void setFields(DataKaryawan karyawan) {
		try {
			txtNip.setText(karyawan.getNip());
			txtNama.setText(karyawan.getNama());
			txtJabatan.setText(karyawan.getJabatan().getNama());
			tableView.getItems().addAll(kasbonKaryawanService.findByKaryawanOrderByCreatedDateAsc(karyawan));

			Integer lastIndex = tableView.getItems().size();
			if (lastIndex >= 1) {
				KasbonKaryawan kasbon = tableView.getItems().get(lastIndex - 1);
				txtTanggal.setText(stringFormater
						.getDateTimeFormatterWithDayAndDateMonthYear(kasbon.getTanggalPinjam().toLocalDate()));
				txtSaldoAkhir.setText(stringFormater.getCurrencyFormate(kasbon.getSaldoTerakhir()));
			} else {
				txtTanggal.clear();
				txtSaldoAkhir.clear();
			}

		} catch (Exception e) {
			logger.error("Tidak dapat mendapatkan data kasbon untuk karyawan atas nama {}" + karyawan.getNama(), e);

			StringBuilder sb = new StringBuilder("Tidak dapat mendapatkan informasi kasbon karyawan atas nama ");
			sb.append(karyawan.getNama()).append(" dengan nip ").append(karyawan.getNip());

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar kasbon karyawan");
			ex.setHeaderText(sb.toString());
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}
	}

	private void clearFields() {
		txtNip.clear();
		txtNama.clear();
		txtJabatan.clear();
		txtTanggal.clear();
		txtSaldoAkhir.clear();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableView.setSelectionModel(null);

		columnCetak.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Boolean>("printed"));
		columnCetak.setCellFactory(
				new Callback<TableColumn<KasbonKaryawan, Boolean>, TableCell<KasbonKaryawan, Boolean>>() {

					@Override
					public TableCell<KasbonKaryawan, Boolean> call(TableColumn<KasbonKaryawan, Boolean> param) {
						return new TableCell<KasbonKaryawan, Boolean>() {
							@Override
							protected void updateItem(Boolean item, boolean empty) {
								setAlignment(Pos.CENTER);
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
								} else {
									Label label = new Label();
									Tooltip tip = new Tooltip();
									if (item) {
										label.setTextFill(Color.GREEN);
										iconFactory.createFontAwesomeIcon18px(label, FontAwesomeIcon.CHECK);
										tip.setText("Transaksi ini sudah dicetak");
									} else {
										iconFactory.createFontAwesomeIcon18px(label, FontAwesomeIcon.PRINT);
										tip.setText("Transaksi ini akan dicetak!");
									}
									label.setTooltip(tip);
									setGraphic(label);
								}
							}
						};
					}
				});
		columnTanggal.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KasbonKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KasbonKaryawan, String> param) {
						KasbonKaryawan kasbon = param.getValue();
						if (kasbon != null) {
							return new SimpleStringProperty(
									stringFormater.getDateIndonesianFormatter(kasbon.getTanggalPinjam().toLocalDate()));
						} else {
							return null;
						}
					}
				});
		columnTanggal
				.setCellFactory(new Callback<TableColumn<KasbonKaryawan, String>, TableCell<KasbonKaryawan, String>>() {

					@Override
					public TableCell<KasbonKaryawan, String> call(TableColumn<KasbonKaryawan, String> param) {
						return new TableCell<KasbonKaryawan, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								setAlignment(Pos.CENTER);
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
								} else {
									setText(item);
								}
							}
						};
					}
				});

		columnPembayaran.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Double>("pembayaran"));
		columnPembayaran
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
									setText(stringFormater.getCurrencyFormate(item));
								}
							}
						};
					}
				});

		columnPeminjaman.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Double>("pinjaman"));
		columnPeminjaman
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
									setText(stringFormater.getCurrencyFormate(item));
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
										setText("LUNAS");
									} else {
										setTextFill(Color.RED);
										setText(stringFormater.getCurrencyFormate(item));
									}
								}
							}
						};
					}
				});

		listView.setCellFactory(new Callback<ListView<DataKaryawan>, ListCell<DataKaryawan>>() {

			@Override
			public ListCell<DataKaryawan> call(ListView<DataKaryawan> param) {
				return new ListCell<DataKaryawan>() {

					@Override
					protected void updateItem(DataKaryawan item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							setText(item.getNip());
						}
					}
				};
			}
		});
		listView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> observable, DataKaryawan oldValue, DataKaryawan newValue) -> {
					tableView.getItems().clear();
					btnCetak.setDisable(newValue == null);
					if (newValue != null) {
						setFields(newValue);
					} else {
						clearFields();
					}
				});

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {

	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/peminjaman/karyawan/List.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {
		try {
			listView.getItems().clear();
			listView.getItems().addAll(this.serviceKaryawan.findAll());
		} catch (Exception e) {
			logger.error("Tidak dapat memuat data karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar kasbon karyawan");
			ex.setHeaderText("Tidak dapat memuat daftar data karyawan");
			ex.setContentText(e.getMessage());
			ex.initModality(Modality.APPLICATION_MODAL);
			ex.show();
		}

	}

	@FXML
	public void doClearSelection(ActionEvent event) {
		this.listView.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefresh(ActionEvent event) {
		initConstuct();
	}

	@FXML
	public void doPrint(ActionEvent event) {
		DataKaryawan dataKaryawan = listView.getSelectionModel().getSelectedItem();

		try {
			List<KasbonKaryawan> listPrinted = new ArrayList<KasbonKaryawan>();
			for (KasbonKaryawan kasbon : tableView.getItems()) {
				if (!kasbon.getPrinted()) {
					kasbon.setPrinted(true);
					listPrinted.add(kasbon);
				}
			}

			if (listPrinted.size() >= 1) {
				Task<Object> task = new Task<Object>() {

					@Override
					protected Object call() throws Exception {

						for (int i = 0; i < 100; i++) {
							Thread.sleep(10);
							updateProgress(i, 99);
							updateMessage("Mencetak daftar kasbon karyawan...");
						}
						try {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("nip", dataKaryawan.getNip());
							map.put("nama", dataKaryawan.getNama());

							configPrint.setValue("/jasper/peminjaman/BukuPinjamanKaryawan.jasper", map,
									new JRBeanCollectionDataSource(listPrinted));
							configPrint.doPrinted();

							for (int i = 0; i < 100; i++) {
								Thread.sleep(10);
								updateProgress(i, 99);
								updateMessage("Menyimpan perubahan data kasbon...");
							}
							for (KasbonKaryawan kasbon : listPrinted) {
								kasbonKaryawanService.save(kasbon);
							}

						} catch (JRException e) {
							logger.error("Tidak dapat mencetak dokument DaftarPeminjaman.jrxml", e);
							ExceptionDialog ex = new ExceptionDialog(e);
							ex.setTitle("Cetak daftar kasbon karyawan");
							ex.setHeaderText("Tidak dapat mencetak dokument Daftar Kasbn Karyawan");
							ex.setContentText(e.getMessage());
							ex.initModality(Modality.APPLICATION_MODAL);
							ex.show();
						}

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
						initConstuct();
					}
				});
				ProgressDialog dlg = new ProgressDialog(task);
				dlg.setTitle("Daftar kasbon karyawan");
				dlg.setHeaderText("Mencetak daftar kasbon karyawan atas nama " + dataKaryawan.getNama());
				dlg.show();
				Thread th = new Thread(task);
				th.setDaemon(true);
				th.start();
			} else {
				logger.info("Tidak ada transaksi baru yang dapat dicetak oleh karyawan atas nama {} dengan NIP ",
						dataKaryawan.getNama(), dataKaryawan.getNip());
				Alert notif = new Alert(AlertType.WARNING);
				notif.setTitle("Cetak data kasbon karyawan");
				notif.setHeaderText("Tidak dapat mencetak daftar gaji kasbon karyawan");
				notif.setContentText("Tidak ada transaksi yang baru!");
				notif.initModality(Modality.APPLICATION_MODAL);
				notif.show();
			}

		} catch (Exception e) {
			logger.error("Tidak dapat menyimpan perubahan", e);
			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Daftar Kasbon Karyawan");
			ex.setHeaderText("Tidak dapat menyimpan perubahan data kasbon karyawan");
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
