package com.phoenix.service.cloud;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    Map<?,?> upload(byte[] bytes, Map<?,?> params) throws IOException;
}
