package app.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.Jabatan;
import java.util.List;

public interface JabatanRepository extends PagingAndSortingRepository<Jabatan, String> {

	public List<Jabatan> findAll();
}
