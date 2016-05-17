package app.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.KehadiranKaryawan;

public interface AbsensiService extends PagingAndSortingRepository<KehadiranKaryawan, String> {

	public List<KehadiranKaryawan> findAll();

}
