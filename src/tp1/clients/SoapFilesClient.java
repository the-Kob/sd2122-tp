package tp1.clients;

import jakarta.ws.rs.client.WebTarget;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;

public class SoapFilesClient extends SoapClient implements Files {

    public SoapFiles files;

    public SoapFilesClient( URI serverURI ) throws MalformedURLException {
        super(serverURI, new QName(SoapFiles.NAMESPACE, SoapFiles.NAME));

        SoapFiles files = service.getPort(SoapFiles.class);
        applyTimeouts(files);

        this.files = files;
    }

    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        return super.reTry( () -> {clt_writeFile( fileId, data, token ); return null;});
    }


    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        return super.reTry( () -> {clt_deleteFile( fileId, token ); return null;});
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        return super.reTry( () -> clt_getFile( fileId, token ));
    }

    private Result<Void> clt_writeFile(String fileId, byte[] data, String token) {
        try {
            files.writeFile(fileId, data, token);

            return Result.ok();
        } catch (FilesException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<Void> clt_deleteFile(String fileId, String token) {
        try {
            files.deleteFile(fileId, token);

            return Result.ok();
        } catch (FilesException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    private Result<byte[]> clt_getFile(String fileId, String token) {
        try {
            return Result.ok(files.getFile(fileId, token));
        } catch(FilesException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

}
