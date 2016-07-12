package app.controller.prestasi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootFormInitializable;
import app.configs.PrintConfig;
import app.configs.StringFormatterFactory;
import app.controller.HomeController;
import app.entities.kepegawaian.uang.prestasi.Motor;
import app.entities.kepegawaian.uang.prestasi.PembayaranCicilanMotor;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKaryawan;
import app.repositories.RepositoryPengajuanAngsuranPrestasi;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

@Component
public class AngsuranPrestasiPersetujuanFormController implements BootFormInitializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ApplicationContext springContext;

    @FXML
    private Button btnSetuju;
    @FXML
    private Button btnBatal;
    @FXML
    private CheckBox check;
    @FXML
    private TableView<DataKaryawan> tableView;
    @FXML
    private TableColumn<DataKaryawan, Integer> columnNik;
    @FXML
    private TableColumn<DataKaryawan, String> columnNama;
    @FXML
    private TextField txtNik;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtTanggal;
    @FXML
    private TextField txtMerekMotor;
    @FXML
    private TextField txtCicilan;
    @FXML
    private TextField txtAngsuran;
    @FXML
    private TextField txtUangMuka;
    @FXML
    private TextField txtJabatan;
    @FXML
    private TextField txtGajiPokok;

    @Autowired
    private RepositoryKaryawan serviceKaryawan;
    @Autowired
    private StringFormatterFactory formater;
    @Autowired
    private RepositoryPengajuanAngsuranPrestasi serviceMotor;
    @Autowired
    private HomeController homeController;
    @Autowired
    private PrintConfig printConfig;

    private void clearFields() {
        txtNik.clear();
        txtNama.clear();
        txtTanggal.clear();
        txtMerekMotor.clear();
        txtCicilan.clear();
        txtAngsuran.clear();
        txtUangMuka.clear();
        txtJabatan.clear();
        txtGajiPokok.clear();
    }

    private void setFields(DataKaryawan karyawan) {
        txtNik.setText(karyawan.getNik().toString());
        txtNama.setText(karyawan.getNama());
        txtTanggal.setText(karyawan.getTanggalMulaiKerja().toString());
        txtJabatan.setText(karyawan.getJabatan().getNama());

        Motor motor = karyawan.getNgicilMotor();
        txtMerekMotor.setText(motor.getMerkMotor());
        txtCicilan.setText(formater.getCurrencyFormate(motor.getPembayaran()));
        txtAngsuran.setText(formater.getNumberIntegerOnlyFormate(motor.getTotalAngsuran()));
        txtUangMuka.setText(formater.getCurrencyFormate(motor.getDp()));
        txtGajiPokok.setText(formater.getCurrencyFormate(karyawan.getGajiPokok()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.btnSetuju.setDisable(true);

        this.check.setOpacity(0D);
        this.check.setText("");

        this.check.selectedProperty()
                .addListener((ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) -> {
                    btnSetuju.setDisable(!newValue);
                });
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends DataKaryawan> values, DataKaryawan oldValue, DataKaryawan newValue) -> {
                    this.btnSetuju.setOnAction(e -> {
                        doSave(e, newValue);
                    });
                    if (newValue != null) {
                        setFields(newValue);
                        this.check.setOpacity(0.9);
                        this.check.setText("Saya bersedia memberikan Uang Muka sebesar "
                                + formater.getCurrencyFormate(newValue.getNgicilMotor().getDp()));
                    } else {
                        this.check.setOpacity(0D);
                        this.check.setText("");
                        clearFields();
                    }
                });
        columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, Integer>("nik"));
        columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
    }

    private void doSave(ActionEvent event, DataKaryawan newValue) {
        try {
            Motor m = newValue.getNgicilMotor();
            Task<Object> task = new Task<Object>() {

                @Override
                protected Object call() throws Exception {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(10);
                        updateProgress(i, 99);
                        updateMessage("Menyetujui angsuran prestasi...");
                    }
                    m.setSetuju(true);
                    m.setAcceptTime(Timestamp.valueOf(LocalDateTime.now()));

                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(10);
                        updateProgress(i, 99);
                        updateMessage("Melakukan pembayaran angsuran prestasi...");
                    }
                    PembayaranCicilanMotor cicilanMotor = new PembayaranCicilanMotor();
                    cicilanMotor.setMotor(m);
                    cicilanMotor.setTanggalBayar(Date.valueOf(LocalDate.now()));
                    cicilanMotor.setBayar(m.getDp());
                    cicilanMotor.setAngsuranKe(1);
                    m.getDaftarCicilan().add(cicilanMotor);
                    serviceMotor.save(m);

                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(10);
                        updateProgress(i, 99);
                        updateMessage("Mencetak kwitansi pembayaran uang muka angsuran prestasi...");
                    }
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("nip", newValue.getNip());
                    map.put("nama", newValue.getNama());
                    map.put("dp", m.getDp());
                    map.put("bayar", m.getMerkMotor());

                    printConfig.setValue("/jasper/prestasi/PersetujuanAngsuranPrestasi.jasper", map);
                    printConfig.doPrinted();
                    succeeded();
                    return null;
                }

                @Override
                protected void succeeded() {
                    try {
                        for (int i = 0; i < 100; i++) {
                            Thread.sleep(10);
                            updateProgress(i, 99);
                            updateMessage("Menyelesaikan proses...");
                        }
                        super.succeeded();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };
            task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent event) {
                    StringBuilder saveMessage = new StringBuilder("Penggajuan angsuran prestasi karyawan atas nama ");
                    saveMessage.append(newValue.getNama()).append(" dengan NIP ").append(newValue.getNip())
                            .append(", Berhasil disimpan!");
                    Notifications.create().title("Pengajuan angsuran prestasi karyawan").text(saveMessage.toString())
                            .position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(3D)).showInformation();
                    initConstuct();
                }
            });
            ProgressDialog dlg = new ProgressDialog(task);
            dlg.setTitle("Form Persetujuan angsuran prestasi");
            dlg.setHeaderText("Melakukan persetujuan angsuran prestasi");
            dlg.show();
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

        } catch (Exception e) {
            logger.error("Tidak dapat menyimpan data pengajuan uang prestasi untuk karyawan dengan nama {}",
                    newValue.getNama(), e);

            StringBuilder errorMessage = new StringBuilder(
                    "Tidak dapat menyimpan data persetujuan angsuran prestasi karyawan atas nama ");
            errorMessage.append(newValue.getNama()).append(" dengan NIP ").append(newValue.getNip());
            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Pengajuan angsuran prestasi karyawan");
            ex.setHeaderText(errorMessage.toString());
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
    public void setMessageSource(MessageSource messageSource) {

    }

    @Override
    public Node initView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/scenes/inner/prestasi/Persetujuan.fxml"));
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
            for (DataKaryawan karyawan : serviceKaryawan.findAll()) {
                if (karyawan.isGettingCililanMotorUntukDisetujui()) {
                    tableView.getItems().add(karyawan);
                }
            }
        } catch (Exception e) {
            logger.error("Tidak dapat menampilkan daftar karyawan yang siap untuk disetujui oleh direktur", e);

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Persetujuan angsuran prestasi karyawan");
            ex.setHeaderText("Tidak dapat menampilkan daftar karyawan yang siap untuk disetujui oleh direktur");
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    @Override
    public void initValidator() {

    }

    @Override
    public void initIcons() {
        // TODO Auto-generated method stub

    }

}
