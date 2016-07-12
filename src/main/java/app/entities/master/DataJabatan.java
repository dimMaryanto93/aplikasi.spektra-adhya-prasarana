package app.entities.master;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

import app.entities.BasicEntity;

@Entity
@Table(name = "data_jabatan")
public class DataJabatan extends BasicEntity {

    public DataJabatan() {
        this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "kode_jabatan", nullable = false, unique = true)
    private String kodeJabatan;

    @Column(name = "name", nullable = false, unique = true)
    private String nama;

    private String keterangan;

    @Column(name = "gaji_pokok", nullable = false)
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
