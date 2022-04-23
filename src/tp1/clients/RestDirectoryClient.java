package tp1.clients;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;

import java.io.IOException;
import java.io.InputStream;
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
        super.reTry( () -> {clt_deleteFile( filename, userId, password ); return null;});
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        super.reTry(() -> {clt_shareFile( filename, userId, userIdShare, password ); return null;});
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        super.reTry( () -> {clt_unshareFile( filename, userId, userIdShare, password ); return null;});
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
        
        FileInfo f = new FileInfo(userId, filename,"directory/files" + userId + "_" + filename, new HashSet<>());

        Response r = target.path(f.getFileURL())
        .queryParam("password", password).request()
        .accept(MediaType.APPLICATION_JSON)
        .post(Entity.entity(f, MediaType.APPLICATION_OCTET_STREAM));

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            return r.readEntity(FileInfo.class);
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
        }

        return null;
    }

    private FileInfo clt_deleteFile(String filename, String userId, String password) {
        Response r = target.path(userId + "/" + filename)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success:");
            FileInfo f = r.readEntity(FileInfo.class);
            System.out.println( "File : " + f);
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
        }

        return null;
    }

    private void clt_shareFile(String filename, String userId, String userIdShare, String password) {
        Response r = target.path(userId + "/" + filename + "/share/" + userIdShare)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(null));

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success:");
            FileInfo f = r.readEntity(FileInfo.class);
            System.out.println( "File : " + f);
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
        }
    }

    private void clt_unshareFile(String filename, String userId, String userIdShare, String password) {
        Response r = target.path(userId + "/" + filename + "/share/" + userIdShare)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success:");
            FileInfo f = r.readEntity(FileInfo.class);
            System.out.println( "File : " + f);
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
        }
    }

    // QUESTION: How do you return the data of the file if the data isn't stored in the object FileInfo
    private byte[] clt_getFile(String filename, String userId, String accUserId, String password) {
        Response r = target.path(userId + "/" + filename)
                .queryParam("accUserId", accUserId).queryParam("password", password).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success.");
            return r.readEntity(new GenericType<byte[]>() {});
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
        }

        return null;
    }

    private List<FileInfo> clt_lsFile(String userId, String password) {
        Response r = target.path(userId)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity()) {
            return r.readEntity(new GenericType<List<FileInfo>>() {
            });
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
        }

        return null;
    }
}
