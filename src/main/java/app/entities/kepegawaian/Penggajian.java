package app.entities.kepegawaian;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import app.entities.BasicEntity;
import app.entities.master.DataKaryawan;

@Entity
@Table(name = "gaji_karyawan", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tahun_bulan_penerimaan", "id_karyawan"}, name = "uq_karyawan_gaji")})
public class Penggajian extends BasicEntity {

    public Penggajian() {
        this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @JoinColumn(name = "id_karyawan", nullable = false)
    @ManyToOne
    private DataKaryawan karyawan;

    @Column(name = "tahun_bulan_penerimaan", nullable = false)
    private String tahunBulan;

    @Column(nullable = false, name = "tanggal_penerimaan_gaji")
    private Date tanggal;

    @Column(name = "gaji_pokok", nullable = false)
    private Double gajiPokok;

    @Column(name = "uang_transport", nullable = false)
    private Double uangTransport;

    @Column(name = "jumlah_kehadiran", nullable = false)
    private Integer jumlahKehadiran;

    @Column(name = "uang_lembur", nullable = false)
    private Double uangLembur;

    @Column(name = "jumlah_lembur", nullable = false)
    private Integer jumlahLembur;

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

    public String getTahunBulan() {
        return tahunBulan;
    }

    public void setTahunBulan(String tahunBulan) {
        this.tahunBulan = tahunBulan;
    }

    public Integer getJumlahKehadiran() {
        return jumlahKehadiran;
    }

    public void setJumlahKehadiran(Integer jumlahKehadiran) {
        this.jumlahKehadiran = jumlahKehadiran;
    }

    public Integer getJumlahLembur() {
        return jumlahLembur;
    }

    public void setJumlahLembur(Integer jumlahLembur) {
        this.jumlahLembur = jumlahLembur;
    }

}
