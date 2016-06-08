package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.master.DataAkun;

public interface RepositoryAkun extends CrudRepository<DataAkun, String> {

	public List<DataAkun> findAll();

	public DataAkun findByUsernameAndPasswordAndEnabledIsTrue(String username, String password);

	public List<DataAkun> findByEnabledIsFalse();

}
