package app.entities;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class AbsensiKaryawan {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@JoinColumn(name = "id_karyawan")
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	private Employee karyawan;

	private Date tanggalHadir;

	private Boolean lembur;

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

}
