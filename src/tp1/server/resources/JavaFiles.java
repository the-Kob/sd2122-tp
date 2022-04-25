package tp1.server.resources;

import java.io.*;

import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

public class JavaFiles implements Files{

    private static ReentrantLock lock = new ReentrantLock();

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

        //lock.lock();

        File myObj = new File(fileId);

        if (myObj.exists()) {
            myObj.delete();

            //lock.unlock();
        } else {
            //lock.unlock();

            System.out.println("Object doesn't exist.");

            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        return Result.ok();
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
