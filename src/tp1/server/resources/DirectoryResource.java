package tp1.server.resources;

import java.net.URI;

import java.util.logging.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.inject.Singleton;

import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.clients.RestUsersClient;

@Singleton
public class DirectoryResource implements RestDirectory {

    private static final String USER_SERVICE = "users";

    private static Logger Log = Logger.getLogger(DirectoryResource.class.getName());

    private final Map<String, FileInfo> files;
    private final Map<String, Set<FileInfo>> userFiles;

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



        return null;
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {

        //Get User, erro se não existir

    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        
        //Get User, erro se não existir
        //Search userIdshare
        
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        //Get User, erro se não existir
        //Search userIdshare

        
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        return null;
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {

        return null;
    }

    private void shareFileWith(String filename, String ownerUserId, String userId) {


    }

    private void unshareFileWith(String filename, String userId) {

    }
}
