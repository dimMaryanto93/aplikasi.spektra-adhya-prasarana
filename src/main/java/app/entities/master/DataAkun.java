package app.entities.master;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import app.entities.BasicEntity;

@Entity
@Table(name = "data_akun")
public class DataAkun extends BasicEntity {

    public DataAkun() {
        this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        setEnabled(false);
    }

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "is_enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "uname", length = 25, nullable = false, unique = true)
    private String username;

    @Column(name = "passwd", length = 100, nullable = false)
    private String password;

    @Column(name = "nama_lengkap", length = 50, nullable = false)
    private String fullname;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "jenis_keamanan", nullable = false)
    private DataJenisAkun security;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "data_history")
    @Column(name = "kode_history", nullable = false)
    private List<Timestamp> daftarHistoryLogin = new ArrayList<Timestamp>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "data_aktifitas")
    @Column(name = "kode_aktifitas", nullable = false)
    private List<DataAktifitasAkun> daftarAktifitas = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public DataJenisAkun getSecurity() {
        return security;
    }

    public void setSecurity(DataJenisAkun security) {
        this.security = security;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Timestamp> getDaftarHistoryLogin() {
        return daftarHistoryLogin;
    }

    public void setDaftarHistoryLogin(List<Timestamp> daftarHistoryLogin) {
        this.daftarHistoryLogin = daftarHistoryLogin;
    }

}
