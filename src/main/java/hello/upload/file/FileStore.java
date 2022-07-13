package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
// 파일 저장 경로
    @Value("${file.dir}")
    private String fileDir;
// 파일 저장할 루트
    public String getFullPath(String filename) {
        return fileDir + filename;
    }
// multipartFiles 리스트를 받아 for 문 돌면서 storeFileResult ArrayList 에 UploadFile 을 저장.

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                // multipartFiles >> UploadFile
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }
// multipartFile 을 받아서 originalFileName , storeFileName 을 찾아 UploadFile 을 리턴.
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        String storeFileName = createStoreFileName(originalFilename);
// 파일 경로에 저장.
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }
// uuid + fileName
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);

        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }
// 파일 확장자 제거
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


}
