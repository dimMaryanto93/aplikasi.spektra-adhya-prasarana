package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.PengajuanKasbon;

public interface PengajuanKasbonService extends CrudRepository<PengajuanKasbon, String> {

	public List<PengajuanKasbon> findAll();

}
