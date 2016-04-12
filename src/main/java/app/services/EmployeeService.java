package app.services;

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

	/**
	 * method untuk menyimpan data employee
	 * 
	 * @param anEmployee
	 * @throws Exception
	 */
	public void save(Employee anEmployee) throws Exception {
		repo.save(anEmployee);
	}

	/**
	 * method untuk mangambil semua data employee dari table employee
	 * 
	 * @return
	 * @throws Exception
	 */
	public ObservableList<Employee> getAll() throws Exception {
		return FXCollections.observableArrayList(repo.findAll());
	}

	/**
	 * untuk mengupdate data employee
	 * 
	 * @param id
	 *            primary key dari employee
	 * @param anEmployee
	 * @throws Exception
	 */
	public void update(String id, Employee anEmployee) throws Exception {
		repo.save(anEmployee);
	}

	/**
	 * untuk menghapus data employee
	 * 
	 * @param anEmployee
	 * @throws Exception
	 */
	public void delete(Employee anEmployee) throws Exception {
		repo.delete(anEmployee);
	}

}
