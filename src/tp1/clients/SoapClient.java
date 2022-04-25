
package tp1.clients;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

import javax.xml.namespace.QName;

public class SoapClient {
    private static Logger Log = Logger.getLogger(SoapClient.class.getName());

    protected static final int READ_TIMEOUT = 10000;
    protected static final int CONNECT_TIMEOUT = 10000;

    protected static final int RETRY_SLEEP = 1000;
    protected static final int MAX_RETRIES = 3;

    final URI serverURI;
    final Client client;
    final ClientConfig config;

    final Service service;

    SoapClient(URI serverURI, QName qname) throws MalformedURLException {
        this.serverURI = serverURI;
        this.config = new ClientConfig();

        config.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
        config.property( ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);

        this.client = ClientBuilder.newClient(config);

        URL urlswdl;

        try {
            urlswdl = URI.create(serverURI + "?wsdl")
                    .toURL();
            URLConnection conn = null;

            conn = urlswdl.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.connect();
        } catch (IOException e) {
            throw new MalformedURLException();
        }

        this.service = Service.create(urlswdl, qname);
    }

    protected <T> T reTry(Supplier<T> func) {
        for (int i = 0; i < MAX_RETRIES; i++)
            try {
                return func.get(); // Success
            } catch (WebServiceException x) {
                Log.fine("WebServiceException: " + x.getMessage());
                sleep(RETRY_SLEEP);
            } catch (Exception x) {
                // Handle other errors
                Log.fine("Exception: " + x.getMessage());
                x.printStackTrace();
                break;
            }
        return null; // Failure
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException x) { // nothing to do...
        }
    }

    protected <T> void applyTimeouts(T service) {
        Map<String, Object> requestContext = ((BindingProvider) service).getRequestContext();
        requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
        requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
    }
}
