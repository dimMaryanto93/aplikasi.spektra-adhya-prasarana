package app.controller.karyawan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.controller.HomeController;
import app.entities.Employee;
import app.services.EmployeeService;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

@Component
public class EmployeeController implements BootInitializable {

	private Stage primaryStage;
	private ApplicationContext springContext;

	@Autowired
	private EmployeeService service;

	@Autowired
	private HomeController homeController;

	private SpinnerValueFactory<Double> gajiValueFactory;
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

	private void setFields(Employee anEmployee) {
		if (anEmployee != null) {
			txtNama.setText(anEmployee.getName());
			txtAgama.setText(anEmployee.getAgama());
			txtTempatLahir.setText(anEmployee.getTmLahir());
			txtTanggalLahir.setText(anEmployee.gettLahir().toString());
			txaAlamat.setText(anEmployee.getAlamat());
			txtNik.setText(String.valueOf(anEmployee.getNik()));
			txtJabatan.setText(anEmployee.getJabatan());
			txtGapok.setText(anEmployee.getGaji().toString());
			txtJk.setText(anEmployee.getJenisKelamin());
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
				});
		columnNik.setCellValueFactory(new PropertyValueFactory<Employee, String>("nik"));
		columnNama.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		columnJabatan.setCellValueFactory(new PropertyValueFactory<Employee, String>("jabatan"));
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.springContext = arg0;
	}

	@Override
	public void initConstuct() {
		try {
			tableView.getItems().clear();
			tableView.getItems().addAll(service.getAll());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void doAddEmployee(ActionEvent event) {
	}

	@FXML
	public void doClearSelection(ActionEvent e) {
		tableView.getSelectionModel().clearSelection();
	}

	@FXML
	public void doRefreshData(ActionEvent e) {
		initConstuct();
	}

}
