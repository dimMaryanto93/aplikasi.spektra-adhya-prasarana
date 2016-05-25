package app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.entities.kepegawaian.uang.prestasi.Motor;
import app.entities.kepegawaian.uang.prestasi.PembayaranCicilanMotor;

public interface CicilanMotorRepository extends CrudRepository<PembayaranCicilanMotor, String> {

	public List<PembayaranCicilanMotor> findByMotor(Motor motor);
}
