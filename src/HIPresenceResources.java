import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;

public class HIPresenceResources {

	public static final URL WINDOW_XML;
	public static final URL WINDOW_CSS;

	public static final URL WINDOW_ICON;
	public static final URL WINDOW_LOGO;

	public static final Image WINDOW_TRAY;

	public static final URL WINDOW_FONT_1;
	public static final URL WINDOW_FONT_2;

	static {
		try {

			WINDOW_TRAY = ImageIO.read(HIPresence.class.getResourceAsStream("/it/hytale/presence/resources/image-icon.png"));

			WINDOW_XML = HIPresence.class.getResource("/it/hytale/presence/resources/window.fxml");
			WINDOW_CSS = HIPresence.class.getResource("/it/hytale/presence/resources/window.css");

			WINDOW_LOGO = HIPresence.class.getResource("/it/hytale/presence/resources/image-logo.png");
			WINDOW_ICON = HIPresence.class.getResource("/it/hytale/presence/resources/image-icon.png");

			WINDOW_FONT_1 = HIPresence.class.getResource("/penumbraserif/stylesheet.css");
			WINDOW_FONT_2 = HIPresence.class.getResource("/notosans/stylesheet.css");
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static String getImage(String image) {
		return HIPresence.class.getResource("/it/hytale/presence/resources/images/" + image + ".png").toExternalForm();
	}

}
