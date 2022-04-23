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

        Result<User> user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        if(user.isOK()){
            // Check if the file already exists
            if(files.containsKey(filename)) {
                FileInfo file = files.get(filename);

                // Check the original owner of the file
                if(file.getOwner().equals(user.value().getUserId())) {
                    file = newFile;

                    userFiles.get(userId).add(file);
                } else {
                    return Result.error(ErrorCode.BAD_REQUEST);
                }
            } else {
                files.put(filename, newFile);

                // Check if the user is already "registered"
                if(!userFiles.containsKey(user.value().getUserId())) {
                    userFiles.put(user.value().getUserId(), new LinkedList<>());
                }

                userFiles.get(user.value().getUserId()).add(newFile);

                // Search for the server in which the file is stored
            }
        } else {
			return Result.error(user.error());
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

        Result<User> user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        if(user.isOK()){
            // Check the owner of the file
            if(!file.getOwner().equals(user.value().getUserId())) {
                return Result.error(ErrorCode.BAD_REQUEST);
            }

            files.remove(filename);
            userFiles.get(user.value().getUserId()).remove(file);

            return Result.ok();
        } else{
            return Result.error(user.error());
        }

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

        Result<User> owner = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        Result<User> user = new RestUsersClient(URI.create(serverUrl)).searchForUser(userIdShare);

        if(owner.isOK()){
            if(user.isOK()){
                // Check the owner of the file
                if(!file.getOwner().equals(owner.value().getUserId())) {
                    return Result.error(ErrorCode.BAD_REQUEST);
                }

                // Check if the user is already "registered"
                if(!userFiles.containsKey(user.value().getUserId())) {

                    // Check if the file hasn't been shared with the user before
                    if(!userFiles.get(user.value().getUserId()).contains(file)){
                        userFiles.put(user.value().getUserId(), new LinkedList<>());
                    }

                    userFiles.get(user.value().getUserId()).add(file);
                }
            }else{
                return Result.error(user.error());
            } 
        }   else{
            return Result.error(owner.error());
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

        Result<User> owner = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);
        Result<User> user = new RestUsersClient(URI.create(serverUrl)).searchForUser(userIdShare);

        if(owner.isOK()){
            if(user.isOK()){
                // Check the owner of the file
                if(!file.getOwner().equals(owner.value().getUserId())) {
                    return Result.error(ErrorCode.BAD_REQUEST);
                }

                // Check if the file hasn't been shared with the user before
                if(userFiles.get(user.value().getUserId()).contains(file)){
                    userFiles.get(user.value().getUserId()).remove(file);
                }
            }else{
                return Result.error(user.error());
            }
        }else{
            return Result.error(owner.error());
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

        Result<User> user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        if(user.isOK()){
        // Check if the user has access to the file
            if(!userFiles.get(user.value().getUserId()).contains(file)){
                return Result.error(ErrorCode.BAD_REQUEST);
            }
        }else{
            return Result.error(user.error());
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

        Result<User> user = new RestUsersClient(URI.create(serverUrl)).getUser(userId, password);

        if(user.isOK()){
            return Result.ok(userFiles.get(user.value().getUserId()));
        }else{
            return Result.error(user.error());
        }
    }

}
