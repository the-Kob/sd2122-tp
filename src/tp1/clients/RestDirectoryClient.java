package tp1.clients;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

import java.net.URI;
import java.util.HashSet;
import java.util.List;

public class RestDirectoryClient extends RestClient implements Directory {

    final WebTarget target;

    RestDirectoryClient(URI serverURI) {
        super(serverURI);
        target = client.target( serverURI ).path(RestDirectory.PATH);
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry( () -> clt_writeFile( filename, data, userId, password ));
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        return super.reTry( () -> {clt_deleteFile( filename, userId, password ); return null;});
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {clt_shareFile( filename, userId, userIdShare, password ); return null;});
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry( () -> {clt_unshareFile( filename, userId, userIdShare, password ); return null;});
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        return super.reTry( () -> clt_getFile( filename, userId, accUserId, password ));
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        return super.reTry( () -> clt_lsFile( userId, password ));
    }

    private Result<FileInfo> clt_writeFile(String filename, byte[] data, String userId, String password) {
        
        FileInfo f = new FileInfo(userId, filename,"directory/files" + userId + "_" + filename, new HashSet<>());

        Response r = target.path(f.getFileURL())
        .queryParam("password", password).request()
        .accept(MediaType.APPLICATION_JSON)
        .post(Entity.entity(f, MediaType.APPLICATION_OCTET_STREAM));

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            return Result.ok(r.readEntity(FileInfo.class));
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
        }
    }

    private Result<FileInfo> clt_deleteFile(String filename, String userId, String password) {
        Response r = target.path(userId + "/" + filename)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success:");
            return Result.ok(r.readEntity(FileInfo.class));
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
        }
    }

    private Result<Void> clt_shareFile(String filename, String userId, String userIdShare, String password) {
        Response r = target.path(userId + "/" + filename + "/share/" + userIdShare)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(null));

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success:");
            return Result.ok();
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
        }
    }

    private Result<Void> clt_unshareFile(String filename, String userId, String userIdShare, String password) {
        Response r = target.path(userId + "/" + filename + "/share/" + userIdShare)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success:");
            return Result.ok();
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
        }
    }

    // QUESTION: How do you return the data of the file if the data isn't stored in the object FileInfo
    private Result<byte[]> clt_getFile(String filename, String userId, String accUserId, String password) {
        Response r = target.path(userId + "/" + filename)
                .queryParam("accUserId", accUserId).queryParam("password", password).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
            System.out.println("Success.");
            return Result.ok(r.readEntity(new GenericType<byte[]>() {}));
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
        }
    }

    private Result<List<FileInfo>> clt_lsFile(String userId, String password) {
        Response r = target.path(userId)
                .queryParam("password", password).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity()) {
            return Result.ok(r.readEntity(new GenericType<List<FileInfo>>() {
            }));
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
        }
    }
}
