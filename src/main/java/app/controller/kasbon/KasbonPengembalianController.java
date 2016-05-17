package app.controller.kasbon;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.CurrencyNumberFormatter;
import app.entities.KasbonKaryawan;
import app.entities.master.DataJabatan;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Button;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

@Component
public class KasbonPengembalianController implements BootInitializable {

	private ApplicationContext springContext;
	@Autowired
	private KaryawanService karyawanService;
	@Autowired
	private CurrencyNumberFormatter formatUang;

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

		tableView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
					txtBayar.setDisable(newValue == null);
					btnSimpan.setDisable(newValue == null);
					if (newValue != null) {
						spinnerValueFactor.setAmountToStepBy(50000D);
						spinnerValueFactor.setMax(newValue.getSaldoTerakhir());
						spinnerValueFactor.setMin(0D);
						spinnerValueFactor.setValue(0D);
						txtHutang.setText(formatUang.getCurrencyFormate(newValue.getSaldoTerakhir()));
					} else {
						spinnerValueFactor.setAmountToStepBy(0D);
						spinnerValueFactor.setMax(0D);
						spinnerValueFactor.setMin(0D);
						spinnerValueFactor.setValue(0D);
						txtHutang.clear();
					}
				});
		btnSimpan.setOnAction(e -> {
			doSave(e);
		});
	}

	private void doSave(ActionEvent e) {
		DataKaryawan dataKaryawan = tableView.getSelectionModel().getSelectedItem();
		if (dataKaryawan != null) {
			KasbonKaryawan kasbon = new KasbonKaryawan();
			
			Double totalPinjam = 0D;
			Double totalBayar = 0D;

			for (KasbonKaryawan bon : dataKaryawan.getDaftarKasbon()) {
				totalBayar += bon.getPembayaran();
				totalPinjam += bon.getPinjaman();
			}
			
			kasbon.setKaryawan(dataKaryawan);
			kasbon.setTanggalPinjam(Date.valueOf(txtTanggalTransaksi.getValue()));
			kasbon.setPembayaran(txtBayar.getValueFactory().getValue());
			kasbon.setPinjaman(0D);
			
			kasbon.setSaldoTerakhir((totalPinjam - totalBayar) - kasbon.getPembayaran());

			dataKaryawan.setSaldoTerakhir(kasbon.getSaldoTerakhir());

			dataKaryawan.getDaftarKasbon().add(kasbon);

			karyawanService.save(dataKaryawan);
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
		tableView.getItems().clear();
		tableView.getItems().addAll(karyawanService.findAll());
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

}
