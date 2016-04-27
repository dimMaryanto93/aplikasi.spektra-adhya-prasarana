package app.controller.karyawan;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.configs.BootInitializable;
import app.controller.HomeController;
import app.entities.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;

@Component
public class EmployeeFormController implements BootInitializable {

	private ApplicationContext springContext;
	private Stage stage;
	private Boolean update;
	private Employee anEmployee;

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
	}

	public void initConstuct(Employee anEmployee) {
		this.setUpdate(true);
		this.anEmployee = anEmployee;
	}

	@FXML
	public void doCancel(ActionEvent e) {
		homeController.showEmployee();
	}

	@FXML
	public void doSave(ActionEvent e) {
		if (isUpdate()) {
			// do thing new employee
		} else {
			// do thing update employee
		}
	}

}
