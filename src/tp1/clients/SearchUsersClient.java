package tp1.clients;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import tp1.clients.RestUsersClient;
import tp1.server.resources.Discovery;
import util.Debug;

public class SearchUsersClient {
	
	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
	}

	private static final String SERVICE = "users";
	
	public static void main(String[] args) throws IOException {

		Debug.setLogLevel(Level.FINE, Debug.SD2122);

		if (args.length != 1) {
			System.err.println("Use: java sd2122.aula3.clients.SearchUsersClient userId ");
			return;
		}

		String serverUrl;
		String userId = args[0];

		System.out.println("Sending request to server.");

		Discovery discovery = new Discovery(SERVICE);
		discovery.startListener();

		URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = discovery.knownUrisOf(SERVICE);
		}

		serverUrl = uris[0].toString();

		new RestUsersClient(URI.create(serverUrl)).searchUsers(userId);

	}

}
