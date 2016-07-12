package app.entities.kepegawaian.uang.prestasi;

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

@Entity
@Table(name = "cicilan_motor", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"kode_motor", "pembayaran_angsuran_ke"}, name = "uq_angsuran")})
public class PembayaranCicilanMotor extends BasicEntity {

    public PembayaranCicilanMotor() {
        this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "tanggal_bayar", nullable = false)
    private Date tanggalBayar;

    @ManyToOne
    @JoinColumn(name = "kode_motor", nullable = false)
    private Motor motor;

    @Column(nullable = false)
    private Double bayar;

    @Column(nullable = false, name = "pembayaran_angsuran_ke")
    private Integer angsuranKe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTanggalBayar() {
        return tanggalBayar;
    }

    public void setTanggalBayar(Date tanggalBayar) {
        this.tanggalBayar = tanggalBayar;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public Double getBayar() {
        return bayar;
    }

    public void setBayar(Double bayar) {
        this.bayar = bayar;
    }

    public Integer getAngsuranKe() {
        return angsuranKe;
    }

    public void setAngsuranKe(Integer angsuranKe) {
        this.angsuranKe = angsuranKe;
    }

}
