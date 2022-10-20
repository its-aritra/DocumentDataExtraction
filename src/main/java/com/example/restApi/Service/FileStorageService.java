package com.example.restApi.Service;

import com.example.restApi.Model.PDFToBase64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import static com.example.restApi.Main.getPath;

@Service
public class FileStorageService {

    private Path fileStoragePath;
    private String fileStorageLocation;

    public FileStorageService(@Value("${file.storage.location:temp}") String fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    public String storeFile(MultipartFile file) throws IOException, NullPointerException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Path filePath = Paths.get(fileStoragePath + "\\" + fileName);

        String dirPath = "src/main/resources/" + fileName;

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the File");
        }

        System.out.println(fileName);

        if (fileName!=null) {
            PDFToBase64 pdfb64 = new PDFToBase64();
            DocumentReaderService drs = new DocumentReaderService();
            drs.getExtractedText(pdfb64.getBase64(fileName));
            drs.getMetadata(pdfb64.getBase64(fileName));
            drs.getSignatureInfo(pdfb64.getBase64(fileName));
        }
        else {
            storeFile(file);
        }

//        System.out.println("File Created Successfully...");
//
//        System.out.println("Returning FileName from storeFile...");

        return fileName;
    }

    public Resource downloadFile(String fileName) {

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);

        Resource resource;

        try {
            resource = new UrlResource(path.toUri());

        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the File");
        }

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("The File doesn't exist or not readable");
        }
    }
}
