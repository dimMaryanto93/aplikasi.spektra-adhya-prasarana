package app.controller.jabatan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.dialog.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.StringFormatterFactory;
import app.controller.HomeController;
import app.entities.master.DataJabatan;
import app.repositories.RepositoryJabatan;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class JabatanListController implements BootInitializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ApplicationContext springContext;

    @Autowired
    private RepositoryJabatan service;

    @Autowired
    private StringFormatterFactory stringFormater;

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
            txtGapok.setText(stringFormater.getCurrencyFormate(j.getGapok()));
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
            logger.error("Tidak dapat menghapus data jabatan", e);

            StringBuilder sb = new StringBuilder("Tidak dapat menghapus data jabatan dengan kode ")
                    .append(newValue.getKodeJabatan());

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Data jabatan");
            ex.setHeaderText(sb.toString());
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }

    }

    private void doUpdate(DataJabatan jabatan) {
        try {
            homeController.setLayout(formController.initView());
            formController.initConstuct(jabatan);

            this.homeController.setTitleContent("Form ubah data jabatan dengan kode " + jabatan.getKodeJabatan());
        } catch (IOException e) {
            logger.error("Tidak dapat menampilkan form jabatan", e);

            StringBuilder sb = new StringBuilder("Tidak dapat menampilkan form jabatan");
            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Data jabatan");
            ex.setHeaderText(sb.toString());
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;
    }

    @Override
    public Node initView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/scenes/inner/jabatan/List.fxml"));
        loader.setController(springContext.getBean(this.getClass()));
        return loader.load();
    }

    @Override
    public void setStage(Stage stage) {

    }

    @Override
    public void initConstuct() {
        try {
            tableView.getItems().clear();
            tableView.getItems().addAll(service.findAll());
        } catch (Exception e) {
            logger.error("Tidak dapat menampilkan data jabatan", e);
            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Daftar data jabatan");
            ex.setHeaderText("Tidak dapat mendapatkan data jabatan");
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    @FXML
    public void doAdd(ActionEvent event) {
        try {
            homeController.setLayout(formController.initView());
            formController.initConstuct();

            this.homeController.setTitleContent("Form tambah data jabatan");
        } catch (IOException e) {
            logger.error("Tidak dapat menampilkan form jabatan", e);

            StringBuilder sb = new StringBuilder("Tidak dapat menampilkan form jabatan");
            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Data jabatan");
            ex.setHeaderText(sb.toString());
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
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
    public void setMessageSource(MessageSource messageSource) {

    }

    @Override
    public void initIcons() {
        // TODO Auto-generated method stub

    }

}
