package tp1.server.resources;


import java.util.List;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Users;
import tp1.api.service.util.Result.ErrorCode;


@Singleton
public class UsersResource implements RestUsers {

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());

	final Users impl;


	public UsersResource() {
		impl = new JavaUsers();
	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);

		var result = impl.createUser(user);
		
		if(result.isOK())
			return result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
	}


	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);

		var result = impl.getUser(userId, password);
		
		if(result.isOK())
			return result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
	}


	@Override
	public User updateUser(String userId, String password, User user) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; user = " + user);

		var result = impl.updateUser(userId, password, user);
		
		if(result.isOK())
			return result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}

	}


	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);

		var result = impl.deleteUser(userId, password);
		
		if(result.isOK())
			return result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}

	}


	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);

		var result = impl.searchUsers(pattern);
		
		if(result.isOK())
			return result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}

	}

	@Override
	public User searchForUser(String userId) {

		var result = impl.searchForUser(userId);
		
		if(result.isOK())
			return result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
	}
}
