package com.example.restApi.Controller;

import com.example.restApi.Service.DocumentReaderService;
import org.apache.tika.exception.TikaException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@RestController
public class PDFContentController {

    DocumentReaderService docReader = new DocumentReaderService();

    public PDFContentController() throws FileNotFoundException {
    }


    @GetMapping("/text")
    public String text() throws TikaException, IOException, SAXException {
        return docReader.getDocumentText();
    }

    @GetMapping("/metadata")
    public Map<String, String> meta() throws TikaException, IOException, SAXException {
        return docReader.getMetaData();
    }

}
