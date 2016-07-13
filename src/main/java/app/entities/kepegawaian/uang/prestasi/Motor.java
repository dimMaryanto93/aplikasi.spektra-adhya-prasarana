package app.entities.kepegawaian.uang.prestasi;

import java.sql.Date;
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
@Table(name = "data_motor")
public class Motor extends BasicEntity {

    public Motor() {
        this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "no_polisi", length = 20)
    private String noPolisi;

    @Column(name = "merek_motor", nullable = false, length = 50)
    private String merkMotor;

    @Column(name = "jenis_motor", nullable = false, length = 50)
    private String typeMotor;

    @Column(name = "warna_motor", nullable = false, length = 25)
    private String warnaMotor;

    @Column(name = "nomor_rangka_motor", nullable = false, length = 50)
    private String noRangka;

    @Column(name = "nomor_mesin_motor", nullable = false, length = 50)
    private String noMesin;

    @Column(name = "nama_dealer", nullable = false, length = 25)
    private String dealer;

    @Column(name = "alamat_dealer")
    private String alamatDealer;

    @Column(name = "tanggal_pesan", nullable = false)
    private Date tanggalPesan;

    @Column(name = "total_angsuran")
    private Integer totalAngsuran;

    @Column(name = "downpayment", nullable = false)
    private Double dp;

    @Column(name = "pembayaran", nullable = false)
    private Double pembayaran;

    @Column(name = "sudah_diterima", nullable = false)
    private Boolean sudahDiterima;

    @Column(name = "disetujui", nullable = false)
    private Boolean setuju;

    @Column(name = "waktu_disetujui")
    private Timestamp acceptTime;

    @Deprecated
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "motor", orphanRemoval = true)
    private List<PembayaranCicilanMotor> daftarCicilan = new ArrayList<>();

    public Double getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(Double pembayaran) {
        this.pembayaran = pembayaran;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoPolisi() {
        return noPolisi;
    }

    public void setNoPolisi(String noPolisi) {
        this.noPolisi = noPolisi;
    }

    public Date getTanggalPesan() {
        return tanggalPesan;
    }

    public void setTanggalPesan(Date tanggalPesan) {
        this.tanggalPesan = tanggalPesan;
    }

    public Integer getTotalAngsuran() {
        return totalAngsuran;
    }

    public void setTotalAngsuran(Integer totalAngsuran) {
        this.totalAngsuran = totalAngsuran;
    }

    public Double getDp() {
        return dp;
    }

    public void setDp(Double dp) {
        this.dp = dp;
    }

    public String getMerkMotor() {
        return merkMotor;
    }

    public void setMerkMotor(String merkMotor) {
        this.merkMotor = merkMotor;
    }

    public Boolean isSudahDiterima() {
        return sudahDiterima;
    }

    public void setSudahDiterima(Boolean sudahDiterima) {
        this.sudahDiterima = sudahDiterima;
    }

    public Boolean isSetuju() {
        return setuju;
    }

    public void setSetuju(Boolean setuju) {
        this.setuju = setuju;
    }

    public List<PembayaranCicilanMotor> getDaftarCicilan() {
        return daftarCicilan;
    }

    public void setDaftarCicilan(List<PembayaranCicilanMotor> daftarCicilan) {
        this.daftarCicilan = daftarCicilan;
    }

    public Timestamp getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Timestamp acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getTypeMotor() {
        return typeMotor;
    }

    public void setTypeMotor(String typeMotor) {
        this.typeMotor = typeMotor;
    }

    public String getWarnaMotor() {
        return warnaMotor;
    }

    public void setWarnaMotor(String warnaMotor) {
        this.warnaMotor = warnaMotor;
    }

    public String getNoRangka() {
        return noRangka;
    }

    public void setNoRangka(String noRangka) {
        this.noRangka = noRangka;
    }

    public String getNoMesin() {
        return noMesin;
    }

    public void setNoMesin(String noMesin) {
        this.noMesin = noMesin;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getAlamatDealer() {
        return alamatDealer;
    }

    public void setAlamatDealer(String alamatDealer) {
        this.alamatDealer = alamatDealer;
    }

}
