package tp1.clients;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import tp1.api.User;
import tp1.server.resources.Discovery;
import util.Debug;

public class CreateUserClient {
	
	private static Logger Log = Logger.getLogger(CreateUserClient.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
	}

	private static final String SERVICE = "UsersService";
	
	public static void main(String[] args) throws IOException {
		
		Debug.setLogLevel( Level.FINE, Debug.SD2122 );

		if( args.length != 4) {
			System.err.println( "Use: java sd2122.aula2.clients.CreateUserClient userId fullName email password");
			return;
		}

		String serverUrl;
		String userId = args[0];
		String fullName = args[1];
		String email = args[2];
		String password = args[3];

		User u = new User( userId, fullName, email, password);

		System.out.println("Sending request to server.");

		Discovery discovery = new Discovery(SERVICE);
		discovery.startListener();

		URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = discovery.knownUrisOf(SERVICE);
		}

		serverUrl = uris[0].toString();

		Log.info("Sending request to server.");

		var result = new RestUsersClient(URI.create(serverUrl)).createUser(u);
		System.out.println("Result: " + result);
	}

}
