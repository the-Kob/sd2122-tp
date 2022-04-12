package tp1.clients;



import java.net.URI;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.rest.RestUsers;

public class RestFilesClient extends RestClient implements RestFiles {
    
    final WebTarget target;

    RestFilesClient(URI serverURI) {
        super(serverURI);
        target = client.target( serverURI ).path( RestUsers.PATH );
    }


    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        super.reTry( () -> {clt_writeFile( fileId, data, token ); return null;});
        
    }


    @Override
    public void deleteFile(String fileId, String token) {
        super.reTry( () -> {clt_deleteFile( fileId, token ); return null;});
        
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        return super.reTry( () -> clt_getFile( fileId, token ));
    }


    private void clt_writeFile(String fileId, byte[] data, String token) {

		Response r = target.path( fileId ).request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(Entity.json(null), MediaType.APPLICATION_JSON));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success " + r.getStatus());

		} else {
			System.out.println("Error, HTTP error status: " + r.getStatus());
		}
    }

    //TODO
    private void clt_deleteFile(String fileId, String token) {
		Response r = target.path( fileId ).request()
				.accept(MediaType.APPLICATION_JSON)
				.delete();

		if(r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");

			System.out.println( "Files : " + fileId + " was deleted.");
		} else {
			System.out.println("Error, HTTP error status: " + r.getStatus());
		}
    }

    //TODO
    private byte[] clt_getFile(String fileId, String token) {
        Response r = target.path(fileId)
        .request()
        .accept(MediaType.APPLICATION_JSON)
        .get();

        if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity()) {
            return null; //TODO
        }
         else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
        }

        return null;
    }
}
