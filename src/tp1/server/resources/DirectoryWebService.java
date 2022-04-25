package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

import java.util.List;

public class DirectoryWebService implements SoapDirectory {
    private Discovery discovery;

    final Directory impl;

    public DirectoryWebService(Discovery discovery){
        this.discovery = discovery;
        this.discovery.startListener();
        impl = new JavaDirectory(this.discovery);
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws DirectoryException {
        var result = impl.writeFile(filename, data, userId, password);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new DirectoryException( result.error().name());
        } else {
            throw new DirectoryException( result.error().name());
        }
    }

    @Override
    public void deleteFile(String filename, String userId, String password) throws DirectoryException {
        var result = impl.deleteFile(filename, userId, password);

        if(result.isOK())
            result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new DirectoryException( result.error().name());
        } else {
            throw new DirectoryException( result.error().name());
        }
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) throws DirectoryException {
        var result = impl.shareFile(filename, userId, userIdShare, password);

        if(result.isOK())
            result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new DirectoryException( result.error().name());
        } else {
            throw new DirectoryException( result.error().name());
        }
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) throws DirectoryException {
        var result = impl.unshareFile(filename, userId, userIdShare, password);

        if(result.isOK())
            result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new DirectoryException( result.error().name());
        } else {
            throw new DirectoryException( result.error().name());
        }
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) throws DirectoryException {
        var result = impl.getFile(filename, userId, accUserId, password);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new DirectoryException( result.error().name());
        } else {
            throw new DirectoryException( result.error().name());
        }
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) throws DirectoryException {
        var result = impl.lsFile(userId, password);

        if(result.isOK())
            return result.value();
        else if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new DirectoryException( result.error().name());
        } else {
            throw new DirectoryException( result.error().name());
        }
    }

    @Override
    public void removeUser(String userId, String password) throws DirectoryException {
        var result = impl.removeUser(userId, password);

        if(result.error().equals(Result.ErrorCode.BAD_REQUEST)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.NOT_FOUND)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.CONFLICT)){
            throw new DirectoryException( result.error().name());
        } else if(result.error().equals(Result.ErrorCode.FORBIDDEN)){
            throw new DirectoryException( result.error().name());
        } else {
            throw new DirectoryException( result.error().name());
        }
    }
}
