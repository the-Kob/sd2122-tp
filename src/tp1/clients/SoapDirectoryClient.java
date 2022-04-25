package tp1.clients;

import tp1.api.FileInfo;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

public class SoapDirectoryClient extends SoapClient implements Directory {
    public SoapDirectory directory;

    public SoapDirectoryClient( URI serverURI ) throws MalformedURLException {
        super(serverURI, new QName(SoapDirectory.NAMESPACE, SoapDirectory.NAME));

        SoapDirectory directory = service.getPort(SoapDirectory.class);
        applyTimeouts(directory);

        this.directory = directory;
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry( () -> clt_writeFile( filename, data, userId, password ));
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        return super.reTry( () -> {clt_deleteFile( filename, userId, password ); return null;});
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {clt_shareFile( filename, userId, userIdShare, password ); return null;});
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry( () -> {clt_unshareFile( filename, userId, userIdShare, password ); return null;});
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        return super.reTry( () -> clt_getFile( filename, userId, accUserId, password ));
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        return super.reTry( () -> clt_lsFile( userId, password ));
    }

    @Override
    public Result<Void> removeUser(String userId, String password) {
        return super.reTry( () -> clt_removeUser( userId, password ));
    }

    private Result<FileInfo> clt_writeFile(String filename, byte[] data, String userId, String password) {
        try {
            return Result.ok(directory.writeFile(filename, data, userId, password));
        } catch (DirectoryException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<Void> clt_deleteFile(String filename, String userId, String password) {
        try {
            directory.deleteFile(filename, userId, password);

            return Result.ok();
        } catch (DirectoryException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<Void> clt_shareFile(String filename, String userId, String userIdshare, String password) {
        try {
            directory.shareFile(filename, userId, userIdshare, password);

            return Result.ok();
        } catch (DirectoryException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<Void> clt_unshareFile(String filename, String userId, String userIdShare, String password) {
        try {
            directory.unshareFile(filename, userId, userIdShare, password);

            return Result.ok();
        } catch (DirectoryException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<byte[]> clt_getFile(String filename, String userId, String accUserId, String password) {
        try {
            return Result.ok(directory.getFile(filename, userId, accUserId, password));
        } catch(DirectoryException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<List<FileInfo>> clt_lsFile(String userId, String password) {
        try {
            return Result.ok(directory.lsFile(userId, password));
        } catch(DirectoryException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<Void> clt_removeUser(String userId, String password) {
        try {
            directory.removeUser(userId, password);
            return Result.ok();
        } catch(DirectoryException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

}
