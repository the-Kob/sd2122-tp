package tp1.server.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import tp1.api.User;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;
import tp1.api.service.util.Users;
import tp1.clients.ClientFactory;
import tp1.clients.RestDirectoryClient;

public class JavaUsers implements Users {

    private Discovery disc;
    private final Map<String, User> users;

    public JavaUsers(Discovery discovery) {
        this.disc = discovery;
        users = new ConcurrentHashMap<String, User>();
    }

    @Override
    public Result<String> createUser(User user) {

        if (user.getUserId() == null || user.getPassword() == null || user.getFullName() == null ||
                user.getEmail() == null) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        // Check if userId already exists
        if (users.containsKey(user.getUserId())) {
            return Result.error(ErrorCode.CONFLICT);
        }

        //Add the user to the map of users
        users.put(user.getUserId(), user);


        return Result.ok(user.getUserId());
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        return retrieveUser(userId, password);
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {

        Result<User> retUser = retrieveUser(userId, password);

        if (!retUser.isOK()) {
            return retUser;
        }

        User mainUser = retrieveUser(userId, password).value();

        String newPwd = user.getPassword();
        String newFullName = user.getFullName();
        String newEmail = user.getEmail();

        if (newPwd != null) {
            mainUser.setPassword(newPwd);
        }

        if (newFullName != null) {
            mainUser.setFullName(newFullName);
        }

        if (newEmail != null) {
            mainUser.setEmail(newEmail);
        }

        return Result.ok(mainUser);
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        // Remove user's files
        URI[] directoryURIs = disc.knownUrisOf("directory");

        Result<Directory> dir = ClientFactory.getDirectoryClient(directoryURIs[0]);

        if(dir.isOK()) {
            dir.value().removeUser(userId, password);
        } else{
            return Result.error(dir.error());
        }

        Result<User> retUser = retrieveUser(userId, password);

        if (!retUser.isOK()) {
            return retUser;
        }

        User mainUser = retrieveUser(userId, password).value();


        users.remove(mainUser.getUserId());


        return Result.ok(mainUser);
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {

        if (pattern == null) {
            return Result.ok(new ArrayList<User>(users.values()));
        }


        if (users.isEmpty()) {
            return Result.ok(new ArrayList<User>());
        }


        List<User> patternedUsers = new ArrayList<>();


        for (Map.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();

            String fullNameCaps = user.getFullName().toUpperCase();
            String patternToUpper = pattern.toUpperCase();

            if (fullNameCaps.contains(patternToUpper)) {
                patternedUsers.add(user);
            }
        }


        return Result.ok(patternedUsers);
    }

    public Result<Boolean> doesUserExist(String userId) {
        if (userId == null) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }


        User user = users.get(userId);

        if (user == null) {
            return Result.error(ErrorCode.NOT_FOUND);
        }


        return Result.ok(true);
    }

    private Result<User> retrieveUser(String userId, String password) {
        if (userId == null) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }


        User user = users.get(userId);

        if (user == null) {
            System.out.println("user doesnt exist");
            return Result.error(ErrorCode.NOT_FOUND);
        }

        if (password == null) {
            return Result.error(ErrorCode.FORBIDDEN);

        }

        if (!user.getPassword().equals(password)) {
            return Result.error(ErrorCode.FORBIDDEN);
        }

        return Result.ok(user);
    }

}
