package tp1.clients;

import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

public class SoapUsersClient extends SoapClient implements Users {

    public SoapUsers users;

    public SoapUsersClient( URI serverURI ) throws MalformedURLException {
        super(serverURI, new QName(SoapUsers.NAMESPACE, SoapUsers.NAME));

        SoapUsers users = service.getPort(SoapUsers.class);
        applyTimeouts(users);

        this.users = users;
    }

    @Override
    public Result<String> createUser(User user) {
        return super.reTry( () -> clt_createUser( user ));
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        return super.reTry( () -> clt_getUser( userId, password ));
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        return super.reTry( () -> clt_updateUser( userId, password, user ));
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        return super.reTry( () -> clt_deleteUser( userId, password ));
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        return super.reTry( () -> clt_searchUsers( pattern ));
    }

    @Override
    public Result<Boolean> doesUserExist(String userId) {
        return super.reTry( () -> clt_doesUserExist(userId));
    }

    private Result<String> clt_createUser(User user) {
        try {
            return Result.ok(users.createUser(user));
        } catch (UsersException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<User> clt_getUser(String userId, String password) {
        try {
            return Result.ok(users.getUser(userId, password));
        } catch (UsersException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<User> clt_updateUser(String userId, String password, User user) {
        try {
            return Result.ok(users.updateUser(userId, password, user));
        } catch (UsersException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<User> clt_deleteUser(String userId, String password) {
        try {
            return Result.ok(users.deleteUser(userId, password));
        } catch (UsersException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<List<User>> clt_searchUsers(String pattern) {
        try {
            return Result.ok(users.searchUsers(pattern));
        } catch (UsersException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<Boolean> clt_doesUserExist(String userId) {
        try {
            return Result.ok(users.doesUserExist(userId));
        } catch (UsersException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }
}
