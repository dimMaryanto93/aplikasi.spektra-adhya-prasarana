package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.uang.prestasi.Motor;

public interface MotorRepository extends CrudRepository<Motor, String> {

	public List<Motor> findAll();

}
