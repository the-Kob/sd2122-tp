package tp1.clients;

import jakarta.ws.rs.client.WebTarget;
import jakarta.xml.ws.Service;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.util.Users;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.ServiceConfigurationError;

public class SoapUsersClient extends SoapClient implements Users {

    public SoapUsers users;

    public SoapUsersClient( URI serverURI ) throws MalformedURLException {
        super(serverURI, new QName(SoapUsers.NAMESPACE, SoapUsers.NAME));

        SoapUsers users = service.getPort(SoapUsers.class);
        applyTimeouts(users);

        this.users = users;
    }

    @Override
    public String createUser(User user) {
        return null;
    }

    @Override
    public User getUser(String userId, String password) {
        return null;
    }

    @Override
    public User updateUser(String userId, String password, User user) {
        return null;
    }

    @Override
    public User deleteUser(String userId, String password) {
        return null;
    }

    @Override
    public List<User> searchUsers(String pattern) {
        return null;
    }

    @Override
    public boolean doesUserExist(String userId) {
        return false;
    }
}
