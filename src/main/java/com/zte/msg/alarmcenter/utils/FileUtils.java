package com.zte.msg.alarmcenter.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    /**
     * MultipartFile转换成File
     * @param multipartFile
     * @return
     */
    public static File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], filename[filename.length - 1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
