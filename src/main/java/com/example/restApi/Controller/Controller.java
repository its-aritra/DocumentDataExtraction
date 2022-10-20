package com.example.restApi.Controller;

import com.example.restApi.Service.DataExtractionService;
import com.example.restApi.Service.FileStorageService;
import com.example.restApi.dto.FileUploadResponse;
import org.apache.tika.exception.TikaException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xml.sax.SAXException;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class Controller {

    private FileStorageService fileStorageService;
    private DataExtractionService dataExtractionService;

    public Controller(FileStorageService fileStorageService) {
        super();
        this.fileStorageService = fileStorageService;
        this.dataExtractionService = dataExtractionService;
    }

    @PostMapping("/upload")
    FileUploadResponse fileUpload(@RequestParam("file") MultipartFile file) throws TikaException, IOException, SAXException {

        String fileName = fileStorageService.storeFile(file);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/pdf/download/")
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

    @GetMapping("/text")
    public String getExtractedText(String file) throws NullPointerException {
        return dataExtractionService.getExtractedText(file);
    }

    @GetMapping("/metadata")
    public String getMetadata(String file)  throws  NullPointerException{
        return dataExtractionService.getExtractedText(file);
    }

    @GetMapping("/sigInfo")
    public String getSignatureInfo(String file) throws NullPointerException{
        return dataExtractionService.getExtractedText(file);
    }


}
