package com.example.restApi;


import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.restApi.Main.path;

public class DocumentReader {

    BodyContentHandler handler = new BodyContentHandler(-1);
    Metadata metaData = new Metadata();
    ParseContext parseContext = new ParseContext();
    FileInputStream inputStream;
    PDFParser pdfParser = new PDFParser();

    {
        try {
            inputStream = new FileInputStream(path());
            pdfParser.parse(inputStream,handler,metaData,parseContext);
        } catch (IOException | SAXException | TikaException ex) {
            ex.printStackTrace();
        }
    }




    public void DocumentReader(){
    }



    public void importPDF(){
        try {
            pdfParser.parse(inputStream,handler,metaData,parseContext);
        }
        catch (IOException | SAXException | TikaException e){
            e.printStackTrace();
        }
    }

    public String getDocumentText(){
        return handler.toString();
    }

    public Map<String,String> getMetaData(){
        String[] metadatanames = metaData.names();
        Map<String,String> metamap = new HashMap<>();
        for(String name: metadatanames){
            metamap.put(name,metaData.get(name));
        }
        return metamap;
    }
}
