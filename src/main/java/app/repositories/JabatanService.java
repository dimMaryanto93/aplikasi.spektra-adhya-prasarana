package app.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.master.DataJabatan;

import java.util.List;

public interface JabatanService extends PagingAndSortingRepository<DataJabatan, String> {

	public List<DataJabatan> findAll();
}
