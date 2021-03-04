package com.zte.msg.alarmcenter.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zte.msg.alarmcenter.dto.DataResponse;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Api(tags = "上传文件")
@Validated
public class FastDFSController {

    @Resource
    private FastFileStorageClient storageClient;

    @PostMapping("/upLoad")
    public DataResponse<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String extensionName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        // 上传文件
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extensionName, null);
        return DataResponse.of(storePath.getFullPath());
    }
}
