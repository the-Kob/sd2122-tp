package tp1.clients;

import tp1.server.resources.Discovery;

import java.io.IOException;
import java.net.URI;

public class GetUserClient {

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private static final String SERVICE = "users";

    public static void main(String[] args) throws IOException {

        if( args.length != 2) {
            System.err.println( "Use: java sd2122.aula2.clients.GetUserClient userId password");
            return;
        }

        String serverUrl;
        String userId = args[0];
        String password = args[1];

        System.out.println("Sending request to server.");

        Discovery discovery = new Discovery(SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(SERVICE);
        }

        serverUrl = uris[0].toString();

        new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);
    }
}
