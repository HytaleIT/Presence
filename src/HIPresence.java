import club.minnced.discord.rpc.DiscordRPC;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class HIPresence {

	public static final String APP_LINK = "https://hytale.it";

	public static final String APP_IDENTIFIER = "747162403879518308";
	
	public static void main(String[] args) {
		Platform.setImplicitExit(false);

		DiscordRPC discordLibrary = DiscordRPC.INSTANCE;
		discordLibrary.Discord_Initialize(APP_IDENTIFIER, null, true, null);

		Thread discordThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				discordLibrary.Discord_RunCallbacks();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ignored) {
				}
			}
		});
		discordThread.setName("RPC-Callback-Handler");
		discordThread.setDaemon(true);
		discordThread.start();

		new JFXPanel();
		new HIPresenceWindow();
	}

}
