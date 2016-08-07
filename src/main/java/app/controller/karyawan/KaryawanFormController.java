package app.controller.karyawan;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
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
import app.configs.StringFormatterFactory;
import app.controller.HomeController;
import app.entities.master.DataAgama;
import app.entities.master.DataJabatan;
import app.entities.master.DataJenisKelamin;
import app.entities.master.DataKaryawan;
import app.entities.master.DataPendidikan;
import app.repositories.RepositoryJabatan;
import app.repositories.RepositoryKaryawan;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

@Component
public class KaryawanFormController implements BootFormInitializable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ApplicationContext springContext;
    private Boolean update;
    private DataKaryawan anEmployee;
    private SpinnerValueFactory.DoubleSpinnerValueFactory spinnerValueFactory;
    private HashMap<String, DataJabatan> mapJabatan = new HashMap<>();

    @FXML
    private TextField txtNik;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtTempatLahir;
    @FXML
    private TextArea txaAlamat;
    @FXML
    private DatePicker txtHireDate;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<DataAgama> cbkAgama;
    @FXML
    private ComboBox<DataPendidikan> cbkPendidikan;
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
    @FXML
    private Button btnSimpan;
    @FXML
    private Label txtNominal;
    @FXML
    private CheckBox txtValid;

    private void clearFields() {
        txtNik.clear();
        txtNama.clear();
        txtTempatLahir.clear();
        txaAlamat.clear();
        txtHireDate.setValue(LocalDate.now());
        datePicker.setValue(LocalDate.now());
        cbkAgama.getSelectionModel().clearSelection();
        cbkPendidikan.getSelectionModel().clearSelection();
        cbkJabatan.getSelectionModel().clearSelection();
        male.setSelected(true);
        spinGapok.getValueFactory().setValue(0D);
        txtNominal.setText("Rp0,00");
    }

    public Boolean isUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    @Autowired
    private RepositoryKaryawan service;
    @Autowired
    private RepositoryJabatan jabatanService;
    @Autowired
    private HomeController homeController;
    @Autowired
    private StringFormatterFactory stringFormater;

    private ValidationSupport validation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.cbkJabatan.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                        } else {
                            DataJabatan j = mapJabatan.get(item);
                            StringBuilder sb = new StringBuilder(item).append(" (").append(j.getNama()).append(")");
                            setText(sb.toString());
                        }
                    }
                };
            }
        });

        this.spinnerValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0D, 0D, 0D, 0D);
        this.spinGapok.setValueFactory(this.spinnerValueFactory);
        this.spinGapok.getEditor().setAlignment(Pos.CENTER_RIGHT);
        this.spinGapok.setDisable(true);
        this.spinGapok.getValueFactory().valueProperty().addListener((d, old, newValue) -> {
            this.txtNominal.setText(this.stringFormater.getCurrencyFormate(newValue));
        });

        this.txtHireDate.setValue(LocalDate.now());
        this.datePicker.setValue(LocalDate.now());

        this.cbkJabatan.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> value, String oldValue, String newValue) -> {
                    spinGapok.setDisable(newValue == null);
                    this.spinGapok.setEditable(newValue != null);
                    if (newValue != null) {
                        this.spinnerValueFactory.setAmountToStepBy(5000);
                        this.spinnerValueFactory.setMax(Double.MAX_VALUE);
                        this.spinnerValueFactory.setMin(0D);
                        DataJabatan jab = mapJabatan.get(newValue);
                        this.spinnerValueFactory.setValue(jab.getGapok());
                    } else {
                        this.spinnerValueFactory.setAmountToStepBy(0D);
                        this.spinnerValueFactory.setMax(0D);
                        this.spinnerValueFactory.setMin(0D);
                        this.spinnerValueFactory.setValue(0D);
                    }
                });
        initValidator();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;

    }

    @Override
    public Node initView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/scenes/inner/karyawan/Form.fxml"));
        loader.setController(springContext.getBean(this.getClass()));
        return loader.load();
    }

    @Override
    public void setStage(Stage stage) {
    }

    @Override
    public void initConstuct() {
        try {
            this.setUpdate(false);
            this.cbkJabatan.setDisable(false);
            this.spinGapok.setDisable(false);
            this.txtHireDate.setDisable(false);
            this.anEmployee = new DataKaryawan();

            this.mapJabatan.clear();
            this.cbkJabatan.getItems().clear();
            for (DataJabatan j : jabatanService.findAll()) {
                mapJabatan.put(j.getKodeJabatan(), j);
                this.cbkJabatan.getItems().add(j.getKodeJabatan());
            }
            cbkAgama.getItems().addAll(DataAgama.values());
            cbkPendidikan.getItems().addAll(DataPendidikan.values());
            this.datePicker.setValue(LocalDate.now());
            this.homeController.setTitleContent("Form tambah data karyawan");
        } catch (Exception e) {
            logger.error("Tidak dapat menampilkan data jabatan pada form karyawan", e.getMessage());

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Form data karyawan");
            ex.setHeaderText("Tidak dapat mendapatkan data jabatan!");
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }

    }

    public void initConstuct(DataKaryawan anEmployee) {
        try {
            this.setUpdate(true);
            this.cbkJabatan.setDisable(true);
            this.spinGapok.setDisable(true);
            this.txtHireDate.setDisable(true);
            this.anEmployee = anEmployee;

            cbkAgama.getItems().addAll(DataAgama.values());
            cbkPendidikan.getItems().addAll(DataPendidikan.values());
            this.txtNik.setText(String.valueOf(anEmployee.getNik()));
            this.txtNama.setText(anEmployee.getNama());
            this.txtTempatLahir.setText(anEmployee.getTempatLahir());
            this.txtHireDate.setValue(anEmployee.getTanggalMulaiKerja().toLocalDate());
            this.datePicker.setValue(anEmployee.getTanggalLahir().toLocalDate());

            // add item to combobox jabatan
            this.mapJabatan.clear();
            this.cbkJabatan.getItems().clear();
            for (DataJabatan j : jabatanService.findAll()) {
                mapJabatan.put(j.getKodeJabatan(), j);
                this.cbkJabatan.getItems().add(j.getKodeJabatan());
            }

            // select value to combobox
            this.cbkJabatan.getSelectionModel().select(anEmployee.getJabatan().getKodeJabatan());
            this.cbkAgama.setValue(anEmployee.getAgama());
            this.cbkPendidikan.setValue(anEmployee.getPendidikan());
            this.datePicker.setValue(anEmployee.getTanggalLahir().toLocalDate());
            this.spinGapok.getValueFactory().setValue(anEmployee.getGajiPokok());

            this.male.setSelected(anEmployee.getJenisKelamin() == DataJenisKelamin.Laki_Laki);
            this.female.setSelected(anEmployee.getJenisKelamin() == DataJenisKelamin.Perempuan);

            this.homeController.setTitleContent("Form ubah data karyawan dengan nip " + anEmployee.getNip());
        } catch (Exception e) {
            logger.error("Tidak dapat menampilkan data jabatan", e.getMessage());

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Form data karyawan");
            ex.setHeaderText("Tidak dapat mendapatkan data jabatan!");
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    @FXML
    public void doCancel(ActionEvent e) {
        homeController.showDaftarKaryawan();
    }

    public DataJenisKelamin getJenisKelamin() {
        DataJenisKelamin value = null;
        if (male.isSelected()) {
            value = DataJenisKelamin.Laki_Laki;
        } else if (female.isSelected()) {
            value = DataJenisKelamin.Perempuan;
        }
        return value;
    }

    private void newDataEmployee() {
        try {
            anEmployee.setNik(txtNik.getText());
            anEmployee.setNama(txtNama.getText());
            anEmployee.setTanggalMulaiKerja(Date.valueOf(txtHireDate.getValue()));
            anEmployee.setAgama(cbkAgama.getValue());
            anEmployee.setJenisKelamin(getJenisKelamin());
            anEmployee.setGajiPokok(spinGapok.getValueFactory().getValue());
            anEmployee.setTanggalLahir(Date.valueOf(datePicker.getValue()));
            anEmployee.setTempatLahir(txtTempatLahir.getText());
            anEmployee.setAlamat(txaAlamat.getText());
            anEmployee.setJabatan(mapJabatan.get(cbkJabatan.getValue()));
            anEmployee.setPendidikan(cbkPendidikan.getValue());
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyMMdd");
            StringBuilder sb = new StringBuilder(anEmployee.getJabatan().getKodeJabatan()).append(".")
                    .append(formater.format(anEmployee.getTanggalMulaiKerja().toLocalDate())).append(".")
                    .append(String.format("%03d", this.service.findAll().size() + 1));
            anEmployee.setNip(sb.toString());

            service.save(anEmployee);

            StringBuilder pesanSimpan = new StringBuilder("Data karyawan baru dengan nip ");
            pesanSimpan.append(anEmployee.getNip());
            pesanSimpan.append(", Berhasil disimpan!");

            Notifications.create().title("Data Karyawan").text(pesanSimpan.toString()).hideAfter(Duration.seconds(3D))
                    .position(Pos.BOTTOM_RIGHT).showInformation();

            initConstuct();
            clearFields();
        } catch (Exception e) {
            logger.error("Tidak dapat menambahkan data baru karyawan", e);

            StringBuilder sb = new StringBuilder("Tidak dapat menyimpan data karyawan dengan nip ");
            sb.append(anEmployee.getNip());

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Form data karyawan");
            ex.setHeaderText(sb.toString());
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    private void existDateEmployess() {
        try {
            anEmployee.setNama(txtNama.getText());
            anEmployee.setTanggalMulaiKerja(Date.valueOf(txtHireDate.getValue()));
            anEmployee.setAgama(cbkAgama.getValue());
            anEmployee.setJenisKelamin(getJenisKelamin());
            anEmployee.setGajiPokok(spinGapok.getValueFactory().getValue());
            anEmployee.setTanggalLahir(Date.valueOf(datePicker.getValue()));
            anEmployee.setTempatLahir(txtTempatLahir.getText());
            anEmployee.setAlamat(txaAlamat.getText());
            anEmployee.setJabatan(mapJabatan.get(cbkJabatan.getValue()));
            anEmployee.setPendidikan(cbkPendidikan.getValue());
            service.save(anEmployee);

            StringBuilder pesanSimpan = new StringBuilder("Data karyawan dengan nip ");
            pesanSimpan.append(anEmployee.getNip());
            pesanSimpan.append(", Berhasil diperbaharui!");

            Notifications.create().title("Data Karyawan").text(pesanSimpan.toString()).hideAfter(Duration.seconds(3D))
                    .position(Pos.BOTTOM_RIGHT).showInformation();

            homeController.showDaftarKaryawan();
        } catch (Exception e) {
            logger.error("Tidak dapat menyimpan data karyawan", e);

            StringBuilder sb = new StringBuilder("Tidak dapat menyimpan data karyawan dengan nip ");
            sb.append(anEmployee.getNip());

            ExceptionDialog ex = new ExceptionDialog(e);
            ex.setTitle("Form data karyawan");
            ex.setHeaderText(sb.toString());
            ex.setContentText(e.getMessage());
            ex.initModality(Modality.APPLICATION_MODAL);
            ex.show();
        }
    }

    @Override
    public void initValidator() {
        this.validation = new ValidationSupport();
        this.validation.registerValidator(txtNik, (Control c, String value) -> ValidationResult.fromErrorIf(c,
                "Format No Induk kependudukan (No KTP) salah!", !value.matches("\\d[\\d.]*")));
        this.validation.registerValidator(txtNama,
                Validator.createEmptyValidator("Nama tidak boleh kosong", Severity.ERROR));
        this.validation.registerValidator(txtHireDate,
                (Control c, LocalDate value) -> ValidationResult.fromErrorIf(c,
                        "Tanggal tidak boleh lebih dari tanggal " + LocalDate.now().toString(),
                        value.isAfter(LocalDate.now())));
        this.validation.registerValidator(cbkAgama,
                Validator.createEmptyValidator("Agama belum dipilih!", Severity.ERROR));
        this.validation.registerValidator(spinGapok.getEditor(),
                (Control c, String value) -> ValidationResult.fromErrorIf(c,
                        "Gaji pokok nominalnya minimal harus lebih besar dari "
                        + stringFormater.getCurrencyFormate(100),
                        Double.valueOf(value) < 100));
        this.validation.registerValidator(cbkJabatan,
                Validator.createEmptyValidator("Jabatan belum dipilih!", Severity.ERROR));
        this.validation.registerValidator(cbkPendidikan,
                Validator.createEmptyValidator("Pendidikan terakhir belum dipilih!", Severity.ERROR));
        this.validation.registerValidator(txaAlamat,
                Validator.createEmptyValidator("Alamat karyawan masih kosong", Severity.WARNING));
        this.validation.registerValidator(txtTempatLahir,
                Validator.createEmptyValidator("Tempat lahir karyawan masih kosong!", Severity.ERROR));
        this.validation.registerValidator(datePicker,
                (Control c, LocalDate value) -> ValidationResult.fromErrorIf(c,
                        "Minimal karyawan berumur 17 tahun dari sekarang ",
                        value.isAfter(LocalDate.now().minusYears(17))));
        this.validation.registerValidator(txtValid,
                (Control c, Boolean value) -> ValidationResult.fromErrorIf(c, "Anda belum menyetujui", !value));

        this.validation.invalidProperty()
                .addListener((ObservableValue<? extends Boolean> values, Boolean oldValue, Boolean newValue) -> {
                    btnSimpan.setDisable(newValue);
                });
    }

    @FXML
    public void doSave(ActionEvent e) {
        if (isUpdate()) {
            existDateEmployess();
        } else {
            newDataEmployee();
        }
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {

    }

    @Override
    public void initIcons() {
        // TODO Auto-generated method stub

    }

}
