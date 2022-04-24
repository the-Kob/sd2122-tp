package tp1.server.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

public class FilesResource implements RestFiles {

    final Files impl = new JavaFiles();

    public FilesResource(Discovery discovery) {
        discovery.startListener();
    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        var result = impl.writeFile(fileId, data, token);

        if (!result.isOK()) {
            if (result.error().equals(Result.ErrorCode.BAD_REQUEST)) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else if (result.error().equals(Result.ErrorCode.NOT_FOUND)) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else if (result.error().equals(Result.ErrorCode.CONFLICT)) {
                throw new WebApplicationException(Response.Status.CONFLICT);
            } else if (result.error().equals(Result.ErrorCode.FORBIDDEN)) {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            } else {
                throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
            }
        }
    }

    @Override
    public void deleteFile(String fileId, String token) {
        var result = impl.deleteFile(fileId, token);

        if (!result.isOK()) {
            if (result.error().equals(Result.ErrorCode.BAD_REQUEST)) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            } else if (result.error().equals(Result.ErrorCode.NOT_FOUND)) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            } else if (result.error().equals(Result.ErrorCode.CONFLICT)) {
                throw new WebApplicationException(Response.Status.CONFLICT);
            } else if (result.error().equals(Result.ErrorCode.FORBIDDEN)) {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            } else {
                throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
            }
        }
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        var result = impl.getFile(fileId, token);

        if (result.isOK()) {
            return result.value();
        }else if (result.error().equals(Result.ErrorCode.BAD_REQUEST)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } else if (result.error().equals(Result.ErrorCode.NOT_FOUND)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (result.error().equals(Result.ErrorCode.CONFLICT)) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        } else if (result.error().equals(Result.ErrorCode.FORBIDDEN)) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        } else {
            throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
        }
    }
    
}
