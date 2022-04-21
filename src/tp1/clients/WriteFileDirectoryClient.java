package tp1.clients;

import tp1.server.resources.Discovery;
import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteFileDirectoryClient {

    private static Logger Log = Logger.getLogger((WriteFileDirectoryClient.class.getName()));

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private static final String SERVICE = "directory";

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel( Level.FINE, Debug.SD2122 );

        if( args.length != 4) {
            System.err.println( "Use: java tp1.clients.WriteFileDirectoryClient filename data userId password");
            return;
        }

        String serverUrl;
        String filename = args[0];
        byte[] data = args[1].getBytes();
        String userId = args[2];
        String password = args[3];

        System.out.println("Sending request to server.");

        URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = Discovery.getInstance().knownUrisOf(SERVICE);
		}

        serverUrl = uris[0].toString();

        Log.info("Sending request to server.");

        var result = new RestDirectoryClient(URI.create(serverUrl)).writeFile(filename, data, userId, password);
        System.out.println("Result: " + result);
    }
}
