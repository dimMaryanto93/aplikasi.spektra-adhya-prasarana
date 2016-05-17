package app.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.KehadiranKaryawan;
import app.entities.master.DataKaryawan;

public interface AbsensiService extends PagingAndSortingRepository<KehadiranKaryawan, String> {

	public List<KehadiranKaryawan> findAll();
	
	public List<KehadiranKaryawan> findByKaryawan(DataKaryawan karyawan);

}
