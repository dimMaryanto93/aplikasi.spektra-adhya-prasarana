package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.master.DataKaryawan;

public interface RepositoryKasbonKaryawan extends CrudRepository<KasbonKaryawan, String> {

	public List<KasbonKaryawan> findAll();

	public List<KasbonKaryawan> findByKaryawanOrderByCreatedDateAsc(DataKaryawan karyawan);

	public List<KasbonKaryawan> findByKaryawan(DataKaryawan karyawan);

}
