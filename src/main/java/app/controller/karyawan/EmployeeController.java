package app.controller.karyawan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.controller.HomeController;
import app.entities.Employee;
import app.entities.Jabatan;
import app.repositories.EmployeeRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import scala.annotation.meta.param;

@Component
public class EmployeeController implements BootInitializable {

	private Stage primaryStage;
	private ApplicationContext springContext;

	@Autowired
	private EmployeeRepository service;

	@Autowired
	private HomeController homeController;

	@Autowired
	private EmployeeFormController formController;

	@FXML
	TextField txtNama;
	@FXML
	TextField txtAgama;
	@FXML
	TextField txtTempatLahir;
	@FXML
	TextField txtTanggalLahir;
	@FXML
	TextArea txaAlamat;
	@FXML
	TextField txtNik;
	@FXML
	TextField txtJabatan;
	@FXML
	TextField txtGapok;
	@FXML
	TextField txtJk;
	@FXML
	TextField txtCari;
	@FXML
	TableView<Employee> tableView;
	@FXML
	TableColumn<Employee, String> columnNik;
	@FXML
	TableColumn<Employee, String> columnNama;
	@FXML
	TableColumn<Employee, String> columnJabatan;
	@FXML
	TableColumn<Employee, String> columnAksi;
	@FXML
	private Button btnRemoveEmployee;
	@FXML
	private Button btnUpdateEmployee;

	private void setFields(Employee anEmployee) {
		if (anEmployee != null) {
			txtNama.setText(anEmployee.getNama());
			txtAgama.setText(anEmployee.getAgama().toString());
			txtTempatLahir.setText(anEmployee.getTmLahir());
			txtTanggalLahir.setText(anEmployee.gettLahir().toString());
			txaAlamat.setText(anEmployee.getAlamat());
			txtNik.setText(String.valueOf(anEmployee.getNik()));
			txtJabatan.setText(anEmployee.getJabatan().getNama());
			txtGapok.setText(anEmployee.getGaji().toString());
			txtJk.setText(anEmployee.getJenisKelamin().toString());
		} else {
			clearFields();
		}
	}

	private void clearFields() {
		txtNama.clear();
		txtAgama.clear();
		txtTempatLahir.clear();
		txtTanggalLahir.clear();
		txaAlamat.clear();
		txtNik.clear();
		txtJabatan.clear();
		txtGapok.clear();
		txtJk.clear();
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/karyawan/list.fxml"));

		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends Employee> ob, Employee e, Employee newValue) -> {
					setFields(newValue);
					btnRemoveEmployee.setDisable(newValue == null);
					btnRemoveEmployee.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							try {
								doDelete(newValue);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					btnUpdateEmployee.setDisable(newValue == null);
					btnUpdateEmployee.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							try {
								doUpdate(newValue);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});

				});
		columnNik.setCellValueFactory(new PropertyValueFactory<Employee, String>("nik"));
		columnNama.setCellValueFactory(new PropertyValueFactory<Employee, String>("nama"));
		columnJabatan.setCellValueFactory(params -> {
			Jabatan j = params.getValue().getJabatan();
			if (j != null) {
				return new SimpleStringProperty(j.getNama());
			} else {
				return new SimpleStringProperty();
			}
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public void initConstuct() {
		try {
			tableView.getItems().clear();
			tableView.getItems().addAll(service.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void doAddEmployee(ActionEvent event) throws IOException {
		homeController.setLayout(formController.initView());
		formController.initConstuct();
	}

	@FXML
	public void doClearSelection(ActionEvent e) {
		tableView.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefreshData(ActionEvent e) {
		initConstuct();
	}

	public void doUpdate(Employee employee) throws IOException {
		homeController.setLayout(formController.initView());
		formController.initConstuct(employee);
	}

	public void doDelete(Employee employee) throws Exception {
		service.delete(employee);
		initConstuct();
	}

}
