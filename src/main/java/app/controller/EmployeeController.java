package app.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.entities.Employee;
import app.services.EmployeeService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

@Component
public class EmployeeController implements BootInitializable {

	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtNIK;
	@FXML
	private TextField txtAgama;
	@FXML
	private TextField txtJabatan;
	@FXML
	private DatePicker datePickerLahir;
	@FXML
	private RadioButton rbLaki;
	@FXML
	private RadioButton rbPerempuan;
	@FXML
	private TextArea txaAlamat;
	@FXML
	private TextField txtTempatLahir;
	@FXML
	private Spinner<Double> spinnerGaji;
	@FXML
	private TableView<Employee> tableEmployee;
	@FXML
	private TableColumn<Employee, Integer> columnNIK;
	@FXML
	private TableColumn<Employee, String> columnNama;
	@FXML
	private TableColumn<Employee, String> columnAgama;
	@FXML
	private TableColumn<Employee, String> columnJenisKelamin;
	@FXML
	private TableColumn<Employee, String> columnTempatLahir;
	@FXML
	private TableColumn<Employee, String> columnTanggalLahir;
	@FXML
	private TableColumn<Employee, String> columnAlamat;

	private Stage primaryStage;
	private ApplicationContext springContext;

	@Autowired
	private EmployeeService service;

	private SpinnerValueFactory<Double> gajiValueFactory;

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/karyawan.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		this.primaryStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.gajiValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.valueOf(0), Double.MAX_VALUE,
				Double.valueOf(0), Double.valueOf(0));
		this.spinnerGaji.setValueFactory(gajiValueFactory);
		this.columnNIK.setCellValueFactory(new PropertyValueFactory<>("nik"));
		this.columnNama.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.columnAgama.setCellValueFactory(new PropertyValueFactory<>("agama"));
		this.columnAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
		this.columnTanggalLahir.setCellValueFactory(param -> {
			if (param != null)
				return new SimpleObjectProperty<>(param.getValue().gettLahir().toString());
			else {
				return null;
			}
		});
		this.columnTempatLahir.setCellValueFactory(new PropertyValueFactory<>("tmLahir"));
		loadData();
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.springContext = arg0;
	}

	@FXML
	public void doSave() {
		try {
			Employee anEmployee = new Employee();
			anEmployee.setNik(Integer.parseInt(txtNIK.getText()));
			anEmployee.setName(txtNama.getText());
			anEmployee.setAgama(txtAgama.getText());
			anEmployee.setTmLahir(txtTempatLahir.getText());
			anEmployee.settLahir(Date.valueOf(datePickerLahir.getValue()));
			anEmployee.setAlamat(txaAlamat.getText());
			anEmployee.setJabatan(txtJabatan.getText());
			service.save(anEmployee);
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadData() {
		try {
			tableEmployee.getItems().clear();
			List<Employee> employee = service.getAll();
			for (Employee employees : employee) {
				tableEmployee.getItems().add(employees);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
