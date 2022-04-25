package tp1.server.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

public class FilesWebService implements SoapFiles {

    final Files impl = new JavaFiles();

    public FilesWebService(Discovery discovery) {
        discovery.startListener();
    }

    @Override
    public byte[] getFile(String fileId, String token) throws FilesException {
        var result = impl.getFile(fileId, token);

        if (result.isOK()) {
            return result.value();
        } else if (result.error().equals(Result.ErrorCode.BAD_REQUEST)) {
            throw new FilesException( result.error().name());
        } else if (result.error().equals(Result.ErrorCode.NOT_FOUND)) {
            throw new FilesException( result.error().name());
        } else if (result.error().equals(Result.ErrorCode.CONFLICT)) {
            throw new FilesException( result.error().name());
        } else if (result.error().equals(Result.ErrorCode.FORBIDDEN)) {
            throw new FilesException( result.error().name());
        } else {
            throw new FilesException( result.error().name());
        }
    }

    @Override
    public void deleteFile(String fileId, String token) throws FilesException {
        var result = impl.deleteFile(fileId, token);

        if (!result.isOK()) {
            if (result.error().equals(Result.ErrorCode.BAD_REQUEST)) {
                throw new FilesException( result.error().name());
            } else if (result.error().equals(Result.ErrorCode.NOT_FOUND)) {
                throw new FilesException( result.error().name());
            } else if (result.error().equals(Result.ErrorCode.CONFLICT)) {
                throw new FilesException( result.error().name());
            } else if (result.error().equals(Result.ErrorCode.FORBIDDEN)) {
                throw new FilesException( result.error().name());
            } else {
                throw new FilesException( result.error().name());
            }
        }
    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) throws FilesException {
        var result = impl.writeFile(fileId, data, token);

        if (!result.isOK()) {
            if (result.error().equals(Result.ErrorCode.BAD_REQUEST)) {
                throw new FilesException( result.error().name());
            } else if (result.error().equals(Result.ErrorCode.NOT_FOUND)) {
                throw new FilesException( result.error().name());
            } else if (result.error().equals(Result.ErrorCode.CONFLICT)) {
                throw new FilesException( result.error().name());
            } else if (result.error().equals(Result.ErrorCode.FORBIDDEN)) {
                throw new FilesException( result.error().name());
            } else {
                throw new FilesException( result.error().name());
            }
        }
    }
}
