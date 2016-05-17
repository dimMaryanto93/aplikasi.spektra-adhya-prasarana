package app.controller.kasbon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.CurrencyNumberFormatter;
import app.entities.KasbonKaryawan;
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class KasbonListController implements BootInitializable {

	@Autowired
	private KaryawanService karyawanService;

	@Autowired
	private CurrencyNumberFormatter formatUang;

	private ApplicationContext springContext;
	@FXML
	private ListView<DataKaryawan> listView;
	@FXML
	private TableView<KasbonKaryawan> tableView;
	@FXML
	private TableColumn<KasbonKaryawan, String> columnTanggal;
	@FXML
	private TableColumn<KasbonKaryawan, String> columnPeminjaman;
	@FXML
	private TableColumn<KasbonKaryawan, String> columnPembayaran;
	@FXML
	private TableColumn<KasbonKaryawan, String> columnSaldo;
	@FXML
	private Button btnCetak;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnTanggal.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KasbonKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KasbonKaryawan, String> param) {
						KasbonKaryawan kasbon = param.getValue();
						if (kasbon != null) {
							return new SimpleStringProperty(kasbon.getTanggalPinjam().toString());
						} else {
							return null;
						}
					}
				});
		columnPembayaran.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KasbonKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KasbonKaryawan, String> param) {
						KasbonKaryawan kasbon = param.getValue();
						if (kasbon != null) {
							return new SimpleStringProperty(formatUang.getCurrencyFormate(kasbon.getCredit()));
						} else {
							return null;
						}
					}
				});
		
		columnPeminjaman.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KasbonKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KasbonKaryawan, String> param) {
						KasbonKaryawan kasbon = param.getValue();
						if (kasbon != null) {
							return new SimpleStringProperty(formatUang.getCurrencyFormate(kasbon.getDebit()));
						} else {
							return null;
						}
					}
				});
		columnSaldo.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<KasbonKaryawan, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<KasbonKaryawan, String> param) {
						KasbonKaryawan kasbon = param.getValue();
						if (kasbon != null) {
							return new SimpleStringProperty(formatUang.getCurrencyFormate(kasbon.getSaldoTerakhir()));
						} else {
							return null;
						}
					}
				});

		listView.setCellFactory(new Callback<ListView<DataKaryawan>, ListCell<DataKaryawan>>() {

			@Override
			public ListCell<DataKaryawan> call(ListView<DataKaryawan> param) {
				return new ListCell<DataKaryawan>() {
					HBox box;
					Label nip;
					Label nama;

					@Override
					protected void updateItem(DataKaryawan item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							box = new HBox(10);
							nip = new Label(item.getNik().toString());
							nama = new Label(item.getNama());
							box.getChildren().add(nip);
							box.getChildren().add(nama);
							setGraphic(box);
						}
					}
				};
			}
		});
		listView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataKaryawan> observable, DataKaryawan oldValue, DataKaryawan newValue) -> {
					tableView.getItems().clear();
					if (newValue != null) {
						tableView.getItems().addAll(newValue.getDaftarKasbon());
					}
				});

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.springContext = arg0;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/kasbon/List.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initConstuct() {
		doRefresh();
	}

	@FXML
	public void doClearSelection() {
		listView.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefresh() {
		listView.getItems().clear();
		listView.getItems().addAll(karyawanService.findAll());
	}

}
