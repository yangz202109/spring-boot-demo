package com.xkcoding.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssUploadService {

 String uploadFile(MultipartFile file,String fileName);
}
