package app.entities;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import app.entities.master.DataKaryawan;

@Entity
@Table(name = "kasbon_karyawan")
public class KasbonKaryawan {
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@JoinColumn(name = "id_karyawan", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	private DataKaryawan karyawan;

	@Column(nullable = false, name = "tanggal_pinjam")
	private Date tanggalPinjam;

	@Column(nullable = false)
	private Double debit;

	@Column(nullable = false)
	private Double credit;

	@Column(nullable = false)
	private Double saldoTerakhir;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataKaryawan getKaryawan() {
		return karyawan;
	}

	public void setKaryawan(DataKaryawan karyawan) {
		this.karyawan = karyawan;
	}

	public Date getTanggalPinjam() {
		return tanggalPinjam;
	}

	public void setTanggalPinjam(Date tanggalPinjam) {
		this.tanggalPinjam = tanggalPinjam;
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Double getSaldoTerakhir() {
		return saldoTerakhir;
	}

	public void setSaldoTerakhir(Double saldoTerakhir) {
		this.saldoTerakhir = saldoTerakhir;
	}

}
