package app.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue
	private Long id;
	private Integer nik;
	private String nama;
	private String agama;
	private String jenisKelamin;
	private Date tLahir;
	private String tmLahir;
	private String alamat;
	private Double gaji;
	private String jabatan;
	private Date tanggalMulaiKerja;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<Penggajian> daftarTerimaGaji = new ArrayList<Penggajian>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<Peminjaman> daftarPinjamDana = new ArrayList<Peminjaman>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<AbsensiKaryawan> daftarAbsenKaryawan = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNik() {
		return nik;
	}

	public void setNik(Integer nik) {
		this.nik = nik;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAgama() {
		return agama;
	}

	public void setAgama(String agama) {
		this.agama = agama;
	}

	public String getJenisKelamin() {
		return jenisKelamin;
	}

	public void setJenisKelamin(String jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}

	public Date gettLahir() {
		return tLahir;
	}

	public void settLahir(Date tLahir) {
		this.tLahir = tLahir;
	}

	public String getTmLahir() {
		return tmLahir;
	}

	public void setTmLahir(String tmLahir) {
		this.tmLahir = tmLahir;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public Double getGaji() {
		return gaji;
	}

	public void setGaji(Double gaji) {
		this.gaji = gaji;
	}

	public String getJabatan() {
		return jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	public Date getTanggalMulaiKerja() {
		return tanggalMulaiKerja;
	}

	public void setTanggalMulaiKerja(Date tanggalMulaiKerja) {
		this.tanggalMulaiKerja = tanggalMulaiKerja;
	}

	public List<Penggajian> getDaftarTerimaGaji() {
		return daftarTerimaGaji;
	}

	public void setDaftarTerimaGaji(List<Penggajian> daftarTerimaGaji) {
		this.daftarTerimaGaji = daftarTerimaGaji;
	}

	public List<Peminjaman> getDaftarPinjamDana() {
		return daftarPinjamDana;
	}

	public void setDaftarPinjamDana(List<Peminjaman> daftarPinjamDana) {
		this.daftarPinjamDana = daftarPinjamDana;
	}

	public List<AbsensiKaryawan> getDaftarAbsenKaryawan() {
		return daftarAbsenKaryawan;
	}

	public void setDaftarAbsenKaryawan(List<AbsensiKaryawan> daftarAbsenKaryawan) {
		this.daftarAbsenKaryawan = daftarAbsenKaryawan;
	}

}
