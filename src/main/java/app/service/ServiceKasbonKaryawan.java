package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.entities.kepegawaian.KasbonKaryawan;
import app.entities.master.DataKaryawan;
import app.repositories.RepositoryKasbonKaryawan;

@Component
public class ServiceKasbonKaryawan {

    @Autowired
    private RepositoryKasbonKaryawan kasbonKaryawan;

    public Double getTotalPeminjaman(DataKaryawan karyawan) {
        List<KasbonKaryawan> daftarKasbon = kasbonKaryawan.findByKaryawan(karyawan);
        Double value = 0D;
        for (KasbonKaryawan kasbon : daftarKasbon) {
            value += kasbon.getPinjaman();
        }
        return value;
    }

    public Double getTotalPembayaran(DataKaryawan karyawan) {
        List<KasbonKaryawan> daftarKasbon = kasbonKaryawan.findByKaryawan(karyawan);
        Double value = 0D;
        for (KasbonKaryawan kasbon : daftarKasbon) {
            value += kasbon.getPembayaran();
        }
        return value;
    }

    public Double getSaldoTerakhir(DataKaryawan karyawan) {
        return getTotalPeminjaman(karyawan) - getTotalPembayaran(karyawan);
    }

}
