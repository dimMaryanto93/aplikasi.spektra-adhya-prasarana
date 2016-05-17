package app.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.master.DataKaryawan;

public interface KaryawanService extends PagingAndSortingRepository<app.entities.master.DataKaryawan, Long> {

	public List<DataKaryawan> findAll();

	public DataKaryawan findByNama(String name);

}
