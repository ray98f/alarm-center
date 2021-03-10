package com.zte.msg.alarmcenter.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zte.msg.alarmcenter.dto.DataResponse;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("/file")
@Api(tags = "上传文件")
@Validated
public class FastDFSController {

    @Resource
    private FastFileStorageClient storageClient;

    @Value("${imagePath}")
    private String imgPath;

    @PostMapping("/upLoad")
    public DataResponse<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String extensionName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        // 上传文件
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extensionName, null);
        return DataResponse.of(storePath.getFullPath());
    }

    @GetMapping("/downLoadImg")
    public DataResponse<Void> downLoadFile(@RequestParam("imagePath") String imagePath){
        try {
            String[] split = imagePath.split("/");
            String[] strings = imagePath.split(split[0]);
            strings[1] = strings[1].substring(1);
            byte[] bytes = storageClient.downloadFile(split[0], strings[1], new DownloadByteArray());
            FileOutputStream stream = new FileOutputStream(imagePath);
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DataResponse.success();
    }

    @GetMapping("/forwardImg")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("imagePath")String imagePath) throws IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            BufferedImage bi;
            bi = ImageIO.read(new URL(imgPath + imagePath));
            ImageIO.write(bi, "png", bas);
            bytes = bas.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bas.close();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
