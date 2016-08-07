package app.controller.peminjaman;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

@Component
public class DaftarPeminjamanKaryawan implements BootInitializable {

	private ApplicationContext springContext;
	private MessageSource messageSource;

	@Autowired
	private RepositoryKaryawan serviceKaryawan;
	@Autowired
	private RepositoryKasbonKaryawan kasbonKaryawanService;
	@Autowired
	private StringFormatterFactory stringFormater;
	@Autowired
	private PrintConfig configPrint;
	@Autowired
	private HomeController homeController;

	@FXML
	private TableView<KasbonKaryawan> tableView;
	@FXML
	private TableColumn<KasbonKaryawan, String> columnNip;
	@FXML
	private TableColumn<KasbonKaryawan, String> columnNama;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnDebit;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnKredit;
	@FXML
	private TableColumn<KasbonKaryawan, Double> columnSaldo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		columnNip.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KasbonKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KasbonKaryawan, String> param) {
						DataKaryawan karyawan = param.getValue().getKaryawan();
						if (karyawan != null) {
							return new SimpleStringProperty(karyawan.getNip());
						} else {
							return new SimpleStringProperty();
						}
					}
				});
		columnNama.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KasbonKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KasbonKaryawan, String> param) {
						DataKaryawan karyawan = param.getValue().getKaryawan();
						if (karyawan != null) {
							return new SimpleStringProperty(karyawan.getNama());
						} else {
							return new SimpleStringProperty();
						}
					}
				});
		columnDebit.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Double>("pembayaran"));
		columnDebit
				.setCellFactory(new Callback<TableColumn<KasbonKaryawan, Double>, TableCell<KasbonKaryawan, Double>>() {

					@Override
					public TableCell<KasbonKaryawan, Double> call(TableColumn<KasbonKaryawan, Double> param) {
						return new TableCell<KasbonKaryawan, Double>() {
							@Override
							protected void updateItem(Double item, boolean empty) {
								super.updateItem(item, empty);
								setAlignment(Pos.CENTER_RIGHT);
								if (empty) {
									setText("");
								} else {
									setText(stringFormater.getCurrencyFormate(item));
								}
							}
						};
					}
				});
		columnKredit.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Double>("pinjaman"));
		columnKredit
				.setCellFactory(new Callback<TableColumn<KasbonKaryawan, Double>, TableCell<KasbonKaryawan, Double>>() {

					@Override
					public TableCell<KasbonKaryawan, Double> call(TableColumn<KasbonKaryawan, Double> param) {
						return new TableCell<KasbonKaryawan, Double>() {
							@Override
							protected void updateItem(Double item, boolean empty) {
								super.updateItem(item, empty);
								setAlignment(Pos.CENTER_RIGHT);
								if (empty) {
									setText("");
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
									setText("");
								} else {
									setText(stringFormater.getCurrencyFormate(item));
								}
							}
						};
					}
				});
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.springContext = arg0;
	}

	@Override
	public void setMessageSource(MessageSource arg0) {
		this.messageSource = arg0;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/peminjaman/List.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {

		List<DataKaryawan> daftarKarywan = serviceKaryawan.findAll();
		for (DataKaryawan karyawan : daftarKarywan) {
			KasbonKaryawan kasbon = new KasbonKaryawan();
			kasbon.setKaryawan(karyawan);

			List<KasbonKaryawan> daftarKasbonPerkaryawan = kasbonKaryawanService.findByKaryawan(karyawan);
			Double bayar = 0D;
			Double pinjam = 0D;
			for (KasbonKaryawan bon : daftarKasbonPerkaryawan) {
				bayar += bon.getPembayaran();
				pinjam += bon.getPinjaman();
			}
			kasbon.setPembayaran(bayar);
			kasbon.setPinjaman(pinjam);
			kasbon.setSaldoTerakhir(pinjam - bayar);
			tableView.getItems().add(kasbon);
		}

	}

	@Override
	public void initIcons() {

	}

	@FXML
	public void cetak(ActionEvent event) {
	}

}
