package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.master.DataKaryawan;

public interface KaryawanService extends CrudRepository<app.entities.master.DataKaryawan, String> {

	public List<DataKaryawan> findAll();

	public DataKaryawan findByNama(String name);

	public List<DataKaryawan> findByPengajuanKasbonIsNotNullAndPengajuanKasbonAccepted(Boolean accepted);

	public List<DataKaryawan> findByPengajuanKasbonIsNullOrPengajuanKasbonAccepted(Boolean accepted);

}
