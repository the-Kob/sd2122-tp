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
			throw new WebApplicationException( result.error().name() );
	}


	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);

		return retrieveUser(userId, password);
	}


	@Override
	public User updateUser(String userId, String password, User user) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; user = " + user);

		User mainUser = retrieveUser(userId, password);

		String newPwd = user.getPassword();
		String newFullName = user.getFullName();
		String newEmail = user.getEmail();

		if(newPwd != null) {
			mainUser.setPassword(newPwd);
		}

		if(newFullName != null) {
			mainUser.setFullName(newFullName);
		}

		if(newEmail != null) {
			mainUser.setEmail(newEmail);
		}

		return mainUser;
	}


	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);

		User user = retrieveUser(userId, password);

		return users.remove(user.getUserId());
	}


	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);

		if(pattern == null) {
			return new ArrayList<User>(users.values());
		}

		if(users.isEmpty()) {
			return new ArrayList<>();
		}

		List<User> patternedUsers = new ArrayList<>();

		for(Map.Entry<String, User> entry : users.entrySet()) {
			User user = entry.getValue();

			String fullNameCaps = user.getFullName().toUpperCase();
			String patternToUpper = pattern.toUpperCase();

			if(fullNameCaps.contains(patternToUpper)) {
				patternedUsers.add(user);
			}
		}

		return patternedUsers;
	}

	@Override
	public User searchForUser(String userId) {
		if(userId == null) {
			Log.info("UserId  null.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}

		User user = users.get(userId);

		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}

		return user;
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
