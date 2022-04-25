package tp1.clients;

import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

public class SoapFilesClient extends SoapClient implements Files {
    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        return null;
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        return null;
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        return null;
    }
}
