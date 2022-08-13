
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HIPresenceWindow {

    @FXML
    private StackPane window;

    @FXML
    private BorderPane windowHeader;

    @FXML
    private Circle windowMinify;

    @FXML
    private Circle windowClose;

    @FXML
    private Button windowReset;

    @FXML
    private Button windowUpdate;

    @FXML
    private Hyperlink windowSite;

    @FXML
    private ImageView windowPresenceImageLarge;

    @FXML
    private ImageView windowPresenceImageSmall;

    @FXML
    private Label windowPresenceName;

    @FXML
    private Label windowPresenceDetails;

    @FXML
    private Label windowPresenceState;

    @FXML
    private Label windowPresenceTime;

    @FXML
    private ComboBox<HIPresenceItem> windowControlState;

    @FXML
    private ComboBox<HIPresenceItem> windowControlDetails;

    @FXML
    private ComboBox<HIPresenceItem> windowControlImageLarge;

    @FXML
    private ComboBox<HIPresenceItem> windowControlImageSmall;

    @FXML
    private Spinner<Integer> windowControlPlayerMin;

    @FXML
    private Spinner<Integer> windowControlPlayerMax;

    @FXML
    private ComboBox<HIPresenceItem> windowControlTime;

	private Stage stage;

	private boolean ready;

	private double xOffset, yOffset;

	private TrayIcon trayIcon;

    public HIPresenceWindow() {

    	trayInstall();

    	Platform.runLater(() -> {
    		stage = new Stage();
    		stage.setTitle("Hytale Italia Presence");
    		stage.initStyle(StageStyle.UNDECORATED);

	    	try {
		    	FXMLLoader loader = new FXMLLoader();
		    	loader.setController(this);
		    	loader.load(HIPresenceResources.WINDOW_XML.openStream());
	    	} catch (Exception exception) {
	    		exception.printStackTrace();
	    	}


	    	window.getStylesheets().add(HIPresenceResources.WINDOW_FONT_1.toExternalForm());
	    	window.getStylesheets().add(HIPresenceResources.WINDOW_FONT_2.toExternalForm());
	    	window.getStylesheets().add(HIPresenceResources.WINDOW_CSS.toExternalForm());

	    	stage.setScene(new Scene(window));
	    	stage.getIcons().add(new Image(HIPresenceResources.WINDOW_ICON.toExternalForm()));
	    	stage.show();

	    	windowReset.setVisible(false);

	    	windowHeader.setOnMousePressed(new EventHandler<MouseEvent>() {

				@Override
	            public void handle(MouseEvent event) {
	                xOffset = stage.getX() - event.getScreenX();
	                yOffset = stage.getY() - event.getScreenY();
	            }
	        });

	    	windowHeader.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	stage.setX(event.getScreenX() + xOffset);
	                stage.setY(event.getScreenY() + yOffset);
	            }
	        });

	    	windowClose.setOnMouseClicked(($) -> {
	    		trayRemove();
	    		Platform.exit();
	    		System.exit(0);
	    	});

	    	windowMinify.setOnMouseClicked(($) -> {
	    		stage.hide();
	    	});

	    	windowSite.setOnAction(($) -> {
	    		try {
		    		Desktop desktop = Desktop.getDesktop();
		    		desktop.browse(URI.create(HIPresence.APP_LINK));
	    		} catch (Exception exception) {
	    			exception.printStackTrace();
	    		}
	    	});

	        windowControlImageLarge.getItems()
	        	.addAll(
        			new HIPresenceItem("Hytale Italia", "hytaleit-icon"),
        			new HIPresenceItem("Hytale Italia Sfondo", "hytaleit-icon-bg"),
        			new HIPresenceItem("Hytale", "hytale-icon"),
        			new HIPresenceItem("Hytale Sfondo", "hytale-icon-bg")
	        	);

	        windowControlImageSmall.getItems()
        		.addAll(
        			new HIPresenceItem("Vuoto", null),
        			new HIPresenceItem("Hytale Italia", "hytaleit-icon"),
        			new HIPresenceItem("Hytale Italia Sfondo", "hytaleit-icon-bg"),
        			new HIPresenceItem("Hytale", "hytale-icon"),
        			new HIPresenceItem("Hytale Sfondo", "hytale-icon-bg")
	        	);

	        windowControlState.getItems()
	        	.addAll(
	        		new HIPresenceItem("Modalità Survival"),
	        		new HIPresenceItem("Modalità Creativa"),
	        		new HIPresenceItem("Survival Mode"),
	        		new HIPresenceItem("Creative Mode"),
	        		new HIPresenceItem("Server Hytale Italia"),
	        		new HIPresenceItem("Server Hytale"),
	        		new HIPresenceItem("Vuoto", null)
	        	);

	    	windowControlDetails.getItems()
	    		.addAll(
					new HIPresenceItem("Orbis - Zona 1"),
					new HIPresenceItem("Orbis - Zona 2"),
					new HIPresenceItem("Orbis - Zona 3"),
					new HIPresenceItem("Orbis - Zone 1"),
					new HIPresenceItem("Orbis - Zone 2"),
					new HIPresenceItem("Orbis - Zone 3"),
					new HIPresenceItem("Vuoto", null)
	    		);

	        IntegerSpinnerValueFactory spinerFactoryMax = new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 500, 0);
	        spinerFactoryMax.setWrapAround(true);

	        IntegerSpinnerValueFactory spinerFactoryMin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500, 0);
	        spinerFactoryMin.setWrapAround(true);

	        windowControlPlayerMin.setValueFactory(spinerFactoryMin);
	        windowControlPlayerMax.setValueFactory(spinerFactoryMax);

	    	windowControlTime.getItems()
	    		.addAll(
					new HIPresenceItem("Vuoto", null),
					new HIPresenceItem("Ora", String.valueOf(0)),
					new HIPresenceItem("5 minuti", String.valueOf(5)),
					new HIPresenceItem("15 minuti", String.valueOf(15)),
					new HIPresenceItem("30 minuti", String.valueOf(30)),
					new HIPresenceItem("1 ora", String.valueOf(60 * 1)),
					new HIPresenceItem("5 ore", String.valueOf(60 * 5)),
					new HIPresenceItem("10 ore", String.valueOf(60 * 10)),
					new HIPresenceItem("15 ore", String.valueOf(60 * 15)),
					new HIPresenceItem("1 giorno", String.valueOf(60 * 24 * 1)),
					new HIPresenceItem("2 giorni", String.valueOf(60 * 24 * 2))
	    		);

	    	windowControlImageSmall.valueProperty().addListener(($1, $2, $3) -> updatePresenceUI(false));
	    	windowControlImageLarge.valueProperty().addListener(($1, $2, $3) -> updatePresenceUI(false));

	    	windowControlDetails.valueProperty().addListener(($1, $2, $3) -> updatePresenceUI(false));
	    	windowControlState.valueProperty().addListener(($1, $2, $3) -> updatePresenceUI(false));

	    	windowControlPlayerMin.valueProperty().addListener(($1, $2, $3) -> updatePresenceUI(false));
	    	windowControlPlayerMax.valueProperty().addListener(($1, $2, $3) -> updatePresenceUI(false));

	    	windowControlTime.valueProperty().addListener(($1, $2, $3) -> updatePresenceUI(false));

	    	windowUpdate.setOnAction(($1) -> updatePresenceUI(true));

	    	windowControlDetails.getSelectionModel().selectFirst();
	    	windowControlState.getSelectionModel().selectFirst();

	    	windowControlImageSmall.getSelectionModel().selectFirst();
	    	windowControlImageLarge.getSelectionModel().selectFirst();

	    	windowControlTime.getSelectionModel().selectFirst();

	        ready = true;
	        updatePresenceUI(false);
    	});
	}


    private void trayInstall() {
		SystemTray tray = SystemTray.getSystemTray();

		Dimension trayIconDimension = tray.getTrayIconSize();
		java.awt.Image trayIconImage = HIPresenceResources.WINDOW_TRAY;
		trayIconImage = trayIconImage.getScaledInstance(
			(int) trayIconDimension.getWidth(),
			(int) trayIconDimension.getHeight(),
			java.awt.Image.SCALE_SMOOTH
		);

		trayIcon = new TrayIcon(HIPresenceResources.WINDOW_TRAY, "Hytale Italia Presence", null);
		trayIcon.setToolTip("Hytale Italia Presence");
		trayIcon.setImageAutoSize(true);
		trayIcon.addActionListener(($) -> {
			Platform.runLater(() -> {
				System.out.println("show");
				stage.show();
			});
		});

		try {
		    tray.add(trayIcon);
		} catch (AWTException exception) {
			exception.printStackTrace();
		}
	}

    private void trayRemove() {
    	SystemTray tray = SystemTray.getSystemTray();
    	tray.remove(trayIcon);
    }


	public void updatePresenceUI(boolean update) {
    	if (ready) {

    		DiscordRichPresence discordPresence = new DiscordRichPresence();

    		/* ==================================================================== */

    		// Ottengo lo stato del gioco
	    	HIPresenceItem stateItem = windowControlState.getValue();
	    	String state = stateItem.getItemValue();

	    	// Imposto lo stato sulla presenza
	    	discordPresence.state = state;

	    	// Ottengo il numero di giocatori in gioco
	    	Integer playerMin = windowControlPlayerMin.getValue();
	    	Integer playerMax = windowControlPlayerMax.getValue();

	    	// Se il numero di giocatori massimi e minimi è valido e lo stato è specificato
	    	if (state != null && playerMin > 0 && playerMax > 0) {
	    		state += " (" + playerMin + " di " + playerMax +  ")";

	            discordPresence.partyId = String.valueOf(UUID.randomUUID());
				discordPresence.partySize = playerMin;
				discordPresence.partyMax = playerMax;
	    	}

	    	// Nascondo/Visualizzo lo stato in base al valore
	    	windowPresenceState.setVisible(Objects.nonNull(state));
	    	windowPresenceState.setManaged(Objects.nonNull(state));

	    	// Imposto il valore dello stato
	    	windowPresenceState.setText(state);

	    	/* ==================================================================== */

	    	// Ottengo i dettagli di gioco
	    	HIPresenceItem detailsItem = windowControlDetails.getValue();
	    	String details = detailsItem.getItemValue();

	    	// Imposto i dettagli sulla presenza
	    	discordPresence.details = details;

	    	// Nascondo/Visualizzo i dettagli in base al valore
			windowPresenceDetails.setVisible(Objects.nonNull(details));
			windowPresenceDetails.setManaged(Objects.nonNull(details));

			// Imposto il valore dei dettagli
			windowPresenceDetails.setText(details);

			/* ==================================================================== */

	    	HIPresenceItem imageSmallItem = windowControlImageSmall.getValue();
	    	String imageSmall = imageSmallItem.getItemValue();

	        if (Objects.nonNull(imageSmall)) {
	        	windowPresenceImageSmall.setImage(new Image(HIPresenceResources.getImage(imageSmall)));

		        discordPresence.smallImageKey = imageSmall;
		        discordPresence.smallImageText = "Hytale Italia";
	        } else {
	        	windowPresenceImageSmall.setImage(null);
	        }


	    	/* ==================================================================== */

			HIPresenceItem imageLargeItem = windowControlImageLarge.getValue();
			String imageLarge = imageLargeItem.getItemValue();

	        if (Objects.nonNull(imageLarge)) {
	        	windowPresenceImageLarge.setImage(new Image(HIPresenceResources.getImage(imageLarge)));

		        discordPresence.largeImageKey = imageLarge;
		        discordPresence.largeImageText = "Hytale Italia";
	        } else {
	        	windowPresenceImageLarge.setImage(null);
	        }

			/* ==================================================================== */

			// Ottengo il tempo desiderato di gioco
			HIPresenceItem timeItem = windowControlTime.getValue();
			String time = timeItem.getItemValue();

			if (Objects.nonNull(time)) {
		        long timeComputed = Long.parseLong(time);
		        timeComputed = (System.currentTimeMillis() / 1000) - (timeComputed * 60);
		        discordPresence.startTimestamp = timeComputed;
			}

			// Nascondo/Visualizzo il tempo in base al valore
			windowPresenceTime.setVisible(Objects.nonNull(time));
			windowPresenceTime.setManaged(Objects.nonNull(time));

			// Imposto il valore del tempo
			windowPresenceTime.setText("hh:mm trascorsi");

	        // Imposto la presenza su discord
	        if (update) {
	        	DiscordRPC.INSTANCE.Discord_UpdatePresence(discordPresence);
	        }

    	}
    }

}
