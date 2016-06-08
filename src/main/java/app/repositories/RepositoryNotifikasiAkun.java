package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.master.DataAkun;
import app.entities.master.DataNotifikasiAkun;

public interface RepositoryNotifikasiAkun extends CrudRepository<DataNotifikasiAkun, String> {

	public List<DataNotifikasiAkun> findAll();

	public List<DataNotifikasiAkun> findByResponseAndReadableIsFalse(DataAkun response);

}
