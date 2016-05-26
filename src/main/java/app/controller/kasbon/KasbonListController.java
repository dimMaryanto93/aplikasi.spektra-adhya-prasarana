package app.controller.kasbon;

import java.io.IOException;
import java.net.URL;
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
import app.entities.master.DataKaryawan;
import app.repositories.KaryawanService;
import app.repositories.KasbonService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class KasbonListController implements BootInitializable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private KaryawanService karyawanService;

	@Autowired
	private KasbonService kasbonService;

	@Autowired
	private FormatterFactory formatUang;

	private ApplicationContext springContext;
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
		columnPembayaran.setCellValueFactory(new PropertyValueFactory<KasbonKaryawan, Double>("pembayaran"));
		columnPembayaran
				.setCellFactory(new Callback<TableColumn<KasbonKaryawan, Double>, TableCell<KasbonKaryawan, Double>>() {

					@Override
					public TableCell<KasbonKaryawan, Double> call(TableColumn<KasbonKaryawan, Double> param) {
						return new TableCell<KasbonKaryawan, Double>() {
							@Override
							protected void updateItem(Double item, boolean empty) {
								// TODO Auto-generated method stub
								super.updateItem(item, empty);
								setAlignment(Pos.CENTER_RIGHT);
								if (empty) {
									setText(null);
								} else {
									setText(formatUang.getCurrencyFormate(item));
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
								// TODO Auto-generated method stub
								super.updateItem(item, empty);
								setAlignment(Pos.CENTER_RIGHT);
								if (empty) {
									setText(null);
								} else {
									setText(formatUang.getCurrencyFormate(item));
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
								// TODO Auto-generated method stub
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
									setText(formatUang.getCurrencyFormate(item));
								}
							}
						};
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
						tableView.getItems().addAll(kasbonService.findByKaryawanOrderByWaktuAsc(newValue));
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
