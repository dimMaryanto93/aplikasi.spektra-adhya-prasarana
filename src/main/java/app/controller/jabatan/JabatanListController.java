package app.controller.jabatan;

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
import app.configs.DialogsFX;
import app.controller.HomeController;
import app.entities.master.DataJabatan;
import app.repositories.JabatanService;
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
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ApplicationContext springContext;
	private Stage primaryStage;
	@Autowired
	private JabatanService service;

	@Autowired
	private HomeController homeController;

	@Autowired
	private JabatanFormController formController;

	@FXML
	private TableView<DataJabatan> tableView;
	@FXML
	private TableColumn<DataJabatan, String> columnId;
	@FXML
	private TableColumn<DataJabatan, String> columnNama;
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
	@FXML
	private TextField txtGapok;

	public void setFields(DataJabatan j) {
		if (j != null) {
			txtKode.setText(j.getKodeJabatan());
			txtNama.setText(j.getNama());
			txtKeterangan.setText(j.getKeterangan());
			txtGapok.setText(j.getGapok().toString());
		} else {
			clearFields();
		}
	}

	private void clearFields() {
		txtKode.clear();
		txtNama.clear();
		txtKeterangan.clear();
		txtGapok.clear();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		tableView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends DataJabatan> value, DataJabatan oldValue, DataJabatan newValue) -> {
					setFields(newValue);
					btnUpdate.setDisable(newValue == null);
					btnUpdate.setOnAction(e -> {
						doUpdate(newValue);
					});
					btnDelete.setDisable(newValue == null);
					btnDelete.setOnAction(e -> {
						doDelete(newValue);
					});

				});
		columnId.setCellValueFactory(new PropertyValueFactory<DataJabatan, String>("kodeJabatan"));
		columnNama.setCellValueFactory(new PropertyValueFactory<DataJabatan, String>("nama"));
	}

	private void doDelete(DataJabatan newValue) {
		try {
			service.delete(newValue);
			initConstuct();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void doUpdate(DataJabatan jabatan) {
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
