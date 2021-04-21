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
import java.io.*;
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
    public ResponseEntity<byte[]> downLoadFile(@RequestParam("imagePath") String imagePath) throws IOException {
        String group = imagePath.substring(0, imagePath.indexOf("/"));
        String path = imagePath.substring(imagePath.indexOf("/") + 1);
        byte[] bytes = storageClient.downloadFile(group, path, new DownloadByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
