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
import app.services.JabatanService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

@Component
public class JabatanFormController implements BootInitializable {

	private ApplicationContext springContext;
	@FXML
	private TextField txtNama;
	@FXML
	private TextArea txtKeterangan;
	@FXML
	private Label txtKode;

	private Jabatan jabatan;

	private Boolean update;

	@Autowired
	private JabatanRepository repo;

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
		this.jabatan = new Jabatan();
	}

	public void initConstuct(Jabatan j) {
		setUpdate(true);
		this.jabatan = j;
		txtKode.setText(j.getId());
		txtNama.setText(j.getNama());
		txtKeterangan.setText(j.getKeterangan());
	}

	@FXML
	public void doSave(ActionEvent event) {
		if (isUpdate()) {
			// lakukan fungsi update
			jabatan.setNama(txtNama.getText());
			jabatan.setKeterangan(txtKeterangan.getText());
			repo.save(jabatan);
			homeController.showDepartment(event);
		} else {
			// lakukan fungsi simpan
			jabatan.setNama(txtNama.getText());
			jabatan.setKeterangan(txtKeterangan.getText());
			repo.save(jabatan);
			homeController.showDepartment(event);
		}
	}

	@FXML
	public void doCancel(ActionEvent event) {
		homeController.showDepartment(event);
	}

}
