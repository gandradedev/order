package br.com.order.domain.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains(".")) {
            int lastIndexOfDot = fileName.lastIndexOf('.');
            if (lastIndexOfDot > 0 && lastIndexOfDot < fileName.length() - 1) {
                return fileName.substring(lastIndexOfDot);
            }
        }
        return "";
    }

}
