package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
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
import app.configs.StringFormatterFactory;
import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.kepegawaian.PengajuanKasbon;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKaryawan;
import app.repositories.RepositoryKasbonKaryawan;
import app.repositories.RepositoryPengajuanKasbonKaryawan;
import app.service.ServiceKasbonKaryawan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
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
public class KasbonKaryawanPersetujuanDirekturController implements BootFormInitializable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
	private StringFormatterFactory stringFormatter;

	@Autowired
	private RepositoryKaryawan repoKaryawan;

	@Autowired
	private RepositoryKasbonKaryawan repoKasbon;

	@Autowired
	private RepositoryPengajuanKasbonKaryawan repoPengajuanKasbon;

	@Autowired
	private ServiceKasbonKaryawan serviceKasbon;

	private void setFields(DataKaryawan karyawan) {
		txtNip.setText(karyawan.getNip());
		txtNama.setText(karyawan.getNama());
		tablePeminjaman.getItems().addAll(repoKasbon.findByKaryawan(karyawan));

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
					.addAll(this.repoKaryawan.findByPengajuanKasbonIsNotNullAndPengajuanKasbonAccepted(false));
		} catch (Exception e) {
			logger.error("Tidak dapat memuat data karyawan", e);

			ExceptionDialog ex = new ExceptionDialog(e);
			ex.setTitle("Data pengajuan kasbon karyawan");
			ex.setHeaderText("Tidak dapat mendapatkan daftar data karyawan yang akan distujui oleh direktur");
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
		this.validation.registerValidator(txtNip,
				Validator.createEmptyValidator("Karyawan belum pilih!", Severity.ERROR));
		this.validation.registerValidator(checkValid, (Control c, Boolean value) -> ValidationResult
				.fromErrorIf(checkValid, "Anda belum menyetujui perjanjian!", !value));

		this.validation.invalidProperty().addListener((b, old, value) -> {
			btnSave.setDisable(value);
		});
	}

	private Boolean pinjamLagi(DataKaryawan karyawan) {
		Double sixtyPersenOfLastCredit = serviceKasbon.getTotalPeminjaman(karyawan) * 0.6;
		return serviceKasbon.getTotalPembayaran(karyawan) >= sixtyPersenOfLastCredit;
	}

	@FXML
	public void doSave(ActionEvent event) {
		DataKaryawan karyawan = tableView.getSelectionModel().getSelectedItem();
		if (karyawan != null) {
			if (pinjamLagi(karyawan)) {
				PengajuanKasbon kasbon = karyawan.getPengajuanKasbon();
				try {
					kasbon.setAccepted(true);
					repoPengajuanKasbon.save(kasbon);

					StringBuilder saveMessage = new StringBuilder("Persetujuan kasbon karyawan atas nama ");
					saveMessage.append(karyawan.getNama()).append(" dengan NIP ").append(karyawan.getNip());
					saveMessage.append(" sebesar ").append(stringFormatter.getCurrencyFormate(kasbon.getNominal()))
							.append(", Berhasil disimpan");
					Notifications.create().title("Data pencairan kasbon karyawan").text(saveMessage.toString())
							.position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(4D)).showInformation();

					initConstuct();
				} catch (Exception e) {
					logger.error("Tidak dapat menyimpan data persetujuan kasbon karyawan atas nama {} sebesar {}",
							karyawan.getNama(), stringFormatter.getCurrencyFormate(kasbon.getNominal()), e);

					StringBuilder errorMessage = new StringBuilder(
							"Tidak dapat menyimpan kasbon karyawan yang disetujui oleh direktur atas nama ");
					errorMessage.append(karyawan.getNama()).append(" dengan NIP ").append(karyawan.getNip())
							.append(" sebesar ").append(stringFormatter.getCurrencyFormate(kasbon.getNominal()));

					ExceptionDialog ex = new ExceptionDialog(e);
					ex.setTitle("Data persetujuan direktur untuk kasbon karyawan");
					ex.setHeaderText(errorMessage.toString());
					ex.setContentText(e.getMessage());
					ex.initModality(Modality.APPLICATION_MODAL);
					ex.show();
				}
			} else {
				logger.info("Tidak dapat melakukan peminjaman karena karyawan tersebut masih memiliki hutang");

				Alert exe = new Alert(AlertType.WARNING);
				exe.setTitle("Data persetujuan direktur untuk kasbon karyawan");
				exe.setHeaderText("Tidak dapat melakukan peminjaman!");
				exe.setContentText(
						"Karena karyawan tersebut masih memiliki tunggakan yang harus diselesaikan pembayarannya");
				exe.initModality(Modality.APPLICATION_MODAL);
				exe.show();
			}
		} else {
			logger.warn("Data karyawan belum diseleksi pada tabel view");

			Alert exe = new Alert(AlertType.WARNING);
			exe.setTitle("Data persetujuan direktur untuk kasbon karyawan");
			exe.setHeaderText("Data karyawan belum dipilih!");
			exe.setContentText("Daftar karyawan pada tabel harus diseleksi salah satu");
			exe.initModality(Modality.APPLICATION_MODAL);
			exe.show();
		}

	}

	@FXML
	public void doBack(ActionEvent event) {
	}

}
