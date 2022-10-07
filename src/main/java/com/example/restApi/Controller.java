package com.example.restApi;

import org.apache.tika.exception.TikaException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import static com.example.restApi.Main.path;

@RestController
public class Controller {

    DocumentReader docReader = new DocumentReader();

    public Controller() throws FileNotFoundException {
    }

    @GetMapping("/")
    public String index(){
        return "ohayo!!";
    }

    @GetMapping("/text")
    public String text() throws TikaException, IOException, SAXException {
        return docReader.getDocumentText();
    }

    @GetMapping("/meta")
    public Map<String, String> meta() throws TikaException, IOException, SAXException {
        return docReader.getMetaData();
    }
}
