package test;

import java.io.IOException;

import tp1.clients.CreateUserClient;
import tp1.clients.SearchUsersClient;
import tp1.server.RESTDirectoryServer;
import tp1.server.RESTUsersServer;

public class Main {
    public static void main(String arg[]) throws IOException, InterruptedException{

        RESTUsersServer.main(new String[]{});
        RESTDirectoryServer.main(new String[]{});

        Thread.sleep(5000);

        CreateUserClient.main(new String[]{"user1", "fullname", "email", "12345"});
        SearchUsersClient.main(new String[]{""});
    }
}
