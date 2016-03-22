package test.app.crud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import app.MainApplication;
import app.entities.Employee;
import app.services.EmployeeService;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class EmployeeDAOTest extends TestCase {

	@Autowired
	private EmployeeService service;

	@Test
	public void testSave() throws Exception {
		Employee anEmployee = new Employee();
		service.save(anEmployee);
	}

}
