package app.configs;

import org.controlsfx.validation.ValidationSupport;

public interface BootFormInitializable extends BootInitializable {
	/**
	 * harus ditambahkan @Autowired secara manual
	 * 
	 * @param notif
	 *            digunakan untuk menampilkan dialog atau notifikasi
	 */
	public void setValidationSupport(ValidationSupport validation);
}
