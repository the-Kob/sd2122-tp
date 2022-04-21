package tp1.server.resources;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;
import tp1.clients.RestUsersClient;

public class JavaDirectory implements Directory {
    
    private static final String USER_SERVICE = "users";

    private final Map<String, FileInfo> files;
    private final Map<String, List<FileInfo>> userFiles;

    public JavaDirectory(){
        files = new HashMap<String, FileInfo>();
        userFiles = new HashMap<String, List<FileInfo>>();
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        FileInfo newFile = new FileInfo(userId, filename, userId + "_" + filename, new HashSet<String>() );

        URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = Discovery.getInstance().knownUrisOf(USER_SERVICE);
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
                return Result.error(ErrorCode.BAD_REQUEST);
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

        return Result.ok(newFile);
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        // Check if the file doesn't exist
        if(!files.containsKey(filename)) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        FileInfo file = files.get(filename);

        URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = Discovery.getInstance().knownUrisOf(USER_SERVICE);
		}

        String serverUrl = uris[0].toString();

        User user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        // Check the owner of the file
        if(!file.getOwner().equals(user.getUserId())) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        files.remove(filename);
        userFiles.get(user.getUserId()).remove(file);

        return Result.ok();

    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        // Check if the file doesn't exist
        if(!files.containsKey(filename)) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        FileInfo file = files.get(filename);

        URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = Discovery.getInstance().knownUrisOf(USER_SERVICE);
		}

        String serverUrl = uris[0].toString();

        User owner = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        User user = new RestUsersClient(URI.create(serverUrl)).searchForUser(userIdShare);

        // Check the owner of the file
        if(!file.getOwner().equals(owner.getUserId())) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        // Check if the user is already "registered"
        if(!userFiles.containsKey(user.getUserId())) {

            // Check if the file hasn't been shared with the user before
            if(!userFiles.get(user.getUserId()).contains(file)){
                userFiles.put(user.getUserId(), new LinkedList<>());
            }

            userFiles.get(user.getUserId()).add(file);
        }

        return Result.ok();
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        // Check if the file doesn't exist
        if(!files.containsKey(filename)) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        FileInfo file = files.get(filename);

        URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = Discovery.getInstance().knownUrisOf(USER_SERVICE);
		}

        String serverUrl = uris[0].toString();

        User owner = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);
        User user = new RestUsersClient(URI.create(serverUrl)).searchForUser(userIdShare);

        // Check the owner of the file
        if(!file.getOwner().equals(owner.getUserId())) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        // Check if the file hasn't been shared with the user before
        if(userFiles.get(user.getUserId()).contains(file)){
            userFiles.get(user.getUserId()).remove(file);
        }

        return Result.ok();
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        // Check if the file doesn't exist
        if(!files.containsKey(filename)) {
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        FileInfo file = files.get(filename);

        URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = Discovery.getInstance().knownUrisOf(USER_SERVICE);
		}

        String serverUrl = uris[0].toString();

        User user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        // Check if the user has access to the file
        if(!userFiles.get(user.getUserId()).contains(file)){
            return Result.error(ErrorCode.BAD_REQUEST);
        }

        return null;
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {

        URI[] uris = new URI[] {null};

		while(uris[0] == null) {
			uris = Discovery.getInstance().knownUrisOf(USER_SERVICE);
		}

        String serverUrl = uris[0].toString();

        User user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        return Result.ok(userFiles.get(user.getUserId()));
    }

}
