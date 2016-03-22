package app.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends PagingAndSortingRepository<app.entities.Employee, Long> {

}
