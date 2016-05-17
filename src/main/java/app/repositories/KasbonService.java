package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.KasbonKaryawan;

public interface KasbonService extends CrudRepository<KasbonKaryawan, String> {

	public List<KasbonKaryawan> findAll();

}
