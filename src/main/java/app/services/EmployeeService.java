package app.services;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Employee;
import app.repositories.EmployeeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Service
public class EmployeeService {

	@Autowired
	public EmployeeService(EmployeeRepository repo) {
		this.repo = repo;
	}

	private EmployeeRepository repo;

	public void save(Employee anEmployee) throws Exception {
		repo.save(anEmployee);

	}

	public ObservableList<Employee> getAll() throws Exception {
		ObservableList<Employee> employess = FXCollections.observableArrayList();
		Iterator<Employee> employeeList = repo.findAll().iterator();
		while (employeeList.hasNext()) {
			employess.add(employeeList.next());
		}
		return employess;
	}

}
