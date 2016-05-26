package app.entities.kepegawaian;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.entities.master.DataKaryawan;

@Entity
@Table(name = "gaji_karyawan")
public class Penggajian {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@JoinColumn(name = "id_karyawan")
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	private DataKaryawan karyawan;

	@Column(nullable = false)
	private Date tanggal;

	@Column(name = "saldo", nullable = false)
	private Double gaji;

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

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public Double getGaji() {
		return gaji;
	}

	public void setGaji(Double gaji) {
		this.gaji = gaji;
	}

}
