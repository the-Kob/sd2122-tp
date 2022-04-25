package tp1.clients;

import tp1.api.service.util.Directory;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

import java.net.MalformedURLException;
import java.net.URI;

public class ClientFactory {
    public static Result<Users> getUsersClient(URI serverURI) {
        try {
            var s = serverURI.toString();

            if (s.endsWith("rest")) {
                return Result.ok(new RestUsersClient(serverURI));
            } else {
                return Result.ok(new SoapUsersClient(serverURI));
            }
        } catch (MalformedURLException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    public static Result<Directory> getDirectoryClient(URI serverURI) {
        try {
            var s = serverURI.toString();

            if (s.endsWith("rest")) {
                return Result.ok(new RestDirectoryClient(serverURI));
            } else {
                return Result.ok(new SoapDirectoryClient(serverURI));
            }
        } catch (MalformedURLException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }

    public static Result<Files> getFilesClient(URI serverURI) {
        try {
            var s = serverURI.toString();
            if (s.endsWith("rest")) {
                return Result.ok(new RestFilesClient(serverURI));
            } else {
                return Result.ok(new SoapFilesClient(serverURI));
            }
        } catch (MalformedURLException e) {
            return Result.error(Result.ErrorCode.valueOf(e.getMessage()));
        }
    }
}
