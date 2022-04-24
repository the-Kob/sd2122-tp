package tp1.server.resources;

import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;
import tp1.clients.RestUsersClient;
import tp1.clients.RestFilesClient;

public class JavaDirectory implements Directory {

    private static final String USER_SERVICE = "users";
    private static final String FILE_SERVICE = "files";

    private Discovery disc;
    private Map<String, URI> files;
    private Map<String, List<FileInfo>> userFiles;

    public JavaDirectory(Discovery discovery){
        this.disc = discovery;
        this.disc.startListener();
        files = new HashMap<String, URI>();
        userFiles = new HashMap<String, List<FileInfo>>();
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        URI[] userURIs = disc.knownUrisOf(USER_SERVICE);

        Result<User> user = new RestUsersClient(userURIs[0]).getUser(userId, password);

        URI[] filesURIs = disc.knownUrisOf(FILE_SERVICE);

        // Choose a random file URI
        Random r = new Random();
        URI fileURI = filesURIs[r.nextInt(filesURIs.length)];
        String fileURL = String.format("%s%s/%s_%s", fileURI.toString(), RestFiles.PATH, userId.replace(".", "_"), filename);

        if(!user.isOK()) {
            return Result.error(user.error());
        }

        String fileId = String.format("%s_%s", userId.replace(".", "_"), filename);
        FileInfo file;

        // Check if user already exists
        if(userFiles.containsKey(userId)) {
            file = searchForFile(userId, filename);

            if(file != null) {

                var res = new RestFilesClient(fileURI).writeFile(fileId, data, "");

                return Result.ok(file);
            }
        } else {
            userFiles.put(userId, new LinkedList<FileInfo>());
        }

        file = new FileInfo(userId, filename, fileURL, new HashSet<String>());
        userFiles.get(userId).add(file);

        if(!files.containsKey(fileId)) {
            files.put(fileId, fileURI);
        }

        return Result.ok(file);
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        URI[] userURIs = disc.knownUrisOf(USER_SERVICE);

        Result<User> user = new RestUsersClient(userURIs[0]).getUser(userId, password);

        if(!user.isOK()) {
            return Result.error(user.error());
        }

        String fileId = String.format("%s_%s", userId.replace(".", "_"), filename);

        // Check if user exists
        if(userFiles.containsKey(userId)) {
            FileInfo file  = searchForFile(userId, filename);

            // Check if file exists
            if(file != null) {
                var res = new RestFilesClient(files.get(filename)).deleteFile(fileId, "");

                userFiles.get(userId).remove(file);
                files.remove(filename);
            } else {
                //File doesn't exist
                return Result.error(ErrorCode.NOT_FOUND);
            }
        } else {
            // User doesn't exist
            return Result.error(ErrorCode.NOT_FOUND);
        }

        return Result.ok();
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        URI[] userURIs = disc.knownUrisOf(USER_SERVICE);

        Result<User> owner = new RestUsersClient(userURIs[0]).getUser(userId, password);

        if(!owner.isOK()) {
            return Result.error(owner.error());
        }

        Result<User> user = new RestUsersClient(userURIs[0]).searchForUser(userIdShare);

        if(!user.isOK()) {
            return Result.error(user.error());
        }

        FileInfo file = searchForFile(userId, filename);

        if(file == null) {
            // File doesn't exist
            return Result.error(ErrorCode.NOT_FOUND);
        }

        // Check if file has already been shared with the user
        if(!file.getSharedWith().contains(userIdShare)) {
            file.getSharedWith().add(userIdShare);
        }

        return Result.ok();
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        URI[] userURIs = disc.knownUrisOf(USER_SERVICE);

        Result<User> owner = new RestUsersClient(userURIs[0]).getUser(userId, password);

        if(!owner.isOK()) {
            return Result.error(owner.error());
        }

        Result<User> user = new RestUsersClient(userURIs[0]).searchForUser(userIdShare);

        if(!user.isOK()) {
            return Result.error(user.error());
        }

        FileInfo file = searchForFile(userId, filename);

        if(file == null) {
            // File doesn't exist
            return Result.error(ErrorCode.NOT_FOUND);
        }

        // Check if file has already been shared with the user
        if(file.getSharedWith().contains(userIdShare)) {
            file.getSharedWith().remove(userIdShare);
        }

        return Result.ok();
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        URI[] userURIs = disc.knownUrisOf(USER_SERVICE);

        Result<User> user = new RestUsersClient(userURIs[0]).getUser(accUserId, password);

        if(!user.isOK()) {
            return Result.error(user.error());
        }

        Result<User> owner = new RestUsersClient(userURIs[0]).searchForUser(userId);

        if(!owner.isOK()) {
            return Result.error(owner.error());
        }

        if(!userFiles.containsKey(userId)) {
            // Owner doesn't have files
            System.out.println("owner doesnt have files");
            return Result.error(ErrorCode.NOT_FOUND);
        }

        FileInfo file = searchForFile(userId, filename);

        if(file == null) {
            // File doesn't exist
            System.out.println("file doesnt exist");
            return Result.error(ErrorCode.NOT_FOUND);
        }

        // Check if user has access to the file, either by being its owner or by having the file being shared with him
        if(!accUserId.equals(file.getOwner()) && !file.getSharedWith().contains(accUserId)) {
            return Result.error(ErrorCode.FORBIDDEN);
        }

        String fileId = String.format("%s_%s", userId.replace(".", "_"), filename);

        URI fileURI = files.get(fileId);

        return new RestFilesClient(fileURI).getFile(fileId, "");
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        URI[] userURIs = disc.knownUrisOf(USER_SERVICE);

        Result<User> user = new RestUsersClient(userURIs[0]).getUser(userId, password);

        if(!user.isOK()) {
            return Result.error(user.error());
        }

        if(!userFiles.containsKey(userId)) {
            // User doesn't have files
            return Result.error(ErrorCode.NOT_FOUND);
        }

        return Result.ok(userFiles.get(userId));
    }

    private FileInfo searchForFile(String userId, String filename) {
        List<FileInfo> filesU = userFiles.get(userId);

        for(FileInfo f: filesU) {
            if(f.getFilename().equals(filename)) {
                return f;
            }
        }

        return null;
    }

    @Override
    public Result<Void> removeUser(String userId, String password) {
        URI[] userURIs = disc.knownUrisOf(USER_SERVICE);

        Result<User> user = new RestUsersClient(userURIs[0]).getUser(userId, password);

        if(!user.isOK()) {
            return Result.error(user.error());
        }

        // Remove the specified user and all its files
        List<FileInfo> userF = userFiles.get(userId);

        for (FileInfo file: userF) {
            String fileId = String.format("%s_%s", userId.replace(".", "_"), file.getFilename());

            URI fileURI = files.get(fileId);
            new RestFilesClient(fileURI).deleteFile(fileId, "");
        }

        userFiles.remove(userId);

        // Remove the specified user from all the sharedWith lists from the files
        for(List<FileInfo> fileList: userFiles.values()) {
            for (FileInfo file: fileList) {
                if(file.getSharedWith().contains(userId)) {
                    file.getSharedWith().remove(userId);
                }
            }
        }

        return Result.ok();
    }

}
