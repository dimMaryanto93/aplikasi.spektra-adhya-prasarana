package app.controller.peminjaman.karyawan;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.ProgressDialog;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
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
import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.kepegawaian.PengajuanKasbon;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKaryawan;
import app.repositories.RepositoryPengajuanKasbonKaryawan;
import app.service.ServiceKasbonKaryawan;
import java.util.UUID;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

@Component
public class KasbonKaryawanPencairanDanaController implements BootFormInitializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext springContext;
    private ValidationSupport validation;

    @FXML
    private TableView<DataKaryawan> tableView;
    @FXML
    private TableColumn<DataKaryawan, String> columnNik;
    @FXML
    private TableColumn<DataKaryawan, String> columnNama;
    @FXML
    private TextField txtNip;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtTanggalPengajuan;
    @FXML
    private TextField txtNominalPengajuan;
    @FXML
    private CheckBox checkValid;
    @FXML
    private Button btnSave;

    @Autowired
    private RepositoryKaryawan repoKaryawan;
    @Autowired
    private RepositoryPengajuanKasbonKaryawan repoPengajuanKaryawan;
    @Autowired
    private ServiceKasbonKaryawan serviceKasbonKaryawan;
    @Autowired
    private StringFormatterFactory stringFormatter;
    @Autowired
    private PrintConfig configPrint;
    @Autowired
    private HomeController homeController;

    private KasbonKaryawan kasbon;

    private void setFields(DataKaryawan karyawan) {
        txtNip.setText(karyawan.getNip());
        txtNama.setText(karyawan.getNama());

        PengajuanKasbon pengajuan = karyawan.getPengajuanKasbon();
        txtTanggalPengajuan.setText(stringFormatter.getDateIndonesianFormatter(pengajuan.getTanggal().toLocalDate()));
        txtNominalPengajuan.setText(stringFormatter.getCurrencyFormate(pengajuan.getNominal()));
        this.checkValid.setOpacity(1D);

        StringBuilder sb = new StringBuilder("Dengan ini, saya setuju memberikan pinjaman dana sebesar ")
                .append(stringFormatter.getCurrencyFormate(pengajuan.getNominal()));
        this.checkValid.setText(sb.toString());
    }

    private void cleanFields() {
        txtNip.clear();
        txtNama.clear();
        txtTanggalPengajuan.clear();
        txtNominalPengajuan.clear();

        checkValid.setSelected(false);
        this.checkValid.setOpacity(0D);
        this.checkValid.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initValidator();
        columnNik.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nip"));
        columnNama.setCellValueFactory(new PropertyValueFactory<DataKaryawan, String>("nama"));
        tableView.getSelectionModel().selectedItemProperty().addListener((d, old, value) -> {
            this.checkValid.setDisable(value == null);
            if (value != null) {
                setFields(value);
            } else {
                cleanFields();
            }
        });
    }

    @Override
    public Node initView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/scenes/inner/peminjaman/karyawan/PencairanDana.fxml"));
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
            tableView.getItems()
                    .addAll(this.repoKaryawan.findByPengajuanKasbonIsNotNullAndPengajuanKasbonAccepted(true));
        } catch (Exception e) {
            logger.error("Tidak dapat memuat data karyawan", e);

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Data pencairan kasbon karyawan");
            ex.setHeaderText("Tidak dapat mendapatkan daftar data karyawan yang memiliki pengajuan kasbon");
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
    public void initValidator() {
        this.validation = new ValidationSupport();
        this.validation.registerValidator(txtNip, true,
                Validator.createEmptyValidator("Karyawan belum dipilih!", Severity.ERROR));
        this.validation.registerValidator(checkValid, (Control c, Boolean value) -> ValidationResult.fromErrorIf(c,
                "Anda belum melakukan persetujuan perjanjian", !value));
        this.validation.invalidProperty().addListener((b, old, value) -> {
            btnSave.setDisable(value);
        });
        this.validation.redecorate();
    }

    @FXML
    public void doSave(ActionEvent event) {
        DataKaryawan dataKaryawan = tableView.getSelectionModel().getSelectedItem();
        try {
            if (dataKaryawan != null) {
                this.kasbon = new KasbonKaryawan();
                kasbon.setKaryawan(dataKaryawan);
                PengajuanKasbon pengajuan = dataKaryawan.getPengajuanKasbon();

                kasbon.setTanggalPinjam(pengajuan.getTanggal());
                kasbon.setPinjaman(pengajuan.getNominal());
                kasbon.setPembayaran(0D);

                Double saldoAkhir = serviceKasbonKaryawan.getSaldoTerakhir(dataKaryawan);
                kasbon.setSaldoTerakhir(saldoAkhir + kasbon.getPinjaman());

                dataKaryawan.setPengajuanKasbon(null);
                dataKaryawan.getDaftarKasbon().add(kasbon);

                Task<Object> task = new Task<Object>() {

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

                    @Override
                    protected Object call() throws Exception {
                        for (int i = 0; i < 100; i++) {
                            Thread.sleep(10);
                            updateProgress(i, 99);
                            updateMessage("Menyiapkan data untuk menyimpan...");
                        }

                        repoKaryawan.save(dataKaryawan);
                        repoPengajuanKaryawan.delete(pengajuan);

                        for (int i = 0; i < 100; i++) {
                            Thread.sleep(10);
                            updateProgress(i, 99);
                            updateMessage("Memproses untuk mencetak...");
                        }
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("nip", dataKaryawan.getNip());
                        map.put("nama", dataKaryawan.getNama());
                        map.put("pinjam", kasbon.getPinjaman());
                        map.put("saldo", saldoAkhir);
                        configPrint.setValue("/jasper/peminjaman/KasbonPeminjaman.jasper", map);
                        configPrint.doPrinted();
                        succeeded();
                        return null;
                    }

                };
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

                    @Override
                    public void handle(WorkerStateEvent event) {
                        StringBuilder saveMessage = new StringBuilder("Pencairan dana kasbon karyawan atas nama ");
                        saveMessage.append(dataKaryawan.getNama()).append(" dengan NIP ").append(dataKaryawan.getNip());
                        saveMessage.append(" sebesar ").append(stringFormatter.getCurrencyFormate(kasbon.getPinjaman()))
                                .append(", Berhasil disimpan");
                        Notifications.create().title("Data pencairan kasbon karyawan").text(saveMessage.toString())
                                .position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(4D)).showInformation();

                        initConstuct();
                    }
                });

                ProgressDialog dlg = new ProgressDialog(task);
                dlg.setTitle("Pencairan dana kasbon");
                dlg.setHeaderText("Menyimpan dan mencetak data pencairan dana kasbon");
                dlg.initModality(Modality.APPLICATION_MODAL);
                dlg.show();
                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();

            } else {
                logger.warn("Data karyawan belum diseleksi pada tabel view");

                Alert exe = new Alert(AlertType.WARNING);
                exe.setTitle("Data pencairan dana kasbon karyawan");
                exe.setHeaderText("Data karyawan belum dipilih!");
                exe.setContentText("Daftar karyawan pada tabel harus diseleksi salah satu");
                exe.initModality(Modality.APPLICATION_MODAL);
                exe.show();
            }
        } catch (Exception e) {
            logger.error("Tidak dapat mengimpan dan melakukan perubahan data peminjaman karyawan", e);

            StringBuilder errorMessages = new StringBuilder(
                    "Tidak dapat menyimpan pencairan dana kasbon untuk karyawan atas nama ");
            errorMessages.append(dataKaryawan.getNama()).append(" dengan NIP ").append(dataKaryawan.getNip());

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Data pencairan kasbon karyawan");
            ex.setHeaderText(errorMessages.toString());
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    @FXML
    public void doBack(ActionEvent event) {
        homeController.showWellcome();
    }

    @Override
    public void initIcons() {
        // TODO Auto-generated method stub

    }

}
