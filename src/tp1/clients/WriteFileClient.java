package tp1.clients;

import util.Debug;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteFileClient {
    
    /*
    
    private static Logger Log = Logger.getLogger((WriteFileClient.class.getName()));

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private static final String SERVICE = "files";

    private static String[] fileServers = {"files1", "files2", "files3"};

    public static void main(String[] args) throws IOException {

        Debug.setLogLevel(Level.FINE, Debug.SD2122);

        if( args.length != 3) {
            System.err.println( "Use: java tp1.clients.WriteFileClient fileId data token");
            return;
        }

        String serverUrl;
        String fileId = args[0];
        byte[] data = args[1].getBytes();
        String token = args[2];

        System.out.println("Sending request to server.");

        //URI stuff

        Log.info("Sending request to server.");
        var result = new RestFilesClient(URI.create(serverUrl)).writeFile(fileId, data, token);
        System.out.println("Result: " + result);

    }
        */



}
