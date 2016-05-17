package app.controller.absensi;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.controller.HomeController;
import app.entities.KehadiranKaryawan;
import app.entities.master.DataKaryawan;
import app.repositories.AbsensiService;
import app.repositories.KaryawanService;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class AbsensiListController implements BootInitializable {

	@Autowired
	private AbsensiService absen;
	@Autowired
	private KaryawanService karyawan;
	@Autowired
	private HomeController homeController;

	private ApplicationContext springContainer;
	@FXML
	private TableView<DataKaryawan> tableView;
	@FXML
	private TableColumn<DataKaryawan, String> columnNik;
	@FXML
	private TableColumn<DataKaryawan, String> columnNama;
	@FXML
	private ListView<KehadiranKaryawan> listView;
	@FXML
	private Button btnTambah;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nik"));
		columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
		tableView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
					listView.getItems().clear();
					if (newValue != null) {
						listView.getItems().addAll(newValue.getDaftarAbsenKaryawan());
					}
				});
		listView.setCellFactory(new Callback<ListView<KehadiranKaryawan>, ListCell<KehadiranKaryawan>>() {

			Label label;
			HBox box;

			@Override
			public ListCell<KehadiranKaryawan> call(ListView<KehadiranKaryawan> param) {
				// TODO Auto-generated method stub
				return new ListCell<KehadiranKaryawan>() {
					@Override
					protected void updateItem(KehadiranKaryawan item, boolean empty) {
						// TODO Auto-generated method stub
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							box = new HBox();
							box.setSpacing(10);

							box.getChildren().add(new Label(DateTimeFormatter.ofPattern("dd MMM yyyy")
									.format(item.getTanggalHadir().toLocalDate())));

							label = new Label("Lembur : " + item.getLembur());
							box.getChildren().add(label);
							setGraphic(box);

						}
					}
				};
			}
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContainer = applicationContext;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/absen/list.fxml"));
		loader.setController(springContainer.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	private void loadDataKaryawan() {
		try {
			tableView.getItems().clear();
			tableView.getItems().addAll(karyawan.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initConstuct() {
		loadDataKaryawan();
	}

	@FXML
	public void doAdd(ActionEvent event) {
		try {
			AbsensiFormController absen = springContainer.getBean(AbsensiFormController.class);
			homeController.setLayout(absen.initView());
			absen.initConstuct();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
