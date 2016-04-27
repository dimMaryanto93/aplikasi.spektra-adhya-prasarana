package app.entities;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue
	private Long id;
	private int nik;
	private String name;
	private String agama;
	private String jenisKelamin;
	private Date tLahir;
	private String tmLahir;
	private String alamat;
	private BigDecimal gaji;
	private String jabatan;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNik() {
		return nik;
	}

	public void setNik(int nik) {
		this.nik = nik;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public BigDecimal getGaji() {
		return gaji;
	}

	public void setGaji(BigDecimal gaji) {
		this.gaji = gaji;
	}

	public String getJabatan() {
		return jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

}
