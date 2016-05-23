package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.master.DataKaryawan;

public interface KasbonService extends CrudRepository<KasbonKaryawan, String> {

	public List<KasbonKaryawan> findAll();

	public List<KasbonKaryawan> findByKaryawanOrderByWaktuAsc(DataKaryawan karyawan);

}
