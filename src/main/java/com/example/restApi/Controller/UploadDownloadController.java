package com.example.restApi.Controller;

import com.example.restApi.Service.FileStorageService;
import com.example.restApi.dto.FileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UploadDownloadController {

    private FileStorageService fileStorageService;


    public UploadDownloadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    FileUploadResponse fileUpload(@RequestParam("file") MultipartFile file) {

        String fileName = fileStorageService.storeFile(file);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();

        String contentType = file.getContentType();

        FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);

        return response;
    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource>downloadFile(@PathVariable String fileName){
        Resource resource = fileStorageService.downloadFile(fileName);
        MediaType contentType = MediaType.APPLICATION_PDF;
        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .body(resource);
    }
}
