package app.configs;

import org.controlsfx.control.Notifications;
import org.springframework.stereotype.Component;

import javafx.geometry.Pos;
import javafx.util.Duration;

@Component
public class NotificationDialogs {

	public void showWarningNotification(String title, String text) {
		Notifications.create().title(title).text(text).position(Pos.BOTTOM_RIGHT).hideAfter(Duration.seconds(2D))
				.hideCloseButton().showWarning();
	}

}
