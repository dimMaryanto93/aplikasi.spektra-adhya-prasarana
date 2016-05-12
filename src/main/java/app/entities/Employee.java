package app.entities;

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

@Entity
@Table(name = "karyawan")
public class Employee {

	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private Integer nik;

	@Column(nullable = false)
	private String nama;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private Agama agama;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private JenisKelamin jenisKelamin;

	private Date tLahir;
	private String tmLahir;
	private String alamat;
	private Double gaji;

	private Date tanggalMulaiKerja;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<Penggajian> daftarTerimaGaji = new ArrayList<Penggajian>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<Peminjaman> daftarPinjamDana = new ArrayList<Peminjaman>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
	private List<AbsensiKaryawan> daftarAbsenKaryawan = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "kode_jabatan", nullable = false)
	private Jabatan jabatan;
	
	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private Pendidikan pendidikan;
	
	

	public Pendidikan getPendidikan() {
		return pendidikan;
	}

	public void setPendidikan(Pendidikan pendidikan) {
		this.pendidikan = pendidikan;
	}

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
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

	public Agama getAgama() {
		return agama;
	}

	public void setAgama(Agama agama) {
		this.agama = agama;
	}

	public JenisKelamin getJenisKelamin() {
		return jenisKelamin;
	}

	public void setJenisKelamin(JenisKelamin jenisKelamin) {
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
