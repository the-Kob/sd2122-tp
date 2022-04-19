package tp1.server.resources;

import java.net.URI;

import java.util.*;
import java.util.logging.Logger;

import jakarta.inject.Singleton;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.clients.RestUsersClient;

@Singleton
public class DirectoryResource implements RestDirectory {

    private static final String USER_SERVICE = "users";

    private static Logger Log = Logger.getLogger(DirectoryResource.class.getName());

    private final Map<String, FileInfo> files;
    private final Map<String, List<FileInfo>> userFiles;

    public DirectoryResource(){
        files = new HashMap<>();
        userFiles = new HashMap<>();
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {

        FileInfo newFile = new FileInfo(userId, filename, userId + "_" + filename, new HashSet<String>() );

        Discovery discovery = new Discovery(USER_SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(USER_SERVICE);
        }

        String serverUrl = uris[0].toString();

        User user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        // Check if the file already exists
        if(files.containsKey(filename)) {
            FileInfo file = files.get(filename);

            // Check the original owner of the file
            if(file.getOwner().equals(user.getUserId())) {
                file = newFile;

                userFiles.get(userId).add(file);
            } else {
                Log.info("User isn't the owner of the original file.");
                throw new WebApplicationException( Response.Status.BAD_REQUEST );
            }
        } else {
            files.put(filename, newFile);

            // Check if the user is already "registered"
            if(!userFiles.containsKey(user.getUserId())) {
                userFiles.put(user.getUserId(), new LinkedList<>());
            }

            userFiles.get(user.getUserId()).add(newFile);

            // Search for the server in which the file is stored
        }

        return newFile;
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {

        // Check if the file doesn't exist
        if(!files.containsKey(filename)) {
            Log.info("File doesn't exist.");
            throw new WebApplicationException( Response.Status.BAD_REQUEST );
        }

        FileInfo file = files.get(filename);

        Discovery discovery = new Discovery(USER_SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(USER_SERVICE);
        }

        String serverUrl = uris[0].toString();

        User user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        // Check the owner of the file
        if(!file.getOwner().equals(user.getUserId())) {
            Log.info("User isn't the owner of the file.");
            throw new WebApplicationException( Response.Status.BAD_REQUEST );
        }

        files.remove(filename);
        userFiles.get(user.getUserId()).remove(file);
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        // Check if the file doesn't exist
        if(!files.containsKey(filename)) {
            Log.info("File doesn't exist.");
            throw new WebApplicationException( Response.Status.BAD_REQUEST );
        }

        FileInfo file = files.get(filename);

        Discovery discovery = new Discovery(USER_SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(USER_SERVICE);
        }

        String serverUrl = uris[0].toString();

        User owner = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        User user = new RestUsersClient(URI.create(serverUrl)).searchForUser(userIdShare);

        // Check the owner of the file
        if(!file.getOwner().equals(owner.getUserId())) {
            Log.info("User isn't the owner of the file.");
            throw new WebApplicationException( Response.Status.BAD_REQUEST );
        }

        // Check if the user is already "registered"
        if(!userFiles.containsKey(user.getUserId())) {

            // Check if the file hasn't been shared with the user before
            if(!userFiles.get(user.getUserId()).contains(file)){
                userFiles.put(user.getUserId(), new LinkedList<>());
            }

            userFiles.get(user.getUserId()).add(file);
        }
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        // Check if the file doesn't exist
        if(!files.containsKey(filename)) {
            Log.info("File doesn't exist.");
            throw new WebApplicationException( Response.Status.BAD_REQUEST );
        }

        FileInfo file = files.get(filename);

        Discovery discovery = new Discovery(USER_SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(USER_SERVICE);
        }

        String serverUrl = uris[0].toString();

        User owner = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);
        User user = new RestUsersClient(URI.create(serverUrl)).searchForUser(userIdShare);

        // Check the owner of the file
        if(!file.getOwner().equals(owner.getUserId())) {
            Log.info("User isn't the owner of the file.");
            throw new WebApplicationException( Response.Status.BAD_REQUEST );
        }

        // Check if the file hasn't been shared with the user before
        if(userFiles.get(user.getUserId()).contains(file)){
            userFiles.get(user.getUserId()).remove(file);
        }
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        // Check if the file doesn't exist
        if(!files.containsKey(filename)) {
            Log.info("File doesn't exist.");
            throw new WebApplicationException( Response.Status.BAD_REQUEST );
        }

        FileInfo file = files.get(filename);

        Discovery discovery = new Discovery(USER_SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(USER_SERVICE);
        }

        String serverUrl = uris[0].toString();

        User user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        // Check if the user has access to the file
        if(!userFiles.get(user.getUserId()).contains(file)){
            Log.info("User doesn't have access to the file.");
            throw new WebApplicationException( Response.Status.BAD_REQUEST );
        }

        return null;
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        Discovery discovery = new Discovery(USER_SERVICE);
        discovery.startListener();

        URI[] uris = new URI[] {null};

        while(uris[0] == null) {
            uris = discovery.knownUrisOf(USER_SERVICE);
        }

        String serverUrl = uris[0].toString();

        User user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        return userFiles.get(user.getUserId());
    }
}
