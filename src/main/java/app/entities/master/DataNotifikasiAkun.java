package app.entities.master;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import app.entities.BasicEntity;

@Entity
@Table(name = "data_notifikasi")
public class DataNotifikasiAkun extends BasicEntity {

	public DataNotifikasiAkun() {
		this.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		this.setLastUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
	}

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_response", nullable = false)
	private DataAkun response;

	@Column(name = "notifikasi_kepada", nullable = false)
	private DataJenisAkun request;

	@Column(name = "pesan", nullable = false)
	private String message;

	@Column(name = "severity", nullable = false)
	private DataSeverity severity;

	@Column(name = "baca", nullable = false)
	private Boolean readable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataAkun getResponse() {
		return response;
	}

	public void setResponse(DataAkun response) {
		this.response = response;
	}

	public DataJenisAkun getRequest() {
		return request;
	}

	public void setRequest(DataJenisAkun request) {
		this.request = request;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(DataSeverity severity) {
		this.severity = severity;
	}

	public Boolean getReadable() {
		return readable;
	}

	public void setReadable(Boolean readable) {
		this.readable = readable;
	}

}
