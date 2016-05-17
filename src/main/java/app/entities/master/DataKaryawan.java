package app.entities.master;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import app.entities.KehadiranKaryawan;
import app.entities.KasbonKaryawan;
import app.entities.Penggajian;

@Entity
@Table(name = "data_karyawan")
public class DataKaryawan {

	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private Integer nik;

	@Column(nullable = false)
	private String nama;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private DataAgama agama;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private DataJenisKelamin jenisKelamin;

	private Date tLahir;
	private String tmLahir;
	private String alamat;
	private Double gaji;

	private Date tanggalMulaiKerja;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<Penggajian> daftarTerimaGaji = new ArrayList<Penggajian>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<KasbonKaryawan> daftarKasbon = new ArrayList<KasbonKaryawan>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<KehadiranKaryawan> daftarAbsenKaryawan = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "kode_jabatan", nullable = false)
	private DataJabatan jabatan;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private DataPendidikan pendidikan;

	@Column(name = "saldo_terakhir", nullable = false)
	private Double saldoTerakhir;

	public DataPendidikan getPendidikan() {
		return pendidikan;
	}

	public void setPendidikan(DataPendidikan pendidikan) {
		this.pendidikan = pendidikan;
	}

	public DataJabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(DataJabatan jabatan) {
		this.jabatan = jabatan;
	}

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

	public DataAgama getAgama() {
		return agama;
	}

	public void setAgama(DataAgama agama) {
		this.agama = agama;
	}

	public DataJenisKelamin getJenisKelamin() {
		return jenisKelamin;
	}

	public void setJenisKelamin(DataJenisKelamin jenisKelamin) {
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

	public List<KasbonKaryawan> getDaftarKasbon() {
		return daftarKasbon;
	}

	public void setDaftarKasbon(List<KasbonKaryawan> daftarKasbon) {
		this.daftarKasbon = daftarKasbon;
	}

	public List<KehadiranKaryawan> getDaftarAbsenKaryawan() {
		return daftarAbsenKaryawan;
	}

	public void setDaftarAbsenKaryawan(List<KehadiranKaryawan> daftarAbsenKaryawan) {
		this.daftarAbsenKaryawan = daftarAbsenKaryawan;
	}

	public Double getSaldoTerakhir() {
		return saldoTerakhir;
	}

	public void setSaldoTerakhir(Double saldoTerakhir) {
		this.saldoTerakhir = saldoTerakhir;
	}

}
