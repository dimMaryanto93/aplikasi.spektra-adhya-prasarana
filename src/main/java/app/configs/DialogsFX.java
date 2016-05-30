package app.configs;

import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ExceptionDialog;
import org.springframework.stereotype.Component;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.util.Duration;

@Component
public class DialogsFX {

	private String title;
	private String text;
	private String header;

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * 
	 * @param title
	 * @param header
	 * @param text
	 */
	public void showDialogInformation(String title, String header, String text) {
		Alert notif = new Alert(AlertType.INFORMATION);
		notif.initModality(Modality.APPLICATION_MODAL);
		notif.setTitle(title);
		notif.setHeaderText(header);
		notif.setContentText(text);
		notif.setResizable(true);
		notif.show();
	}

	/**
	 * 
	 * @param title
	 * @param header
	 * @param text
	 */
	public void showDialogWarning(String title, String header, String text) {
		Alert notif = new Alert(AlertType.WARNING);
		notif.initModality(Modality.APPLICATION_MODAL);
		notif.setTitle(title);
		notif.setHeaderText(header);
		notif.setContentText(text);
		notif.setResizable(true);
		notif.show();
	}

	/**
	 * 
	 * @param title
	 * @param header
	 * @param text
	 * @param ex
	 */
	public void showDialogError(String title, String header, String text, Throwable ex) {
		ExceptionDialog notif = new ExceptionDialog(ex.getCause());
		notif.initModality(Modality.APPLICATION_MODAL);
		notif.setTitle(title);
		notif.setHeaderText(header);
		notif.setContentText(text);
		notif.showAndWait();
	}

	/**
	 * 
	 * @param title
	 * @param text
	 */
	public void showNotificationError(String title, String text) {
		Notifications.create().title(title).text(text).position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(3D))
				.hideCloseButton().showError();
	}

	/**
	 * 
	 * @param title
	 * @param text
	 */
	public void showNotificationWarning(String title, String text) {
		Notifications.create().title(title).text(text).position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(2D))
				.showWarning();
	}

	/**
	 * 
	 * @param title
	 * @param text
	 */
	public void showNotificationInformation(String title, String text) {
		Notifications.create().title(title).text(text).position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(2D))
				.showInformation();
	}

	/**
	 * 
	 * @param title
	 *            judul form
	 */
	public void showDefaultSave(String title) {
		setTitle(title);
		setText("Data berhasil disimpan!");
		showNotificationInformation(getTitle(), getText());
	}

	/**
	 * 
	 * @param title
	 * @param ex
	 */
	public void showDefaultErrorSave(String title, Throwable ex) {
		setTitle(title);
		setHeader("Tidak dapat menyimpan atau melakukan perubahan data!");
		showDialogError(getTitle(), getHeader(), null, ex);
	}

	/**
	 * 
	 * @param title
	 * @param ex
	 */
	public void showDefaultErrorLoad(String title, Throwable ex) {
		setTitle(title);
		setHeader("Tidak dapat mendapatkan data!");
		showDialogError(getTitle(), getHeader(), null, ex);
	}

	public void showDefaultErrorLoadForm(String title, Throwable ex) {
		setTitle(title);
		setHeader("Tidak dapat menampilkan scene");
		showDialogError(getTitle(), getHeader(), null, ex);
	}

}
