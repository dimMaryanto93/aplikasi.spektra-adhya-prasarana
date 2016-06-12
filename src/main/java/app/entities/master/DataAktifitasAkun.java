package app.entities.master;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class DataAktifitasAkun {

	public DataAktifitasAkun(DataStatusAktifitas status, String message) {
		super();
		this.status = status;
		this.message = message;
		setTime(Timestamp.valueOf(LocalDateTime.now()));
	}

	@Enumerated(EnumType.ORDINAL)
	private DataStatusAktifitas status;

	@Column(name = "waktu", nullable = false)
	private Timestamp time;

	@Column(name = "pesan", nullable = false)
	private String message;

	public DataStatusAktifitas getStatus() {
		return status;
	}

	public void setStatus(DataStatusAktifitas status) {
		this.status = status;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
