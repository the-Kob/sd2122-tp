package tp1.server.resources;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.server.RESTUsersServer;

public class DirectoryResource implements RestDirectory {

    public DirectoryResource(){
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
