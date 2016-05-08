package app.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "absensi_karyawan", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "tanggal_hadir", "id_karyawan" }, name = "uq_hadir_karyawan") })

public class AbsensiKaryawan {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@JoinColumn(name = "id_karyawan")
	@OneToOne
	private Employee karyawan;

	@Column(name = "tanggal_hadir", nullable = false)
	private Date tanggalHadir;

	@Column(nullable = false)
	private Boolean hadir;

	@Column(nullable = false)
	private Boolean lembur;

	@Column(name = "keterangan")
	private String ket;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Employee getKaryawan() {
		return karyawan;
	}

	public void setKaryawan(Employee karyawan) {
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

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

}
