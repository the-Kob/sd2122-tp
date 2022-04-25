package tp1.server;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.xml.ws.Endpoint;
import tp1.server.resources.Discovery;
import tp1.server.resources.FilesWebService;
import util.Debug;

public class SOAPFilesServer {

    private static Logger Log = Logger.getLogger(RESTUsersServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static final int PORT = 8080;
    public static final String SERVICE = "files";
    private static final String SERVER_URI_FMT = "http://%s:%s/soap";
	
	public static void main(String[] args) throws Exception{

        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

        Debug.setLogLevel(Level.INFO, Debug.SD2122);

        String ip = InetAddress.getLocalHost().getHostAddress();
        String serverURI = String.format(SERVER_URI_FMT, ip, PORT);

        Discovery discovery = new Discovery();
        discovery.startAnnounce(SERVICE, serverURI);
		
        //ALTERAR PARA CLASSE SOAP RESOURCE
        Endpoint.publish(serverURI, new FilesWebService(discovery));
    }
}