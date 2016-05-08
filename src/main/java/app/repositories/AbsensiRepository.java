package app.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.AbsensiKaryawan;
import app.entities.Employee;

public interface AbsensiRepository extends PagingAndSortingRepository<AbsensiKaryawan, String> {

	public List<AbsensiKaryawan> findAll();

	public List<AbsensiKaryawan> findByKaryawan(Employee e);

}
