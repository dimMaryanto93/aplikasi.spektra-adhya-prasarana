package app.configs;

import org.springframework.stereotype.Component;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

@Component
public class FontIconFactory {

	public void createFontAwesomeIcon32px(Button c, FontAwesomeIcon fontAwesomeIcon) {
		FontAwesomeIconView icon = new FontAwesomeIconView();
		icon.setIcon(fontAwesomeIcon);
		icon.setSize("32px");
		c.setGraphic(icon);
	}

	public void createFontAwesomeIcon32px(MenuButton c, FontAwesomeIcon fontAwesomeIcon) {
		FontAwesomeIconView icon = new FontAwesomeIconView();
		icon.setIcon(fontAwesomeIcon);
		icon.setSize("32px");
		c.setGraphic(icon);
	}

	public void createFontAwesomeIcon24px(MenuItem c, FontAwesomeIcon fontAwesomeIcon) {
		FontAwesomeIconView icon = new FontAwesomeIconView();
		icon.setIcon(fontAwesomeIcon);
		icon.setSize("24px");
		c.setGraphic(icon);
	}

	public void createFontAwesomeIcon18px(Menu c, FontAwesomeIcon fontAwesomeIcon) {
		FontAwesomeIconView icon = new FontAwesomeIconView();
		icon.setIcon(fontAwesomeIcon);
		icon.setSize("18px");
		c.setGraphic(icon);
	}

	public void createFontAwesomeIcon18px(MenuItem c, FontAwesomeIcon fontAwesomeIcon) {
		FontAwesomeIconView icon = new FontAwesomeIconView();
		icon.setIcon(fontAwesomeIcon);
		icon.setSize("18px");
		c.setGraphic(icon);
	}

	public void createFontAwesomeIcon18px(Label c, FontAwesomeIcon fontAwesomeIcon) {
		FontAwesomeIconView icon = new FontAwesomeIconView();
		icon.setIcon(fontAwesomeIcon);
		icon.setSize("18px");
		c.setGraphic(icon);
	}

}
