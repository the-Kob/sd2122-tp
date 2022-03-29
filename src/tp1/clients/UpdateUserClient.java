package tp1.clients;

import tp1.api.User;
import tp1.server.resources.Discovery;

import java.io.IOException;
import java.net.URI;

public class UpdateUserClient {

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private static final String SERVICE = "users";

    public static void main(String[] args) throws IOException {

        if( args.length != 5) {
            System.err.println( "Use: java sd2122.aula2.clients.UpdateUserClient userId oldpwd fullName email password");
            return;
        }

        String serverUrl;

        // User
        String userId = args[0];
        String oldpwd = args[1];

        // New info
        String fullName = args[2];
        String email = args[3];
        String password = args[4];

        User u = new User( userId, fullName, email, password);

        System.out.println("Sending request to server.");

        Discovery discovery = new Discovery(SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(SERVICE);
        }

        serverUrl = uris[0].toString();

        var result = new RestUsersClient(URI.create(serverUrl)).updateUser(userId, oldpwd, u);
        System.out.println("Result: " + result);
    }

}
