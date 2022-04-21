package tp1.server.resources;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.util.*;
import java.util.logging.Logger;


/**
 * <p>A class to perform service discovery, based on periodic service contact endpoint
 * announcements over multicast communication.</p>
 *
 * <p>Servers announce their *name* and contact *uri* at regular intervals. The server actively
 * collects received announcements.</p>
 *
 * <p>Service announcements have the following format:</p>
 *
 * <p>&lt;service-name-string&gt;&lt;delimiter-char&gt;&lt;service-uri-string&gt;</p>
 */
public class Discovery {
    private static Logger Log = Logger.getLogger(Discovery.class.getName());

    private static Discovery discovery = null;

    public static Discovery getInstance(){
        if(discovery == null){
            discovery = new Discovery();
        }

        return discovery;

    }
    static {
        // addresses some multicast issues on some TCP/IP stacks
        System.setProperty("java.net.preferIPv4Stack", "true");
        // summarizes the logging format
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
    }


    // The pre-aggreed multicast endpoint assigned to perform discovery.
    static final InetSocketAddress DISCOVERY_ADDR = new InetSocketAddress("227.227.227.227", 2277);
    static final int DISCOVERY_PERIOD = 1000;
    static final int DISCOVERY_TIMEOUT = 5000;

    // Used separate the two fields that make up a service announcement.
    private static final String DELIMITER = "\t";

    private InetSocketAddress addr;
    private String serviceName;
    private String serviceURI;

    private Map<String, ArrayList<URI>> serviceURIs;
    private Map<String, Long> serviceTimes;

    /**
     * @param  serviceName the name of the service to announce
     * @param  serviceURI an uri string - representing the contact endpoint of the service being announced
     */
    
    public Discovery(String serviceName, String serviceURI) {
        this.serviceName = serviceName;
        this.serviceURI  = serviceURI;
        this.serviceURIs = new HashMap<>();
        this.serviceTimes = new HashMap<>();

        listener();
    }
    
    /*
    public Discovery(String serviceName) {
        this.serviceName = serviceName;
        this.serviceURIs = new HashMap<>();
        this.serviceTimes = new HashMap<>();

        //listener(serviceName, 1);
    }
    */

    public Discovery() {

        this.serviceURIs = new HashMap<>();
        this.serviceTimes = new HashMap<>();

        listener();
    }
    /**
     * Continuously announces a service given its name and uri
     *
     * @param serviceName the composite service name: <domain:service>
     * @param serviceURI - the uri of the service
     */
    public void announce(String serviceName, String serviceURI) {
        Log.info(String.format("Starting Discovery announcements on: %s for: %s -> %s\n", DISCOVERY_ADDR, serviceName, serviceURI));

        var pktBytes = String.format("%s%s%s", serviceName, DELIMITER, serviceURI).getBytes();

        DatagramPacket pkt = new DatagramPacket(pktBytes, pktBytes.length, DISCOVERY_ADDR);
        // start thread to send periodic announcements
        new Thread(() -> {
            try (var ds = new DatagramSocket()) {
                for (;;) {
                    try {
                        Thread.sleep(DISCOVERY_PERIOD);
                        ds.send(pkt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Listens for the given composite service name, blocks until a minimum number of replies is collected.
     * @param serviceName - the composite name of the service
     * @param minRepliesNeeded - the minimum number of replies required.
     * @return the discovery results as an array
     */

    public void listener() {
        Log.info(String.format("Starting discovery on multicast group: %s, port: %d\n", DISCOVERY_ADDR.getAddress(), DISCOVERY_ADDR.getPort()));

        final int MAX_DATAGRAM_SIZE = 65536;
        var pkt = new DatagramPacket(new byte[MAX_DATAGRAM_SIZE], MAX_DATAGRAM_SIZE);

        new Thread(() -> {
            try (var ms = new MulticastSocket(DISCOVERY_ADDR.getPort())) {
                joinGroupInAllInterfaces(ms);
                for(;;) {
                    try {
                        pkt.setLength(MAX_DATAGRAM_SIZE);
                        ms.receive(pkt);

                        var msg = new String(pkt.getData(), 0, pkt.getLength());
                        //System.out.printf( "FROM %s (%s) : %s\n", pkt.getAddress().getCanonicalHostName(),
                        //        pkt.getAddress().getHostAddress(), msg);
                        var tokens = msg.split(DELIMITER);
                        // tokens[0] = serviceName
                        // tokens[1] = (URI) - String format

                        if (tokens.length == 2) {
                            URI uri = URI.create(tokens[1]);
                            if(!this.serviceURIs.containsKey(tokens[0])) {
                                this.serviceURIs.put(tokens[0], new ArrayList<>());
                                this.serviceURIs.get(tokens[0]).add(uri);
                                this.serviceTimes.put(tokens[0], System.currentTimeMillis());
                            } else {
                                if(!this.serviceURIs.get(tokens[0]).contains(uri)) {
                                    this.serviceURIs.get(tokens[0]).add(URI.create(tokens[1]));
                                    this.serviceTimes.put(tokens[0], System.currentTimeMillis());
                                }
                            }
                        }

                        System.out.println(serviceURIs);
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(DISCOVERY_PERIOD);
                        } catch (InterruptedException e1) {
                            // do nothing
                        }
                        Log.finest("Still listening...");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Returns the known servers for a service.
     *
     * @param  serviceName the name of the service being discovered
     * @return an array of URI with the service instances discovered.
     *
     */
    public URI[] knownUrisOf(String serviceName) {
        List<URI> uris = new ArrayList<>();

        if(serviceURIs.containsKey(serviceName)) {
            for (Map.Entry<String, ArrayList<URI>> entry : serviceURIs.entrySet()) {
                ArrayList<URI> knownURIs = entry.getValue();

                if (entry.getKey().equalsIgnoreCase(serviceName)) {
                    for(URI uri : knownURIs) {
                        uris.add(uri);
                    }
                }
            }
            return uris.toArray(new URI[0]);
        } else {
            return new URI[] {null};
        }
    }

    private void joinGroupInAllInterfaces(MulticastSocket ms) throws SocketException {
        Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
        while (ifs.hasMoreElements()) {
            NetworkInterface xface = ifs.nextElement();
            try {
                ms.joinGroup(DISCOVERY_ADDR, xface);
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

    /**
     * Starts sending service announcements at regular intervals...
     */
    public void startAnnounce() {
        announce(serviceName, serviceURI);
    }

    public void startListener() {
        listener();
    }
}
