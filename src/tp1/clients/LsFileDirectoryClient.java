package tp1.clients;

import tp1.server.resources.Discovery;
import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LsFileDirectoryClient {
    private static Logger Log = Logger.getLogger((LsFileDirectoryClient.class.getName()));

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private static final String SERVICE = "directory";

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if (args.length != 2) {
            System.err.println("Use: java tp1.clients.LsFileDirectoryClient userId password");
            return;
        }

        String serverUrl;
        String userId = args[0];
        String password = args[1];

        System.out.println("Sending request to server.");

        Discovery discovery = new Discovery(SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] { null };

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(SERVICE);
        }

        serverUrl = uris[0].toString();

        Log.info("Sending request to server.");

        var result = new RestDirectoryClient(URI.create(serverUrl)).lsFile(userId, password);
        System.out.println("Result: " + result);
    }
}
