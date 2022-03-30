package tp1.clients;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;

import java.net.URI;
import java.util.HashSet;
import java.util.List;

public class RestDirectoryClient extends RestClient implements RestDirectory {

    final WebTarget target;

    RestDirectoryClient(URI serverURI) {
        super(serverURI);
        target = client.target( serverURI ).path(RestDirectory.PATH);
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry( () -> clt_writeFile( filename, data, userId, password ));
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
        super.reTry( () -> clt_deleteFile( filename, userId, password ));
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        super.reTry( () -> clt_shareFile( filename, userId, userIdShare, password ));
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        super.reTry( () -> clt_unshareFile( filename, userId, userIdShare, password ));
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        return super.reTry( () -> clt_getFile( filename, userId, accUserId, password ));
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return super.reTry( () -> clt_lsFile( userId, password ));
    }

    private FileInfo clt_writeFile(String filename, byte[] data, String userId, String password) {
        
        FileInfo f = new FileInfo(userId, filename, userId + " " + filename, new HashSet<>());

        Response r = target.path(f.getFileURL())
        .queryParam("password", password).request()
        .accept(MediaType.APPLICATION_JSON)
        .post(Entity.entity(f, MediaType.APPLICATION_JSON));

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Writed File" + r.readEntity(String.class)); 
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
    }

        return null;
    }

    private <T> T clt_deleteFile(String filename, String userId, String password) {
        return null;
    }

    private <T> T clt_shareFile(String filename, String userId, String userIdShare, String password) {
        return null;
    }

    private <T> T clt_unshareFile(String filename, String userId, String userIdShare, String password) {
        return null;
    }

    private <T> T clt_getFile(String filename, String userId, String accUserId, String password) {
        return null;
    }

    private <T> T clt_lsFile(String userId, String password) {
        return null;
    }
}
