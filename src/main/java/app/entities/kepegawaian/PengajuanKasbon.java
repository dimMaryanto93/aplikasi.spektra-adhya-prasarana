package app.entities.kepegawaian;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import app.entities.BasicEntity;

@Entity
@Table(name = "pengajuan_kasbon_karyawan")
public class PengajuanKasbon extends BasicEntity {

	public PengajuanKasbon() {
		this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
		this.setAccepted(false);
	}

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@Column(name = "tanggal_transaksi", nullable = false)
	private Date tanggal;

	@Column(name = "jumlah_nominal", nullable = false)
	private Double nominal;

	@Column(name = "disetujui", nullable = false)
	private Boolean accepted;

	@Column(name = "disetujui_pada")
	private Timestamp acceptedDateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public Double getNominal() {
		return nominal;
	}

	public void setNominal(Double nominal) {
		this.nominal = nominal;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public Timestamp getAcceptedDateTime() {
		return acceptedDateTime;
	}

	public void setAcceptedDateTime(Timestamp acceptedDateTime) {
		this.acceptedDateTime = acceptedDateTime;
	}

}
