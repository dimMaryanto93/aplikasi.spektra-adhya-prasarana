package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Jabatan;
import app.repositories.JabatanRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Service
public class JabatanService {
	@Autowired
	private JabatanRepository repo;

	public ObservableList<Jabatan> getAll() throws Exception {
		return FXCollections.observableArrayList(repo.findAll());
	}

	public void delete(Jabatan j) throws Exception {
		repo.delete(j);
	}

}
