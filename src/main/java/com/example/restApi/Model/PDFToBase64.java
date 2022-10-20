package com.example.restApi.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static com.example.restApi.Main.getPath;

public class PDFToBase64 {

    private String base64;

    public PDFToBase64() {
    }

    public PDFToBase64(String base64) {
        super();
        this.base64 = base64;
    }

    public String getBase64(String fileName) throws IOException {
        byte[] input_file = Files.readAllBytes(Paths.get(getPath() + fileName));
        byte[] encodedBytes = Base64.getEncoder().encode(input_file);
        String encodedString =  new String(encodedBytes);
        return encodedString;
    }

    public void setBase64(String base64) {

        this.base64 = base64;
    }

}
