package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.uang.prestasi.Motor;

public interface RepositoryPengajuanAngsuranPrestasi extends CrudRepository<Motor, String> {

    public List<Motor> findAll();

}
