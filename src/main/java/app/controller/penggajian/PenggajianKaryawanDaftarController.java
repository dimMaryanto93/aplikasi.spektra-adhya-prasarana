package app.controller.penggajian;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
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
import app.entities.kepegawaian.Penggajian;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryPenggajianKaryawan;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class PenggajianKaryawanDaftarController implements BootFormInitializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext springContext;
    private ValidationSupport validation;

    @FXML
    private Button btnProccessed;
    @FXML
    private CheckBox chenkPrinted;
    @FXML
    private TableView<Penggajian> tableView;
    @FXML
    private TableColumn<Penggajian, String> columnNip;
    @FXML
    private TableColumn<Penggajian, String> columnNama;
    @FXML
    private TableColumn<Penggajian, Integer> columnHadir;
    @FXML
    private TableColumn<Penggajian, Integer> columnLembur;
    @FXML
    private TableColumn<Penggajian, Double> columnGajiPokok;
    @FXML
    private TextField txtTotalGajiPokok;
    @FXML
    private TextField txtTotalHadir;
    @FXML
    private TextField txtTotalLembur;
    @FXML
    private TextField totalSumHadirLembur;
    @FXML
    private TextField txtGrantTotal;
    @FXML
    private ChoiceBox<Month> txtMonth;
    @FXML
    private Spinner<Integer> txtYear;
    @FXML
    private Label txtPeriode;

    @Autowired
    private RepositoryPenggajianKaryawan servicePenggajian;
    @Autowired
    private StringFormatterFactory stringFormatter;
    @Autowired
    private PrintConfig print;
    @Autowired
    private HomeController homeController;

    private IntegerSpinnerValueFactory yearValueFactory;

    @Override
    public Node initView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/scenes/inner/penggajian/Daftar.fxml"));
        loader.setController(springContext.getBean(this.getClass()));
        return loader.load();
    }

    @Override
    public void setStage(Stage stage) {

    }

    @Override
    public void initConstuct() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtMonth.getItems().clear();
        txtMonth.getItems().addAll(Month.values());
        this.yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, 2050, Year.now().getValue(),
                1);
        this.txtYear.setValueFactory(yearValueFactory);
        this.txtYear.setEditable(true);
        this.txtYear.getEditor().setAlignment(Pos.CENTER_RIGHT);

        // table view && columns
        tableView.setSelectionModel(null);
        tableView.getItems().addListener(new ListChangeListener<Penggajian>() {

            private double totalGajiPokok;
            private double totalTransport;
            private double totalLembur;

            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Penggajian> c) {
                totalGajiPokok = 0D;
                totalTransport = 0D;
                totalLembur = 0D;

                for (Penggajian gaji : c.getList()) {
                    totalGajiPokok += gaji.getGajiPokok();
                    totalTransport += gaji.getUangTransport();
                    totalLembur += gaji.getUangLembur();
                }

                txtTotalGajiPokok.setText(stringFormatter.getCurrencyFormate(totalGajiPokok));
                txtTotalHadir.setText(stringFormatter.getCurrencyFormate(totalTransport));
                txtTotalLembur.setText(stringFormatter.getCurrencyFormate(totalLembur));
                totalSumHadirLembur.setText(stringFormatter.getCurrencyFormate(totalTransport + totalLembur));
                txtGrantTotal
                        .setText(stringFormatter.getCurrencyFormate(totalGajiPokok + totalTransport + totalLembur));
            }
        });
        columnNip.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Penggajian, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Penggajian, String> param) {
                DataKaryawan karyawan = param.getValue().getKaryawan();
                if (karyawan != null) {
                    return new SimpleStringProperty(karyawan.getNip());
                } else {
                    return new SimpleStringProperty();
                }
            }
        });
        columnNama.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Penggajian, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Penggajian, String> param) {
                DataKaryawan karyawan = param.getValue().getKaryawan();
                if (karyawan != null) {
                    return new SimpleStringProperty(karyawan.getNama());
                } else {
                    return new SimpleStringProperty();
                }
            }
        });
        columnHadir.setCellValueFactory(new PropertyValueFactory<Penggajian, Integer>("jumlahKehadiran"));
        columnHadir.setCellFactory(new Callback<TableColumn<Penggajian, Integer>, TableCell<Penggajian, Integer>>() {

            @Override
            public TableCell<Penggajian, Integer> call(TableColumn<Penggajian, Integer> param) {
                return new TableCell<Penggajian, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        setAlignment(Pos.CENTER);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(stringFormatter.getNumberIntegerOnlyFormate(item));
                        }
                    }
                };
            }
        });
        columnLembur.setCellValueFactory(new PropertyValueFactory<Penggajian, Integer>("jumlahLembur"));
        columnLembur.setCellFactory(new Callback<TableColumn<Penggajian, Integer>, TableCell<Penggajian, Integer>>() {

            @Override
            public TableCell<Penggajian, Integer> call(TableColumn<Penggajian, Integer> param) {
                return new TableCell<Penggajian, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        setAlignment(Pos.CENTER);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(stringFormatter.getNumberIntegerOnlyFormate(item));
                        }
                    }
                };
            }
        });
        columnGajiPokok.setCellValueFactory(new PropertyValueFactory<Penggajian, Double>("gajiPokok"));
        columnGajiPokok.setCellFactory(new Callback<TableColumn<Penggajian, Double>, TableCell<Penggajian, Double>>() {

            @Override
            public TableCell<Penggajian, Double> call(TableColumn<Penggajian, Double> param) {
                return new TableCell<Penggajian, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(stringFormatter.getCurrencyFormate(item));
                        }
                    }
                };
            }
        });

        initValidator();
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
        this.validation.registerValidator(txtMonth,
                Validator.createEmptyValidator("Bulan belum dipilih!", Severity.ERROR));
        this.validation.registerValidator(txtYear.getEditor(), (Control c, String value) -> ValidationResult
                .fromErrorIf(c, "Tahun harus diisi!", value.matches("\\d")));
        this.validation.invalidProperty().addListener((b, old, value) -> {
            btnProccessed.setDisable(value);
        });
    }

    @FXML
    public void doProceess(ActionEvent event) {
        Month bulan = txtMonth.getSelectionModel().getSelectedItem();
        StringBuilder sb = new StringBuilder(txtYear.getValue().toString()).append("-")
                .append(bulan.getDisplayName(TextStyle.FULL, Locale.getDefault()));
        txtPeriode.setText(sb.toString());
        try {

            Task<Object> task = new Task<Object>() {

                @Override
                protected Object call() throws Exception {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(10);
                        updateProgress(i, 99);
                        updateMessage("Mendapatkan data...");
                    }
                    tableView.getItems().clear();
                    tableView.getItems().addAll(servicePenggajian.findByTahunBulan(sb.toString()));

                    if (chenkPrinted.isSelected()) {
                        for (int i = 0; i < 100; i++) {
                            Thread.sleep(10);
                            updateProgress(i, 99);
                            updateMessage("Mencetak data penggajian pada " + sb.toString());
                        }
                        print.setValue("/jasper/penggajian/DaftarGajiKaryawan.jasper", null,
                                new JRBeanCollectionDataSource(tableView.getItems()));
                        print.doPrinted();
                    }
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
                    Notifications.create().title("Data penggajian karyawan")
                            .text("Penggajian karyawan pada " + txtPeriode.getText() + " berhasil ditampilkan")
                            .hideAfter(Duration.seconds(5D)).hideCloseButton().position(Pos.BOTTOM_RIGHT)
                            .showInformation();
                }
            });
            task.setOnFailed(e -> {
                Notifications.create().title("Data penggajian karyawan")
                        .text(e.getSource().getMessage())
                        .hideAfter(Duration.seconds(5D)).hideCloseButton().position(Pos.BOTTOM_RIGHT)
                        .showError();
            });
            ProgressDialog dlg = new ProgressDialog(task);
            dlg.setTitle("Daftar data gaji karyawan");
            dlg.setHeaderText("Mendapatkan dan mencatak laporan gaji karyawan pada periode " + txtPeriode.getText());
            dlg.initModality(Modality.APPLICATION_MODAL);
            dlg.show();

            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

        } catch (Exception e) {
            logger.error("Tidak dapat mendapatkan data penggajian pada bulan {}", sb.toString(), e);

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Cetak laporan daftar gaji karyawan");
            ex.setHeaderText("Tidak dapat mendapatkan data daftar gaji karyawan pada bulan " + txtPeriode.getText());
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    private void printed(Boolean cetak, List<Penggajian> daftarPenggajian) {

    }

    @Override
    public void initIcons() {
        // TODO Auto-generated method stub

    }

}
