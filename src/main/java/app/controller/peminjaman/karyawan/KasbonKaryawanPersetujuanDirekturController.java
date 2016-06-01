package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import app.entities.kepegawaian.PengajuanKasbon;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.KasbonService;
import app.repositories.PengajuanKasbonService;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

@Component
public class KasbonKaryawanPersetujuanDirekturController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private DialogsFX notif;
	private ApplicationContext springContext;
	private ValidationSupport validation;

	@FXML
	private TextField txtNip;
	@FXML
	private TextField txtNama;
	@FXML
	private TableView<KasbonKaryawan> tablePeminjaman;
	@FXML
	private TableColumn<KasbonKaryawan, Date> columnTanggal;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnPinjam;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnBayar;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnSaldo;
	@FXML
	private TextField txtTanggalPengajuan;
	@FXML
	private TextField txtNominalPengajuan;
	@FXML
	private CheckBox checkValid;
	@FXML
	private Button btnSave;
	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, String> columnNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;

	@Autowired
	private FormatterFactory stringFormatter;

	@Autowired
	private KaryawanService serviceKaryawan;

	@Autowired
	private KasbonService serviceKasbon;

	@Autowired
	private PengajuanKasbonService servicePengajuanKasbon;

	private void setFields(DataKaryawan karyawan) {
		txtNip.setText(karyawan.getNip());
		txtNama.setText(karyawan.getNama());
		tablePeminjaman.getItems().addAll(serviceKasbon.findByKaryawan(karyawan));

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
		columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nip"));
		columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));

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

		tablePeminjaman.setSelectionModel(null);

		tableView.getSelectionModel().selectedItemProperty().addListener((e, old, value) -> {
			tablePeminjaman.getItems().clear();

			this.checkValid.setDisable(value == null);
			if (value != null) {
				setFields(value);
			} else {
				cleanFields();

			}
		});
		initValidator();
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/peminjaman/karyawan/Persetujuan.fxml"));
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
					.addAll(this.serviceKaryawan.findByPengajuanKasbonIsNotNullAndPengajuanKasbonAccepted(false));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void initValidator() {
		this.validation = new ValidationSupport();
		this.validation.registerValidator(txtNip,
				Validator.createEmptyValidator("Karyawan belum pilih!", Severity.ERROR));
		this.validation.registerValidator(checkValid, (Control c, Boolean value) -> ValidationResult
				.fromErrorIf(checkValid, "Anda belum menyetujui perjanjian!", !value));

		this.validation.invalidProperty().addListener((b, old, value) -> {
			btnSave.setDisable(value);
		});
	}

	private Boolean pinjamLagi(DataKaryawan karyawan) {
		Double sixtyPersenOfLastCredit = karyawan.getTotalPeminjaman() * 0.6;
		System.out.println(sixtyPersenOfLastCredit + " : " + karyawan.getTotalPembayaran());
		return karyawan.getTotalPembayaran() >= sixtyPersenOfLastCredit;
	}

	@FXML
	public void doSave(ActionEvent event) {
		DataKaryawan karyawan = tableView.getSelectionModel().getSelectedItem();
		if (karyawan != null) {
			if (pinjamLagi(karyawan)) {
				PengajuanKasbon kasbon = karyawan.getPengajuanKasbon();
				try {
					kasbon.setAccepted(true);

					servicePengajuanKasbon.save(kasbon);
					notif.showDefaultSave("Data Persetujuan Kasbon Karyawan");

					initConstuct();
				} catch (Exception e) {
					logger.error("Tidak dapat menyimpan data persetujuan kasbon karyawan atas nama {} sebesar {}",
							karyawan.getNama(), stringFormatter.getCurrencyFormate(kasbon.getNominal()), e);
					notif.showDefaultErrorSave("Persetujuan kasbon karyawan", e);
				}
			} else {
				logger.info("Tidak dapat melakukan peminjaman karena karyawan tersebut masih memiliki hutang");
				notif.setTitle("Peminjaman Karyawan");
				notif.setHeader("Tidak dapat melakukan peminjaman");
				notif.setText("Karena karyawan tersebut masih memiliki tunggakan");
				notif.showDialogWarning(notif.getTitle(), notif.getHeader(), notif.getText());
			}
		} else {
			logger.warn("Data karyawan belum diseleksi pada tabel view");
			notif.setTitle("Tabel data karyawan");
			notif.setText("Karyawan belum dipilih!");
			notif.setHeader("Tabel Data Karyawan");
			notif.showDialogInformation(notif.getTitle(), notif.getHeader(), notif.getText());
		}

	}

	@FXML
	public void doBack(ActionEvent event) {
	}

}
