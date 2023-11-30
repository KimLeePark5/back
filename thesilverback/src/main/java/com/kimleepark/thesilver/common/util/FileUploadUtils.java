package com.kimleepark.thesilver.common.util;

import com.kimleepark.thesilver.common.exception.ServerInternalException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.FAIL_TO_DELETE_FILE;
import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.FAIL_TO_UPLOAD_FILE;

public class FileUploadUtils {

    public static String saveFile(String uploadDir, String fileName, MultipartFile multipartFile){

        try(InputStream inputStream = multipartFile.getInputStream()){

            Path uploadPath = Paths.get(uploadDir);
            //업로드 경로가 존재하지 않을 시 경로 먼저 생성
            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            //파일명 생성
            String replaceFileName = fileName + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            //파일 저장
            Path filePath = uploadPath.resolve(replaceFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            return replaceFileName;

        }catch (IOException e){
            throw new ServerInternalException(FAIL_TO_UPLOAD_FILE);
        }
    }

    public static void deleteFile(String uploadDir, String fileName){
        try {
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(fileName);

            // 파일이 존재하는지 확인
            if (Files.exists(filePath)) {
                // 파일 삭제
                Files.delete(filePath);
            } else {
                // 파일이 존재하지 않으면 로그 또는 예외 처리를 수행할 수 있습니다.
                System.out.println("파일이 존재하지 않습니다: " + filePath);
                // 또는 throw new NotFoundException("파일을 찾을 수 없습니다"); 등으로 예외 처리할 수 있습니다.
            }
        } catch (IOException e) {
            // 예외 발생 시 로그 또는 예외 처리를 수행할 수 있습니다.
            e.printStackTrace();
            throw new ServerInternalException(FAIL_TO_DELETE_FILE);
        }
    }
}
