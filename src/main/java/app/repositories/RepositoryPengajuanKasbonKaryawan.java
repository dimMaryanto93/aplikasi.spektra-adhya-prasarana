package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.PengajuanKasbon;

public interface RepositoryPengajuanKasbonKaryawan extends CrudRepository<PengajuanKasbon, String> {

    public List<PengajuanKasbon> findAll();

}
