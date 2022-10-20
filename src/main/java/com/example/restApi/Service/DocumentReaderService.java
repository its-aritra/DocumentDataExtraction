package com.example.restApi.Service;


import com.example.restApi.Model.PDFSignatureInfo;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentReaderService implements DataExtractionService {
    @Override
    public String getExtractedText(String file) throws NullPointerException {

//        System.out.println("getExtractedText executed...");

        AutoDetectParser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        byte[] bytes = Base64Utils.decodeFromString(file);
        try {
            InputStream stream = new ByteArrayInputStream(bytes);
            parser.parse(stream, handler, metadata, parseContext);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (TikaException e) {
            System.err.println(e.getMessage());
        } catch (SAXException e) {
            System.err.println(e.getMessage());
        }

        return handler.toString();
    }

    @Override
    public Map<String, String> getMetadata(String file) {

//        System.out.println("getMetadata executed...");


        AutoDetectParser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        byte[] bytes = Base64Utils.decodeFromString(file);
        try {
            InputStream stream = new ByteArrayInputStream(bytes);
            parser.parse(stream, handler, metadata, parseContext);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (TikaException e) {
            System.err.println(e.getMessage());
        } catch (SAXException e) {
            System.err.println(e.getMessage());
        }

        Map<String, String> pdfMetadata = new HashMap<>();
        String[] metadataNames = metadata.names();
        for (String name : metadataNames) {
            pdfMetadata.put(name, metadata.get(name));
        }
        return pdfMetadata;
    }

    @Override
    public List<PDFSignatureInfo> getSignatureInfo(String file) {

//        System.out.println("getSignatureInfo executed...");

        byte[] bytes = Base64Utils.decodeFromString(file);
        InputStream stream = new ByteArrayInputStream(bytes);
        List<PDFSignatureInfo> lpsi = new ArrayList<PDFSignatureInfo>();
        try (PDDocument document = PDDocument.load(stream)) {
            for (PDSignature sig : document.getSignatureDictionaries()) {
                PDFSignatureInfo psi = new PDFSignatureInfo();
                lpsi.add(psi);

                COSDictionary sigDict = sig.getCOSObject();

                psi.reason = sig.getReason();
                psi.name = sig.getName();
                psi.signDate = sig.getSignDate().getTime();
                psi.subFilter = sig.getSubFilter();
                psi.contactInfo = sig.getContactInfo();
                psi.filter = sig.getFilter();
                psi.location = sig.getLocation();

                int[] byteRange = sig.getByteRange();
                if (byteRange.length != 4) {
                    throw new IOException("Signature byteRange must have 4 items");
                } else {
                    long fileLen = bytes.length;
                    long rangeMax = byteRange[2] + (long) byteRange[3];
                    int contentLen = sigDict.getString(COSName.CONTENTS).length() * 2 + 2;
                    if (fileLen != rangeMax || byteRange[0] != 0 || byteRange[1] + contentLen != byteRange[2]) {
                        psi.coversWholeDocument = false;
                    } else {
                        psi.coversWholeDocument = true;
                    }
                }

            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return lpsi;
    }
}
