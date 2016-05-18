package app.entities.master;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "data_jabatan")
public class DataJabatan {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;
	private String kodeJabatan;

	@Column(name = "name", nullable = false, unique = true)
	private String nama;

	private String keterangan;
	private Double gapok;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "jabatan", orphanRemoval = true)
	private List<DataKaryawan> daftarKaryawan = new ArrayList<>();

	public List<DataKaryawan> getDaftarKaryawan() {
		return daftarKaryawan;
	}

	public void setDaftarKaryawan(List<DataKaryawan> daftarKaryawan) {
		this.daftarKaryawan = daftarKaryawan;
	}

	public Double getGapok() {
		return gapok;
	}

	public void setGapok(Double gapok) {
		this.gapok = gapok;
	}

	public String getKodeJabatan() {
		return kodeJabatan;
	}

	public void setKodeJabatan(String kodeJabatan) {
		this.kodeJabatan = kodeJabatan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

}