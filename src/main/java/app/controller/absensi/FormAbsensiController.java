package app.controller.absensi;

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
import app.controller.HomeController;
import app.entities.AbsensiKaryawan;
import app.entities.Employee;
import app.repositories.AbsensiRepository;
import app.repositories.EmployeeRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

@Component
public class FormAbsensiController implements BootInitializable {

	@Autowired
	private HomeController homeController;
	@Autowired
	private EmployeeRepository karyawanRepository;
	@Autowired
	private AbsensiRepository absensiRepository;

	private ApplicationContext springContext;
	@FXML
	private TableView<AbsensiKaryawan> tableView;
	@FXML
	private TableColumn<AbsensiKaryawan, Integer> columnNik;
	@FXML
	private TableColumn<AbsensiKaryawan, String> columnNama;
	@FXML
	private TableColumn<AbsensiKaryawan, Boolean> columnAbsen;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnNik.setCellValueFactory(v -> {
			Employee e = v.getValue().getKaryawan();

			if (e != null) {
				return new SimpleObjectProperty<Integer>(e.getNik());
			} else {
				return new SimpleObjectProperty<Integer>(0);
			}
		});

		columnNama.setCellValueFactory(v -> {
			Employee e = v.getValue().getKaryawan();
			if (e != null) {
				return new SimpleStringProperty(e.getNama());
			} else {
				return new SimpleStringProperty();
			}
		});

		columnAbsen.setCellFactory(v -> new CheckboxInTableView(tableView.getItems()));

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/absen/form.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {

	}

	@Override
	public void initConstuct() {
		tableView.getItems().clear();
		for (Employee karyawan : karyawanRepository.findAll()) {
			AbsensiKaryawan absen = new AbsensiKaryawan();
			absen.setKaryawan(karyawan);
			absen.setTanggalHadir(Date.valueOf(LocalDate.now()));
			absen.setHadir(false);
			absen.setLembur(false);
			tableView.getItems().add(absen);
		}
	}

	@FXML
	public void doSave(ActionEvent event) {
		try {
			for (AbsensiKaryawan absen : tableView.getItems()) {
				if (absen.getHadir()) {				
					absensiRepository.save(absen);
				}
			}
			doCancel(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void doCancel(ActionEvent event) {
		homeController.showAttendance(event);
	}

	public class CheckboxInTableView extends TableCell<AbsensiKaryawan, Boolean> {

		private CheckBox hadir;
		private CheckBox lembur;
		private ObservableList<AbsensiKaryawan> daftarAbsen;

		public CheckboxInTableView(ObservableList<AbsensiKaryawan> items) {
			this.daftarAbsen = items;
		}

		@Override
		protected void updateItem(Boolean item, boolean empty) {
			// TODO Auto-generated method stub
			super.updateItem(item, empty);
			if (!empty) {
				AbsensiKaryawan absen = daftarAbsen.get(getIndex());

				this.hadir = new CheckBox("Kehadiran");
				this.lembur = new CheckBox("Lembur");
				this.lembur.setDisable(true);

				this.hadir.selectedProperty().addListener(
						(ObservableValue<? extends Boolean> values, Boolean oldValue, Boolean newValue) -> {
							lembur.setDisable(!newValue);

							absen.setHadir(newValue);

							System.out.println(
									"Nama Karyawan : " + absen.getKaryawan().getNama() + " hadir " + absen.getHadir());
						});

				this.lembur.selectedProperty().addListener(
						(ObservableValue<? extends Boolean> values, Boolean oldValue, Boolean newValue) -> {
							lembur.setDisable(!newValue);

							absen.setLembur(newValue);

							System.out.println("Nama Karyawan : " + absen.getKaryawan().getNama() + " lembur "
									+ absen.getLembur());
						});

				HBox box = new HBox();
				box.setSpacing(10);
				box.getChildren().add(hadir);
				box.getChildren().add(lembur);
				setGraphic(box);
			} else {
				setGraphic(null);

			}
		}
	}

}
