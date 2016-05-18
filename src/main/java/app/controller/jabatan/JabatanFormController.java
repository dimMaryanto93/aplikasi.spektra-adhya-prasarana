package app.controller.jabatan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.configs.NotificationDialogs;
import app.controller.HomeController;
import app.entities.master.DataJabatan;
import app.repositories.JabatanService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class JabatanFormController implements BootInitializable {

	private ApplicationContext springContext;
	@FXML
	private TextField txtNama;
	@FXML
	private TextArea txtKeterangan;
	@FXML
	private TextField txtKode;
	@FXML
	private Spinner<Double> spinGapok;

	private DataJabatan jabatan;

	private Boolean update;

	@Autowired
	private JabatanService repo;

	public Boolean isUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	@Autowired
	private HomeController homeController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.spinGapok.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.valueOf(0),
				Double.MAX_VALUE, Double.valueOf(0), 500000));
		this.spinGapok.getEditor().setAlignment(Pos.CENTER_RIGHT);
		this.spinGapok.setEditable(true);
		
		
		// TODO Auto-generated method stub

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springContext = applicationContext;

	}

	@Override
	public Node initView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/scenes/inner/jabatan/form.fxml"));
		loader.setController(springContext.getBean(this.getClass()));
		return loader.load();
	}

	@Override
	public void setStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initConstuct() {
		setUpdate(false);
		this.jabatan = new DataJabatan();
	}

	public void initConstuct(DataJabatan j) {
		setUpdate(true);
		this.jabatan = j;
		txtKode.setText(j.getKodeJabatan());
		txtNama.setText(j.getNama());
		txtKeterangan.setText(j.getKeterangan());
		spinGapok.getValueFactory().setValue(j.getGapok());

	}

	private void newDataJabatan() {
		try {
			jabatan.setKodeJabatan(txtKode.getText());
			jabatan.setNama(txtNama.getText());
			jabatan.setKeterangan(txtKeterangan.getText());
			jabatan.setGapok(spinGapok.getValueFactory().getValue());
			repo.save(jabatan);
			homeController.showDepartment();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	private void existJabatan() {
		try {
			jabatan.setKodeJabatan(txtKode.getText());
			jabatan.setNama(txtNama.getText());
			jabatan.setKeterangan(txtKeterangan.getText());
			jabatan.setGapok(spinGapok.getValueFactory().getValue());
			repo.save(jabatan);
			homeController.showDepartment();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	public void doSave(ActionEvent event) {
		if (isUpdate()) {
			// lakukan fungsi update
			existJabatan();
		} else {
			// lakukan fungsi simpan
			newDataJabatan();
		}
	}

	@FXML
	public void doCancel(ActionEvent event) {
		homeController.showDepartment();
	}

	@Override
	@Autowired
	public void setNotificationDialog(NotificationDialogs notif) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		// TODO Auto-generated method stub
		
	}

}
