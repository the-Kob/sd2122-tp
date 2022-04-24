package tp1.server.resources;

import java.io.*;

import java.nio.file.Paths;

import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

public class JavaFiles implements Files{

    public JavaFiles() {}

    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        // Check if file is valid
        if (fileId == null || data == null) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        try {
            File file = new File(fileId);
            FileOutputStream fileIdWriter = new FileOutputStream(file);
            fileIdWriter.write(data);
            fileIdWriter.close();

            return Result.ok();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

            return Result.error(Result.ErrorCode.FORBIDDEN);
        }
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        // Check if file is valid
        if(fileId == null) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        File myObj = new File(fileId);

        if (myObj.exists()) {
            myObj.delete();

            return Result.ok();
        } else {
            System.out.println("Object doesn't exist.");

            return Result.error(Result.ErrorCode.NOT_FOUND);
        }
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        // Check if file is valid
        if(fileId == null) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        try {
            File myObj = new File(fileId);

            if (myObj.exists()) {
                return Result.ok(java.nio.file.Files.readAllBytes(Paths.get(fileId)));
            } else {
                System.out.println("Object doesn't exist.");

                return Result.error(Result.ErrorCode.NOT_FOUND);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

            return Result.error(Result.ErrorCode.FORBIDDEN);
        }
    }
}
