package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

import java.util.List;
import java.util.logging.Logger;

public class UsersWebService implements SoapUsers {

    private static Logger Log = Logger.getLogger(UsersWebService.class.getName());

    private Discovery discovery;

    final Users impl;

    public UsersWebService(Discovery discovery) {
        this.discovery = discovery;
        this.discovery.startListener();
        impl = new JavaUsers(this.discovery);
    }

    @Override
    public String createUser(User user) throws UsersException {
        Log.info("createUser : " + user);

        var result = impl.createUser(user);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new UsersException(result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new UsersException( result.error().name() );
        } else {
            throw new UsersException( result.error().name() );
        }
    }

    @Override
    public User getUser(String userId, String password) throws UsersException {
        var result = impl.getUser(userId, password);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new UsersException(result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new UsersException( result.error().name() );
        } else {
            throw new UsersException( result.error().name() );
        }
    }

    @Override
    public User updateUser(String userId, String password, User user) throws UsersException {
        var result = impl.updateUser(userId, password, user);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new UsersException(result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new UsersException( result.error().name() );
        } else {
            throw new UsersException( result.error().name() );
        }
    }

    @Override
    public User deleteUser(String userId, String password) throws UsersException {
        var result = impl.deleteUser(userId, password);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new UsersException(result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new UsersException( result.error().name() );
        } else {
            throw new UsersException( result.error().name() );
        }
    }

    @Override
    public List<User> searchUsers(String pattern) throws UsersException {
        var result = impl.searchUsers(pattern);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new UsersException(result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new UsersException( result.error().name() );
        } else {
            throw new UsersException( result.error().name() );
        }
    }

    @Override
    public boolean doesUserExist(String userId) throws UsersException {
        var result = impl.doesUserExist(userId);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new UsersException(result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new UsersException( result.error().name() );
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new UsersException( result.error().name() );
        } else {
            throw new UsersException( result.error().name() );
        }
    }
}
