package app.controller.kasbon;

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
import app.configs.FormatterFactory;
import app.configs.DialogsFX;
import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.master.DataJabatan;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.KasbonService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class KasbonPengembalianController implements BootInitializable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ApplicationContext springContext;
	@Autowired
	private KaryawanService karyawanService;
	@Autowired
	private KasbonService kasbonService;
	@Autowired
	private FormatterFactory formatUang;

	@FXML
	private DatePicker txtTanggalTransaksi;
	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, String> columnNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;
	@FXML
	private TableColumn<DataKaryawan, String> columnJabatan;
	@FXML
	private TableColumn<DataKaryawan, Double> columnStatus;
	@FXML
	Spinner<Double> txtBayar;
	@FXML
	private TextField txtHutang;
	@FXML
	private TextField txtSisa;
	@FXML
	private Button btnSimpan;
	@FXML
	private TextField txtCariKaryawan;

	private SpinnerValueFactory.DoubleSpinnerValueFactory spinnerValueFactor = new SpinnerValueFactory.DoubleSpinnerValueFactory(
			Double.valueOf(0), Double.valueOf(0), Double.valueOf(0), Double.valueOf(0));
	private KasbonKaryawan kasbon;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtTanggalTransaksi.getEditor().setAlignment(Pos.CENTER_RIGHT);
		txtTanggalTransaksi.setValue(LocalDate.now());

		txtBayar.setValueFactory(spinnerValueFactor);
		txtBayar.getEditor().setAlignment(Pos.CENTER_RIGHT);

		txtHutang.setAlignment(Pos.CENTER_RIGHT);

		txtSisa.setAlignment(Pos.CENTER_RIGHT);

		columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nik"));
		columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
		columnJabatan.setCellValueFactory(params -> {
			DataJabatan j = params.getValue().getJabatan();
			if (j != null) {
				return new SimpleStringProperty(j.getNama());
			} else {
				return new SimpleStringProperty();
			}
		});
		columnStatus.setCellValueFactory(params -> {
			DataKaryawan value = params.getValue();
			if (value != null) {
				return new SimpleObjectProperty<>(value.getTotalSaldoTerakhir());
			} else {
				return new SimpleObjectProperty<Double>(-1D);
			}
		});
		columnStatus.setCellFactory(new Callback<TableColumn<DataKaryawan, Double>, TableCell<DataKaryawan, Double>>() {

			@Override
			public TableCell<DataKaryawan, Double> call(TableColumn<DataKaryawan, Double> param) {
				// TODO Auto-generated method stub
				return new TableCell<DataKaryawan, Double>() {
					private Label labelStatus;
					private Button buttonStatus;

					@Override
					protected void updateItem(Double item, boolean empty) {
						// TODO Auto-generated method stub
						super.updateItem(item, empty);
						setAlignment(Pos.CENTER);
						if (empty) {
							setText(null);
						} else {
							DataKaryawan karyawan = tableView.getItems().get(getIndex());
							if (item == 0D) {
								labelStatus = new Label("LUNAS");
								labelStatus.setTextFill(Color.GREEN);
								setGraphic(labelStatus);
							} else {
								StringBuilder sb = new StringBuilder(
										"Lihat seluruh transaksi yang dilakukan oleh karyawan dengan nama ")
												.append(karyawan.getNama()).append(" dan dengan NIK ")
												.append(karyawan.getNik());
								buttonStatus = new Button("BELUM LUNAS");
								buttonStatus.setTextFill(Color.RED);
								buttonStatus.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WARNING));
								buttonStatus.setTooltip(new Tooltip(sb.toString()));
								buttonStatus.setOnAction(e -> {
									// TODO show dialog
									doShowDialog(e, karyawan);
								});
								setGraphic(buttonStatus);

							}
						}
					}

					private void doShowDialog(ActionEvent e, DataKaryawan karyawan) {
						for (KasbonKaryawan bon : kasbonService.findByKaryawanOrderByWaktuAsc(karyawan)) {
							StringBuilder sb = new StringBuilder("|").append(bon.getTanggalPinjam()).append("|")
									.append(formatUang.getCurrencyFormate(bon.getPembayaran())).append("|")
									.append(formatUang.getCurrencyFormate(bon.getPinjaman())).append("|")
									.append(formatUang.getCurrencyFormate(bon.getSaldoTerakhir())).append("|");
							System.out.println(sb.toString());
						}
					}
				};
			}
		});

		tableView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
					txtBayar.setDisable(newValue == null);
					if (newValue != null) {
						spinnerValueFactor.setAmountToStepBy(50000D);
						spinnerValueFactor.setMax(newValue.getTotalSaldoTerakhir());
						spinnerValueFactor.setMin(0D);
						spinnerValueFactor.setValue(0D);
						txtHutang.setText(formatUang.getCurrencyFormate(newValue.getTotalSaldoTerakhir()));
					} else {
						spinnerValueFactor.setAmountToStepBy(0D);
						spinnerValueFactor.setMax(0D);
						spinnerValueFactor.setMin(0D);
						spinnerValueFactor.setValue(0D);
						txtHutang.clear();
					}
				});
		txtBayar.getValueFactory().valueProperty()
				.addListener((ObservableValue<? extends Double> values, Double oldValue, Double newValue) -> {
					btnSimpan.setDisable(newValue <= 0D);
				});
		btnSimpan.setOnAction(e -> {
			doSave(e);
		});
	}

	

	private void doSave(ActionEvent e) {
		DataKaryawan dataKaryawan = tableView.getSelectionModel().getSelectedItem();
		if (dataKaryawan != null) {

			try {
				this.kasbon = new KasbonKaryawan();

				kasbon.setKaryawan(dataKaryawan);
				kasbon.setTanggalPinjam(Date.valueOf(txtTanggalTransaksi.getValue()));
				kasbon.setPembayaran(txtBayar.getValueFactory().getValue());
				kasbon.setPinjaman(0D);

				kasbon.setSaldoTerakhir(dataKaryawan.getTotalSaldoTerakhir() - kasbon.getPembayaran());

				dataKaryawan.getDaftarKasbon().add(kasbon);

				karyawanService.save(dataKaryawan);
				initConstuct();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		} else {
			// TODO tampilkan dialop peringatan karyawan belum pilih
			System.out.println("Karyawan belum dipilih");
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.springContext = arg0;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/kasbon/Pengembalian.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initConstuct() {
		txtTanggalTransaksi.setValue(LocalDate.now());
		tableView.getItems().clear();
		tableView.getItems().addAll(karyawanService.findAll());
		txtBayar.getValueFactory().setValue(0D);
		this.kasbon = new KasbonKaryawan();
	}

	@FXML
	public void doBack(ActionEvent event) {
	}

	@FXML
	public void doClearSelection() {
		tableView.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefresh() {
		initConstuct();
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

}
