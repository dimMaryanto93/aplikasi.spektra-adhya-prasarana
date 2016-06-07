package app.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.kepegawaian.KehadiranKaryawan;
import app.entities.master.DataKaryawan;

public interface RepositoryAbsensi extends PagingAndSortingRepository<KehadiranKaryawan, String> {

	public List<KehadiranKaryawan> findAll();

	public List<KehadiranKaryawan> findByKaryawan(DataKaryawan karyawan);

	public KehadiranKaryawan findByKaryawanAndTanggalHadir(DataKaryawan karyawan, Date tanggal);

	public List<KehadiranKaryawan> findByKaryawanAndTanggalHadirBetweenAndHadir(DataKaryawan karyawan, Date awal,
			Date akhir, Boolean hadir);

	public List<KehadiranKaryawan> findByKaryawanAndTanggalHadirBetweenAndLembur(DataKaryawan karyawan, Date awal,
			Date akhir, Boolean lembur);

}
