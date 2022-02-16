package com.phoenix.service.cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Qualifier("cloudinary")
class CloudinaryServiceImplTest {

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Cloudinary Object instantiation test")
    void instantiateCloudinaryTest(){
        assertThat(cloudinary).isNotNull();

    }

    @Test
    void uploadToCloudinaryTest() throws IOException {
        Path file = Paths.get("src/test/resources/me4.JPG");
        assertThat(file.toFile().exists()).isTrue();
        Map<?,?> uploadResult = cloudinaryService.upload(Files.readAllBytes(file), ObjectUtils.emptyMap());
        log.info("Upload result to cloud -> {}", uploadResult);
        assertThat(uploadResult.get("url")).isNotNull();
//        Object url = uploadResult.get("url");
//        log.info("Url -> {}", url);
    }

    @Test
    void uploadMultipartFileToCloudinaryTest() throws IOException {
        //load the file
        Path path = Paths.get("src/test/resources/me4.JPG");
        assertThat(path.toFile()).isNotNull();
        assertThat(path.getFileName().toString()).isEqualTo("me4.JPG");
        //load multipart
        MultipartFile multipartFile = new MockMultipartFile(path.getFileName().toString(), path.getFileName().toString(),
                "img/JPG", Files.readAllBytes(path));
        assertThat(multipartFile).isNotNull();
        assertThat(multipartFile.isEmpty()).isFalse();
        //store multipart to file

        //convert multipart to file


        //upload to cloud
        Map<?,?> uploadResult = cloudinaryService.upload(multipartFile.getBytes(), ObjectUtils.asMap(
                "overwrite", true
        ));
        assertThat(uploadResult.get("url")).isNotNull();

//        File file = new File("src/test/resources/me4.JPG");
//        assertThat(file.exists()).isTrue();
//        Map<?,?> uploadResult = cloudinaryService.upload(file, ObjectUtils.emptyMap());
//        log.info("Upload result to cloud -> {}", uploadResult);
//        assertThat(uploadResult.get("url")).isNotNull();
    }
}