package app.repositories;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.Penggajian;
import app.entities.master.DataKaryawan;

public interface PenggajianService extends CrudRepository<Penggajian, String> {

	public Penggajian findByKaryawanAndTanggalBetween(DataKaryawan karyawan, Date awal, Date akhir);

}
