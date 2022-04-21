package tp1.server.resources;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;
import tp1.api.service.util.Result.ErrorCode;


@Singleton
public class UsersResource implements RestUsers {

	private final Map<String,User> users = new HashMap<String, User>();

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());

	final Users impl;;


	public UsersResource() {
		impl = new JavaUsers();
	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);

		var result = impl.createUser(user);
		
		if(result.isOK())
			return result.value();
		else
			throw new WebApplicationException( Status.BAD_REQUEST );
	}


	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);

		var result = impl.getUser(userId, password);
		
		if(result.isOK())
			return result.value();
		else
			throw new WebApplicationException( Status.BAD_REQUEST );
	}


	@Override
	public User updateUser(String userId, String password, User user) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; user = " + user);

		var result = impl.updateUser(userId, password, user);
		
		if(result.isOK())
			return result.value();
		else
			throw new WebApplicationException( Status.BAD_REQUEST );

	}


	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);

		var result = impl.deleteUser(userId, password);
		
		if(result.isOK())
			return result.value();
		else
			throw new WebApplicationException( Status.BAD_REQUEST );

	}


	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);

		var result = impl.searchUsers(pattern);
		
		if(result.isOK())
			return result.value();
		else
			throw new WebApplicationException( Status.BAD_REQUEST );

	}

	@Override
	public User searchForUser(String userId) {

		var result = impl.searchForUser(userId);
		
		if(result.isOK())
			return result.value();
		else
			throw new WebApplicationException( Status.BAD_REQUEST );
	}

	private User retrieveUser(String userId, String password) {
		if(userId == null) {
			Log.info("UserId  null.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}

		User user = users.get(userId);

		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}

		if(password == null) {
			Log.info("password null.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		if( !user.getPassword().equals( password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		return user;
	}
}
