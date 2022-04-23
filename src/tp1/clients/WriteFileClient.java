package tp1.clients;

import tp1.server.resources.Discovery;
import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteFileClient {
    
    private static Logger Log = Logger.getLogger((WriteFileClient.class.getName()));

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private static final String SERVICE = "files";

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if (args.length != 3) {
            System.err.println("Use: java tp1.clients.WriteFileClient fileId data token");
            return;
        }

        String serverUrl;
        String fileId = args[0];
        byte[] data = args[1].getBytes();
        String token = args[2];

        System.out.println("Sending request to server.");

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = Discovery.getInstance().knownUrisOf(SERVICE);
        }

        Random r = new Random();

        serverUrl = uris[r.nextInt(uris.length)].toString();

        Log.info("Sending request to server.");
        var result = new RestFilesClient(URI.create(serverUrl)).writeFile(fileId, data, token);
        System.out.println("Result: " + result);
    }
}
