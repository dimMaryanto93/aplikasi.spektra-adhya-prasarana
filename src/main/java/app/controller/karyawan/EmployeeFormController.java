package app.controller.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.controller.HomeController;
import app.entities.Agama;
import app.entities.Employee;
import app.entities.Jabatan;
import app.entities.JenisKelamin;
import app.entities.Pendidikan;
import app.repositories.EmployeeRepository;
import app.repositories.JabatanRepository;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Callback;
import scala.annotation.meta.setter;

@Component
public class EmployeeFormController implements BootInitializable {

	@FXML
	private TextField txtNik;
	@FXML
	private TextField txtNama;
	@FXML
	private TextField txtTempatLahir;
	@FXML
	private TextArea txaAlamat;
	@FXML
	private DatePicker datePicker;
	@FXML
	private ComboBox<Agama> cbkAgama;
	@FXML
	private ComboBox<Pendidikan> cbkPendidikan;
	@FXML
	private ComboBox<String> cbkJabatan;
	@FXML
	private RadioButton male;
	@FXML
	private RadioButton female;
	@FXML
	private Spinner<Double> spinGapok;
	@FXML
	private ToggleGroup groupGender;

	private ApplicationContext springContext;
	private Stage stage;
	private Boolean update;
	private Employee anEmployee;

	private HashMap<String, Jabatan> mapJabatan = new HashMap<>();

	public Boolean isUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	@Autowired
	private EmployeeRepository service;

	@Autowired
	private JabatanRepository jabatanService;

	@Autowired
	private HomeController homeController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.cbkJabatan.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> value, String oldValue, String newValue) -> {
					spinGapok.setDisable(newValue == null);
					if (newValue != null) {
						this.spinGapok.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
								Double.valueOf(0), Double.MAX_VALUE, Double.valueOf(0), 500000));
						this.spinGapok.getEditor().setAlignment(Pos.CENTER_RIGHT);
						this.spinGapok.setEditable(true);
						this.spinGapok.getValueFactory().setValue(mapJabatan.get(newValue).getGapok());
					} else {
						this.spinGapok.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
								Double.valueOf(0), Double.valueOf(0), Double.valueOf(0), Double.valueOf(0)));
						this.spinGapok.getEditor().setAlignment(Pos.CENTER_RIGHT);
						this.spinGapok.setEditable(true);
					}
				});

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.springContext = applicationContext;

	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/karyawan/form.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void initConstuct() {
		this.setUpdate(false);
		this.anEmployee = new Employee();
		this.cbkJabatan.getItems().clear();
		for (Jabatan j : jabatanService.findAll()) {
			String key = new StringBuilder(j.getKodeJabatan()).append(" ").append(j.getNama()).toString();
			mapJabatan.put(key, j);
			this.cbkJabatan.getItems().add(key);			
		}
		cbkAgama.getItems().addAll(Agama.values());
		cbkPendidikan.getItems().addAll(Pendidikan.values());
		this.datePicker.setValue(LocalDate.now());

	}

	public void initConstuct(Employee anEmployee) {
		this.setUpdate(true);
		this.anEmployee = anEmployee;
		cbkAgama.getItems().addAll(Agama.values());
		cbkPendidikan.getItems().addAll(Pendidikan.values());
		this.txtNik.setText(String.valueOf(anEmployee.getNik()));
		this.txtNama.setText(anEmployee.getNama());
		
		// add item to combobox jabatan
		this.cbkJabatan.getItems().clear();
		for (Jabatan j : jabatanService.findAll()) {
			String key = new StringBuilder(j.getKodeJabatan()).append(" ").append(j.getNama()).toString();
			mapJabatan.put(key, j);
			this.cbkJabatan.getItems().add(key);			
		}
		
		// set value
		Jabatan j = anEmployee.getJabatan();
		String key = new StringBuilder(j.getKodeJabatan()).append(" ").append(j.getNama()).toString();
		
		// select value to combobox
		this.cbkJabatan.getSelectionModel().select(key);
		
		
		this.cbkAgama.setValue(anEmployee.getAgama());
		this.cbkPendidikan.setValue(anEmployee.getPendidikan());
		this.datePicker.setValue(anEmployee.gettLahir().toLocalDate());
		this.spinGapok.getValueFactory().setValue(anEmployee.getGaji());

		this.male.setSelected(anEmployee.getJenisKelamin() == JenisKelamin.Laki_Laki);
		this.female.setSelected(anEmployee.getJenisKelamin() == JenisKelamin.Perempuan);
	}

	@FXML
	public void doCancel(ActionEvent e) {
		homeController.showEmployee();
	}

	public JenisKelamin getJenisKelamin() {
		JenisKelamin value = null;
		if (male.isSelected()) {
			value = JenisKelamin.Laki_Laki;
		} else if (female.isSelected()) {
			value = JenisKelamin.Perempuan;
		}
		return value;
	}

	private void newDataEmployee() {
		// do thing update employee
		try {
			anEmployee.setNik(Integer.valueOf(txtNik.getText()));
			anEmployee.setNama(txtNama.getText());
			anEmployee.setAgama(cbkAgama.getValue());
			anEmployee.setJenisKelamin(getJenisKelamin());
			anEmployee.setGaji(spinGapok.getValueFactory().getValue());
			anEmployee.settLahir(Date.valueOf(datePicker.getValue()));
			anEmployee.setTmLahir(txtTempatLahir.getText());
			anEmployee.setAlamat(txaAlamat.getText());
			anEmployee.setJabatan(mapJabatan.get(cbkJabatan.getValue()));
			anEmployee.setPendidikan(cbkPendidikan.getValue());
			service.save(anEmployee);
			homeController.showEmployee();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void existDateEmployess() {
		// do thing update employee
		try {
			anEmployee.setNama(txtNama.getText());
			anEmployee.setAgama(cbkAgama.getValue());
			anEmployee.setJenisKelamin(getJenisKelamin());
			anEmployee.setGaji(spinGapok.getValueFactory().getValue());
			anEmployee.settLahir(Date.valueOf(datePicker.getValue()));
			anEmployee.setTmLahir(txtTempatLahir.getText());
			anEmployee.setAlamat(txaAlamat.getText());
			anEmployee.setJabatan(mapJabatan.get(cbkJabatan.getValue()));
			anEmployee.setPendidikan(cbkPendidikan.getValue());
			service.save(anEmployee);
			homeController.showEmployee();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@FXML
	public void doSave(ActionEvent e) {
		if (isUpdate()) {
			existDateEmployess();
		} else {
			newDataEmployee();
		}
	}

}
