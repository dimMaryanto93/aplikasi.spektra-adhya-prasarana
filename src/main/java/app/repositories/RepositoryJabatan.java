package app.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import app.entities.master.DataJabatan;

public interface RepositoryJabatan extends PagingAndSortingRepository<DataJabatan, String> {

    public List<DataJabatan> findAll();
}
