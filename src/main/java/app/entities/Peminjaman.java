package app.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Peminjaman {
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@JoinColumn(name = "id_karyawan")
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	private Employee karyawan;

	private Date tanggalPinjam;

	private Double pinjam;

	private Double sisa;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "pinjam")
	private List<PeminjamanDetail> daftarBayarUtang = new ArrayList<PeminjamanDetail>();

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

	public Date getTanggalPinjam() {
		return tanggalPinjam;
	}

	public void setTanggalPinjam(Date tanggalPinjam) {
		this.tanggalPinjam = tanggalPinjam;
	}

	public Double getPinjam() {
		return pinjam;
	}

	public void setPinjam(Double pinjam) {
		this.pinjam = pinjam;
	}

	public Double getSisa() {
		return sisa;
	}

	public void setSisa(Double sisa) {
		this.sisa = sisa;
	}

}
