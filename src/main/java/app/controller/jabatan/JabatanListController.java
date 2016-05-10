package app.controller.jabatan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.controller.HomeController;
import app.entities.Jabatan;
import app.repositories.JabatanRepository;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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

@Component
public class JabatanListController implements BootInitializable {

	private ApplicationContext springContext;
	private Stage primaryStage;
	@Autowired
	private JabatanRepository service;

	@Autowired
	private HomeController homeController;
	
	@Autowired
	private JabatanFormController formController;

	@FXML
	private TableView<Jabatan> tableView;
	@FXML
	private TableColumn<Jabatan, String> columnId;
	@FXML
	private TableColumn<Jabatan, String> columnNama;
	@FXML
	private Button btnUpdate;
	@FXML
	private Button btnDelete;
	@FXML
	private TextField txtKode;
	@FXML
	private TextField txtNama;
	@FXML
	private TextArea txtKeterangan;
	
	public void setFields(Jabatan j){
		if(j != null){		
			txtKode.setText(j.getKodeJabatan());
			txtNama.setText(j.getNama());
			txtKeterangan.setText(j.getKeterangan());
		}else {
			clearFields();
		}
	}
	
	private void clearFields(){
		txtKode.clear();
		txtNama.clear();
		txtKeterangan.clear();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		tableView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends Jabatan> value, Jabatan oldValue, Jabatan newValue) ->{
			setFields(newValue);
			btnUpdate.setDisable(newValue == null);
			btnUpdate.setOnAction(e -> {
				doUpdate(newValue);
			});
			btnDelete.setDisable(newValue == null);
			btnDelete.setOnAction( e -> {
				doDelete(newValue);
			});
			
		});
		columnId.setCellValueFactory(new PropertyValueFactory<Jabatan, String>("kodeJabatan"));
		columnNama.setCellValueFactory(new PropertyValueFactory<Jabatan, String>("nama"));
	}

	private void doDelete(Jabatan newValue) {
		try {
			service.delete(newValue);
			initConstuct();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void doUpdate(Jabatan jabatan) {
		try {
			homeController.setLayout(formController.initView());
			formController.initConstuct(jabatan);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;
	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/jabatan/list.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		this.primaryStage = stage;

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
	public void doAdd(ActionEvent event) {
		try {
			homeController.setLayout(formController.initView());
			formController.initConstuct();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void doRefresh(ActionEvent event) {
		initConstuct();
	}

	@FXML
	public void doClearSelection(ActionEvent event) {
		tableView.getSelectionModel().clearSelection();
	}

}
