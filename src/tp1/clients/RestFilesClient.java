package tp1.clients;



import java.net.URI;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

public class RestFilesClient extends RestClient implements Files {
    
    final WebTarget target;

    public RestFilesClient(URI serverURI) {
        super(serverURI);
        target = client.target( serverURI ).path( RestFiles.PATH );
    }


    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        return super.reTry( () -> {clt_writeFile( fileId, data, token ); return null;});   
    }


    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        return super.reTry( () -> {clt_deleteFile( fileId, token ); return null;});
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        return super.reTry( () -> clt_getFile( fileId, token ));
    }


    private Result<Void> clt_writeFile(String fileId, byte[] data, String token) {
		Response r = target.path( fileId )
                .queryParam("token", token)
                .request()
				.post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success write" + r.getStatus());
            return Result.ok();
		} else {
			System.out.println("Error, HTTP error status: write file files" + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
		}
    }

    //TODO
    private Result<Void> clt_deleteFile(String fileId, String token) {
		Response r = target.path( fileId )
                .queryParam("token", token)
                .request()
				.delete();

		if(r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			System.out.println( "Files : " + fileId + " was deleted.");
            return Result.ok();
		} else {
			System.out.println("Error, HTTP error status: delete file files" + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
		}
    }

    private Result<byte[]> clt_getFile(String fileId, String token) {
        Response r = target.path(fileId)
                .queryParam("token", token)
                .request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();

        if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity()) {
            return Result.ok(r.readEntity(new GenericType<byte[]>(){ }));
        }
         else {
            System.out.println("Error, HTTP error status bruh bruh: " + r.getStatus());
            Result.ErrorCode code = Result.ErrorCode.valueOf(Response.Status.fromStatusCode(r.getStatus()).name());
			return Result.error(code);
        }
    }
}
