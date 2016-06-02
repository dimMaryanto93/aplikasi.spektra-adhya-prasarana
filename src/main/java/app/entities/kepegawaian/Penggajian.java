package app.entities.kepegawaian;

import java.sql.Date;
import java.sql.Timestamp;
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

import app.entities.BasicEntity;
import app.entities.master.DataKaryawan;

@Entity
@Table(name = "gaji_karyawan")
public class Penggajian extends BasicEntity {

	public Penggajian() {
		this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
	}

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@Column(name = "slip_gaji", nullable = false, unique = true)
	private String slipGaji;

	@JoinColumn(name = "id_karyawan")
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	private DataKaryawan karyawan;

	@Column(nullable = false, name = "tanggal_penerimaan_gaji")
	private Date tanggal;

	@Column(name = "gaji_pokok", nullable = false)
	private Double gajiPokok;

	@Column(name = "uang_transport", nullable = false)
	private Double uangTransport;

	@Column(name = "uang_lembur", nullable = false)
	private Double uangLembur;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSlipGaji() {
		return slipGaji;
	}

	public void setSlipGaji(String slipGaji) {
		this.slipGaji = slipGaji;
	}

	public DataKaryawan getKaryawan() {
		return karyawan;
	}

	public void setKaryawan(DataKaryawan karyawan) {
		this.karyawan = karyawan;
	}

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public Double getGajiPokok() {
		return gajiPokok;
	}

	public void setGajiPokok(Double gajiPokok) {
		this.gajiPokok = gajiPokok;
	}

	public Double getUangTransport() {
		return uangTransport;
	}

	public void setUangTransport(Double uangTransport) {
		this.uangTransport = uangTransport;
	}

	public Double getUangLembur() {
		return uangLembur;
	}

	public void setUangLembur(Double uangLembur) {
		this.uangLembur = uangLembur;
	}

}
