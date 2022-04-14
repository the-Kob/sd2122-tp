package tp1.server.resources;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import jakarta.inject.Singleton;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.server.RESTUsersServer;

@Singleton
public class DirectoryResource implements RestDirectory {

    private static Logger Log = Logger.getLogger(DirectoryResource.class.getName());

    private final Map<String, ArrayList<FileInfo>> ownDirectoriesMap;
    private final Map<String, ArrayList<FileInfo>> sharedDirectoriesMap;

    //ERROS NAO IMPLEMENTADOS
    //COMUNICAÇAO COM OUTROS SERVERS NAO IMPLEMENTADOS

    public DirectoryResource(){
        ownDirectoriesMap = new HashMap<>();
        sharedDirectoriesMap = new HashMap<>();
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {

        FileInfo newFile = new FileInfo(userId, filename, userId + "/" + filename, new HashSet<String>() );

        //Get User, erro se não existir

        if(ownDirectoriesMap.containsKey(userId)){
            ownDirectoriesMap.get(userId).add(newFile);
            //WriteFile
        } else{
            ownDirectoriesMap.put(userId, new ArrayList<FileInfo>());
            ownDirectoriesMap.get(userId).add(newFile);
            //WriteFile
        }

        return null;
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {

        //Get User, erro se não existir

        if(ownDirectoriesMap.containsKey(userId)){
            boolean haveFound = false;
            ArrayList<FileInfo> listOfFiles = ownDirectoriesMap.get(userId);

            for (int i = 0; i < listOfFiles.size(); i++){
                if(listOfFiles.get(i).getFilename().equals(filename)){

                    Iterator<String> iterator = listOfFiles.get(i).getSharedWith().iterator();

                    while(iterator.hasNext()){
                        unshareFileWith(filename, iterator.next());
                    }

                    listOfFiles.remove(i);
                    haveFound = true;
                }
            }

            if(!haveFound){
                //erro ficheiro não existia
            }

            ownDirectoriesMap.replace(userId, listOfFiles);
        } else{

            //erro user não contem files
        }
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
        
        //Get User, erro se não existir
        //Search userIdshare

        if(ownDirectoriesMap.containsKey(userId)){
            boolean haveFound = false;
            ArrayList<FileInfo> listOfFiles = ownDirectoriesMap.get(userId);

            for (int i = 0; i < listOfFiles.size(); i++){

                if(listOfFiles.get(i).getFilename().equals(filename)){
                    Set<String> sharedWith = listOfFiles.get(i).getSharedWith();
                    
                    sharedWith.add(userIdShare);

                    shareFileWith(filename, userId, userIdShare);

                    haveFound = true;
                }
            }

            if(!haveFound){
                //erro ficheiro não existia
            }
            ownDirectoriesMap.replace(userId, listOfFiles);
        } else{
            //erro user não contem files
        }
        
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        //Get User, erro se não existir
        //Search userIdshare

        if(ownDirectoriesMap.containsKey(userId)){
            boolean haveFound = false;
            ArrayList<FileInfo> listOfFiles = ownDirectoriesMap.get(userId);

            for (int i = 0; i < listOfFiles.size(); i++){

                if(listOfFiles.get(i).getFilename().equals(filename)){
                    Set<String> sharedWith = listOfFiles.get(i).getSharedWith();
                    
                    sharedWith.remove(userIdShare);

                    listOfFiles.get(i).setSharedWith(sharedWith);;

                    unshareFileWith(filename, userIdShare);

                    haveFound = true;
                }
            }

            if(!haveFound){
                //erro ficheiro não existia
            }
            ownDirectoriesMap.replace(userId, listOfFiles);
        } else{
            //erro user não contem files
        }
        
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {
        return null;
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {

        if(ownDirectoriesMap.containsKey(userId)){

            List<FileInfo> allFiles = new ArrayList<FileInfo>();

            allFiles.addAll(ownDirectoriesMap.get(userId));
            allFiles.addAll(sharedDirectoriesMap.get(userId));

            return allFiles;

        } else{

            //erro user não contem files
        }
        return null;
    }

    private void shareFileWith(String filename, String ownerUserId, String userId) {

        FileInfo newFile = new FileInfo(userId, filename, ownerUserId + "/" + filename, new HashSet<String>() );

        if(sharedDirectoriesMap.containsKey(userId)){
            sharedDirectoriesMap.get(userId).add(newFile);

        } else{
            sharedDirectoriesMap.put(userId, new ArrayList<FileInfo>());
            sharedDirectoriesMap.get(userId).add(newFile);
        }
    }

    private void unshareFileWith(String filename, String userId) {

        ArrayList<FileInfo> listOfFiles = sharedDirectoriesMap.get(userId);

        for (int i = 0; i < listOfFiles.size(); i++){
            if(listOfFiles.get(i).getFilename().equals(filename)){
                listOfFiles.remove(i);
            }
        }

        sharedDirectoriesMap.replace(userId, listOfFiles);
        
    }
}
