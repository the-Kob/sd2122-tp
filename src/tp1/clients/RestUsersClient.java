package tp1.clients;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;

public class RestUsersClient extends RestClient implements RestUsers {

	final WebTarget target;
	
	public RestUsersClient( URI serverURI ) {
		super( serverURI );
		target = client.target( serverURI ).path( RestUsers.PATH );
	}
	
	@Override
	public String createUser(User user) {
		return super.reTry( () -> clt_createUser( user ));
	}

	@Override
	public User getUser(String userId, String password) {
		return super.reTry( () -> clt_getUser( userId, password ));
	}

	@Override
	public User updateUser(String userId, String password, User user) {
		return super.reTry( () -> clt_updateUser( userId, password, user ));
	}

	@Override
	public User deleteUser(String userId, String password) {
		return super.reTry( () -> clt_deleteUser( userId, password ));
	}

	@Override
	public List<User> searchUsers(String pattern) {
		return super.reTry( () -> clt_searchUsers( pattern ));
	}

	@Override
	public User searchForUser(String userId) {
		return super.reTry( () -> clt_searchForUser(userId));
	}

	private String clt_createUser( User user) {
		
		Response r = target.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			return r.readEntity(String.class);
		} else {
			System.out.println("Error, HTTP error status: " + r.getStatus());
		}

		return null;
	}

	private User clt_getUser( String userId, String password) {

		User u = null;

		Response r = target.path( userId )
				.queryParam("password", password).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			u = r.readEntity(User.class);
			System.out.println( "User : " + u);
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );

		return u;

	}

	private User clt_updateUser(String userId, String password, User user) {
		
		User u = null;
		
		Response r = target.path( userId )
				.queryParam("password", password).request()
				.accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(user, MediaType.APPLICATION_JSON));

		if(r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			u = r.readEntity(User.class);
			System.out.println( "User : " + u);
		} else {
			System.out.println("Error, HTTP error status: " + r.getStatus());
		}

		return u;
	}

	private User clt_deleteUser(String userId, String password) {
		
		User u = null;
		
		Response r = target.path( userId )
				.queryParam("password", password).request()
				.accept(MediaType.APPLICATION_JSON)
				.delete();

		if(r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			u = r.readEntity(User.class);
			System.out.println( "User : " + u + " was deleted.");
		} else {
			System.out.println("Error, HTTP error status: " + r.getStatus());
		}

		return u;
	}
	
	private List<User> clt_searchUsers(String pattern) {

		List<User> listOfUsers = null;

		Response r = target
				.queryParam("query", pattern)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity()) {
			listOfUsers = r.readEntity(new GenericType<List<User>>() {
			});
		} else {
		System.out.println("Error, HTTP error status: " + r.getStatus());
		}

		return listOfUsers;
	}

	private User clt_searchForUser(String userId) {

		User u = null;

		Response r = target.path( "searchUser/" + userId )
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			u = r.readEntity(User.class);
			System.out.println( "User : " + u);
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );

		return u;
	}
}
