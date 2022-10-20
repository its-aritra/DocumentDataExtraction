package com.example.restApi.Controller;

import com.example.restApi.Model.PDFSignatureInfo;
import com.example.restApi.Model.PDFToBase64;
import com.example.restApi.Service.DataExtractionService;
import com.example.restApi.Service.FileStorageService;
import com.example.restApi.dto.FileUploadResponse;
import org.apache.tika.exception.TikaException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
public class Controller {

    private FileStorageService fileStorageService;
    private DataExtractionService dataExtractionService;
    private PDFToBase64 pdfToBase64;

    public Controller(DataExtractionService dataExtractionService, FileStorageService fileStorageService, PDFToBase64 pdfToBase64) {
        super();
        this.fileStorageService = fileStorageService;
        this.dataExtractionService = dataExtractionService;
        this.pdfToBase64 = pdfToBase64;
    }

    static String fileN;

    @PostMapping("/upload")
    FileUploadResponse fileUpload(@RequestParam("file") MultipartFile file) throws TikaException, IOException, SAXException {

        String fileName = fileStorageService.storeFile(file);
        fileN = fileName;
//        System.out.println(fileN + "$$" + fileName);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/pdf/download/")
                .path(fileName)
                .toUriString();

        String contentType = file.getContentType();

        FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);

        return response;
    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.downloadFile(fileName);
        MediaType contentType = MediaType.APPLICATION_PDF;
        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename())
                .body(resource);
    }


    //    @GetMapping("/text")
//    public String getExtractedText(String file) throws NullPointerException {
//        return dataExtractionService.getExtractedText(file);
//    }
//
//    @GetMapping("/metadata")
//    public Map<String, String> getMetadata(String file) throws NullPointerException {
//        return dataExtractionService.getMetadata(file);
//    }
//
//    @GetMapping("/sigInfo")
//    public List<PDFSignatureInfo> getSignatureInfo(String file) throws NullPointerException {
//        return dataExtractionService.getSignatureInfo(file);
//    }
    @GetMapping("/text")
    public ResponseEntity<String> getExtractedText() throws IOException {
//        System.out.println(fileN + "inText");
        return new ResponseEntity<String>(dataExtractionService.getExtractedText(pdfToBase64.getBase64(fileN)), HttpStatus.OK);
    }

    @GetMapping("/metadata")
    public ResponseEntity<Map<String, String>> getMetaData() throws IOException {
//        System.out.println(fileN + "inMeta");
        return new ResponseEntity<Map<String, String>>(dataExtractionService.getMetadata(pdfToBase64.getBase64(fileN)), HttpStatus.OK);
    }

    @GetMapping("/sigInfo")
    public ResponseEntity<List<PDFSignatureInfo>> getSignatureInfo() throws IOException {
//        System.out.println(fileN + "inSignature");
        return new ResponseEntity<List<PDFSignatureInfo>>(dataExtractionService.getSignatureInfo(pdfToBase64.getBase64(fileN)), HttpStatus.OK);
    }
}