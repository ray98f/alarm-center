package com.zte.msg.alarmcenter.utils;

import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/29 16:41
 */
@SuppressFBWarnings("PATH_TRAVERSAL_IN")
public class FileUtil {

    public static String readStringFromFile(String path) {
        File file = new File(path);
        return readStringFromFile(file);
    }

    public static String readStringFromFile(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String s;
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        deleteFile(file);
        return result.toString();
    }

    public static String readStringFromFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String prefix = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
        File file = File.createTempFile(String.valueOf(UUID.randomUUID()), prefix);
        multipartFile.transferTo(file);
        return readStringFromFile(file);
    }

    private static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                boolean res = file.delete();
                if (!res) {
                    throw new CommonException(ErrorCode.FILE_DELETE_ERROR);
                }
            }
        }
    }
}
