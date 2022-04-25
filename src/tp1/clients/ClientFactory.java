package tp1.clients;

import tp1.api.service.util.Directory;
import tp1.api.service.util.Files;
import tp1.api.service.util.Users;

import java.net.URI;

public class ClientFactory {
    public static Users getUsersClient(URI serverURI) {
        var s = serverURI.toString();
        if(s.endsWith("rest")) {
            return new RestUsersClient(serverURI);
        } else {
            return new SoapUsersClient(serverURI);
        }
    }

    public static Directory getDirectoryClient(URI serverURI) {
        var s = serverURI.toString();
        if(s.endsWith("rest")) {
            return new RestDirectoryClient(serverURI);
        } else {
            return new SoapDirectoryClient(serverURI);
        }
    }

    public static Files getFilesClient(URI serverURI) {
        var s = serverURI.toString();
        if(s.endsWith("rest")) {
            return new RestFilesClient(serverURI);
        } else {
            return new SoapFilesClient(serverURI);
        }
    }
}
