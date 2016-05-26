package app.entities.kepegawaian;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

	public KasbonKaryawan() {
		setWaktu(Timestamp.valueOf(LocalDateTime.now()));
		setTanggalPinjam(Date.valueOf(LocalDate.now()));
		setPrinted(false);
	}

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
	private Double pinjaman;

	@Column(nullable = false)
	private Double pembayaran;

	@Column(nullable = false)
	private Double saldoTerakhir;

	@Column(nullable = false)
	private Timestamp waktu;

	@Column(name = "dicetak", nullable = false)
	private Boolean printed;

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

	public Double getPinjaman() {
		return pinjaman;
	}

	public void setPinjaman(Double pinjaman) {
		this.pinjaman = pinjaman;
	}

	public Double getPembayaran() {
		return pembayaran;
	}

	public void setPembayaran(Double pembayaran) {
		this.pembayaran = pembayaran;
	}

	public Double getSaldoTerakhir() {
		return saldoTerakhir;
	}

	public void setSaldoTerakhir(Double saldoTerakhir) {
		this.saldoTerakhir = saldoTerakhir;
	}

	public Timestamp getWaktu() {
		return waktu;
	}

	public void setWaktu(Timestamp waktu) {
		this.waktu = waktu;
	}

	public Boolean getPrinted() {
		return printed;
	}

	public void setPrinted(Boolean printed) {
		this.printed = printed;
	}

}
