package app.entities.kepegawaian;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import app.entities.master.DataKaryawan;
import app.entities.master.DataTidakHadir;

@Entity
@Table(name = "absensi_karyawan", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "tanggal_hadir", "id_karyawan" }, name = "uq_hadir_karyawan") })
public class KehadiranKaryawan {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@JoinColumn(name = "id_karyawan")
	@OneToOne
	private DataKaryawan karyawan;

	@Column(name = "tanggal_hadir", nullable = false)
	private Date tanggalHadir;

	@Column(nullable = false)
	private Boolean hadir;

	@Column(nullable = false)
	private Boolean lembur;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "keterangan")
	private DataTidakHadir ket;

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

	public Date getTanggalHadir() {
		return tanggalHadir;
	}

	public void setTanggalHadir(Date tanggalHadir) {
		this.tanggalHadir = tanggalHadir;
	}

	public Boolean getLembur() {
		return lembur;
	}

	public void setLembur(Boolean lembur) {
		this.lembur = lembur;
	}

	public Boolean getHadir() {
		return hadir;
	}

	public void setHadir(Boolean hadir) {
		this.hadir = hadir;
	}

	public DataTidakHadir getKet() {
		return ket;
	}

	public void setKet(DataTidakHadir ket) {
		this.ket = ket;
	}

}
