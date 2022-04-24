package tp1.server.resources;

import java.util.*;
import java.util.logging.Logger;

import jakarta.inject.Singleton;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;
import tp1.api.service.util.Result.ErrorCode;


@Singleton
public class DirectoryResource implements RestDirectory {

	private Discovery discovery;

    final Directory impl;

    public DirectoryResource(Discovery discovery){
        this.discovery = discovery;
		this.discovery.startListener();
		impl = new JavaDirectory(this.discovery);
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {

        var result = impl.writeFile(filename, data, userId, password);
		
		if(result.isOK())
			return result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
        var result = impl.deleteFile(filename, userId, password);

		if(result.isOK()) {
			result.value();
		} else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        // Check if the file doesn't exist
        var result = impl.shareFile(filename, userId, userIdShare, password);
		
		if(result.isOK())
			result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        // Check if the file doesn't exist
        var result = impl.unshareFile(filename, userId, userIdShare, password);
		
		if(result.isOK())
			result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        var result = impl.getFile(filename, userId, accUserId, password);

		if(result.isOK())
			return result.value();
		else if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        var result = impl.lsFile(userId, password);
        
        if(result.isOK())
            return result.value();
        else if(result.error().equals(ErrorCode.BAD_REQUEST)){
            throw new WebApplicationException( Status.BAD_REQUEST );
        } else if(result.error().equals(ErrorCode.NOT_FOUND)){
            throw new WebApplicationException( Status.NOT_FOUND );
        } else if(result.error().equals(ErrorCode.CONFLICT)){
            throw new WebApplicationException( Status.CONFLICT );
        } else if(result.error().equals(ErrorCode.FORBIDDEN)){
            throw new WebApplicationException( Status.FORBIDDEN );
        } else {
            throw new WebApplicationException( Status.NOT_IMPLEMENTED );
        }
    }

	@Override
	public Result<Void> removeUser(String userId, String password) {
		var result = impl.removeUser(userId, password);

		if(result.error().equals(ErrorCode.BAD_REQUEST)){
			throw new WebApplicationException( Status.BAD_REQUEST );
		} else if(result.error().equals(ErrorCode.NOT_FOUND)){
			throw new WebApplicationException( Status.NOT_FOUND );
		} else if(result.error().equals(ErrorCode.CONFLICT)){
			throw new WebApplicationException( Status.CONFLICT );
		} else if(result.error().equals(ErrorCode.FORBIDDEN)){
			throw new WebApplicationException( Status.FORBIDDEN );
		} else {
			throw new WebApplicationException( Status.NOT_IMPLEMENTED );
		}
	}
}
