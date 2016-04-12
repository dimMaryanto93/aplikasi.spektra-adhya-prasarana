package app.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.Employee;

public interface EmployeeRepository extends PagingAndSortingRepository<app.entities.Employee, Long> {

	public List<Employee> findAll();
}
