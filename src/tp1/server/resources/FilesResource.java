package tp1.server.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import tp1.api.service.rest.RestFiles;

public class FilesResource implements RestFiles {

    

    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        try {
            File myObj = new File(fileId);
            myObj.createNewFile();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

        try {
            FileWriter myWriter = new FileWriter(fileId);
            myWriter.write(new String(data, StandardCharsets.UTF_8));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    @Override
    public void deleteFile(String fileId, String token) {
        File myObj = new File(fileId);
        myObj.delete();
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        String readFile = "";
        
        try {
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              readFile.concat(data);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

          return readFile.getBytes();
    }
    
}
