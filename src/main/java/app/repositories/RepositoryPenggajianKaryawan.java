package app.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.Penggajian;
import app.entities.master.DataKaryawan;

public interface RepositoryPenggajianKaryawan extends CrudRepository<Penggajian, String> {

    public Penggajian findByKaryawanAndTanggalBetween(DataKaryawan karyawan, Date awal, Date akhir);

    public Penggajian findByKaryawanAndTahunBulan(DataKaryawan karyawan, String gajian);

    public List<Penggajian> findByTahunBulan(String gaji);

    public List<Penggajian> findBykaryawan(DataKaryawan karyawan);

}
