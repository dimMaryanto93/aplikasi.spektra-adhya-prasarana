package app.entities.master;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import app.entities.BasicEntity;
import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.kepegawaian.KehadiranKaryawan;
import app.entities.kepegawaian.PengajuanKasbon;
import app.entities.kepegawaian.Penggajian;
import app.entities.kepegawaian.uang.prestasi.Motor;

@Entity
@Table(name = "data_karyawan")
public class DataKaryawan extends BasicEntity {

    public DataKaryawan() {
        this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        this.setAktifBekerja(true);
    }

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(unique = true, nullable = false)
    private String index;

    @Column(nullable = false, name = "no_kepegawaian", unique = true)
    private String nip;

    @Column(nullable = false, name = "no_kependudukan", unique = true)
    private String nik;

    @Column(nullable = false, name = "nama_karyawan")
    private String nama;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private DataAgama agama;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, name = "jenis_kelamin")
    private DataJenisKelamin jenisKelamin;

    @Column(nullable = false, name = "tanggal_lahir")
    private Date tanggalLahir;

    @Column(name = "tempat_lahir", nullable = false)
    private String tempatLahir;

    @Column(name = "alamat_lengkap")
    private String alamat;

    @Column(name = "gaji_pokok", nullable = false)
    private Double gajiPokok;

    @Column(name = "tanggal_mulai_kerja", nullable = false)
    private Date tanggalMulaiKerja;

    @OneToOne
    @JoinColumn(name = "kode_motor")
    private Motor ngicilMotor;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
    private List<Penggajian> daftarTerimaGaji = new ArrayList<Penggajian>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
    private List<KasbonKaryawan> daftarKasbon = new ArrayList<KasbonKaryawan>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "karyawan", orphanRemoval = true)
    private List<KehadiranKaryawan> daftarAbsenKaryawan = new ArrayList<KehadiranKaryawan>();

    @ManyToOne
    @JoinColumn(name = "kode_jabatan", nullable = false)
    private DataJabatan jabatan;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private DataPendidikan pendidikan;

    @OneToOne
    @JoinColumn(name = "kode_pengajuan_kasbon")
    private PengajuanKasbon pengajuanKasbon;

    @Column(name = "aktif_bekerja", nullable = false)
    private Boolean aktifBekerja;

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public DataPendidikan getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(DataPendidikan pendidikan) {
        this.pendidikan = pendidikan;
    }

    public DataJabatan getJabatan() {
        return jabatan;
    }

    public void setJabatan(DataJabatan jabatan) {
        this.jabatan = jabatan;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public DataAgama getAgama() {
        return agama;
    }

    public void setAgama(DataAgama agama) {
        this.agama = agama;
    }

    public DataJenisKelamin getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(DataJenisKelamin jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public PengajuanKasbon getPengajuanKasbon() {
        return pengajuanKasbon;
    }

    public void setPengajuanKasbon(PengajuanKasbon pengajuanKasbon) {
        this.pengajuanKasbon = pengajuanKasbon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Date getTanggalMulaiKerja() {
        return tanggalMulaiKerja;
    }

    public Double getGajiPokok() {
        return gajiPokok;
    }

    public void setGajiPokok(Double gajiPokok) {
        this.gajiPokok = gajiPokok;
    }

    public void setTanggalMulaiKerja(Date tanggalMulaiKerja) {
        this.tanggalMulaiKerja = tanggalMulaiKerja;
    }

    public List<KasbonKaryawan> getDaftarKasbon() {
        return daftarKasbon;
    }

    public void setDaftarKasbon(List<KasbonKaryawan> daftarKasbon) {
        this.daftarKasbon = daftarKasbon;
    }

    public List<KehadiranKaryawan> getDaftarAbsenKaryawan() {
        return daftarAbsenKaryawan;
    }

    public void setDaftarAbsenKaryawan(List<KehadiranKaryawan> daftarAbsenKaryawan) {
        this.daftarAbsenKaryawan = daftarAbsenKaryawan;
    }

    @Deprecated
    public Double getTotalPeminjaman() {
        Double value = 0D;
        for (KasbonKaryawan kasbon : this.daftarKasbon) {
            value += kasbon.getPinjaman();
        }
        return value;
    }

    @Deprecated
    public Double getTotalPembayaran() {
        Double value = 0D;
        for (KasbonKaryawan kasbon : this.daftarKasbon) {
            value += kasbon.getPembayaran();
        }
        return value;
    }

    @Deprecated
    public Double getTotalSaldoTerakhir() {
        return getTotalPeminjaman() - getTotalPembayaran();
    }

    public Motor getNgicilMotor() {
        return ngicilMotor;
    }

    public void setNgicilMotor(Motor ngicilMotor) {
        this.ngicilMotor = ngicilMotor;
    }

    public Boolean isGettingCicilanMotor() {
        Long tahun = ChronoUnit.YEARS.between(getTanggalMulaiKerja().toLocalDate(), LocalDate.now());
        return tahun >= 2 && getNgicilMotor() == null;
    }

    public Boolean isGettingCililanMotorUntukDisetujui() {
        return getNgicilMotor() != null && !getNgicilMotor().isSetuju();
    }

    public Boolean isGettingCicilanMotorDisetujui() {
        return getNgicilMotor() != null && getNgicilMotor().isSetuju();
    }

    public List<Penggajian> getDaftarTerimaGaji() {
        return daftarTerimaGaji;
    }

    public void setDaftarTerimaGaji(List<Penggajian> daftarTerimaGaji) {
        this.daftarTerimaGaji = daftarTerimaGaji;
    }

    public Boolean isAktifBekerja() {
        return aktifBekerja;
    }

    public void setAktifBekerja(Boolean aktifBekerja) {
        this.aktifBekerja = aktifBekerja;
    }

}
