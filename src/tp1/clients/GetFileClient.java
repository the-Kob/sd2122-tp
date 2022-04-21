package tp1.clients;

import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetFileClient {
    private static Logger Log = Logger.getLogger((GetFileClient.class.getName()));

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private static final String SERVICE = "files";

    private static String[] fileServers = {"files1", "files2", "files3"};

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if( args.length != 2) {
            System.err.println( "Use: java tp1.clients.GetFileClient fileId token");
            return;
        }

        String serverUrl;
        String fileId = args[0];
        String token = args[1];

        System.out.println("Sending request to server.");

        //URI stuff

        Log.info("Sending request to server.");
        var result = new RestFilesClient(URI.create(serverUrl)).getFile(fileId, token);
        System.out.println("Result: " + result);
    }
}
