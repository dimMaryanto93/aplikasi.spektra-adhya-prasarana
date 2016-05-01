package app.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class PeminjamanDetail {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@JoinColumn(name = "id_pinjam")
	@OneToOne
	private Peminjaman pinjam;

	private Date tanggalBayar;

	private Double bayar;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Peminjaman getPinjam() {
		return pinjam;
	}

	public void setPinjam(Peminjaman pinjam) {
		this.pinjam = pinjam;
	}

	public Date getTanggalBayar() {
		return tanggalBayar;
	}

	public void setTanggalBayar(Date tanggalBayar) {
		this.tanggalBayar = tanggalBayar;
	}

	public Double getBayar() {
		return bayar;
	}

	public void setBayar(Double bayar) {
		this.bayar = bayar;
	}

}
