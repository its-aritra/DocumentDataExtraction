package com.example.restApi.Service;


import com.example.restApi.dto.FileUploadResponse;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentReaderService {

    FileUploadResponse fileUploadResponse;
    BodyContentHandler handler = new BodyContentHandler(-1);
    Metadata metaData = new Metadata();
    ParseContext parseContext = new ParseContext();
    FileInputStream inputStream;
    PDFParser pdfParser = new PDFParser();

    {
        try {
            if (fileUploadResponse!=null) {
                inputStream = new FileInputStream("src/main/resources/" + fileUploadResponse.getFileName());
                pdfParser.parse(inputStream, handler, metaData, parseContext);
            }
            else {
                System.out.println("File Not Available!!");
            }
        } catch (IOException | SAXException | TikaException ex) {
            ex.printStackTrace();
        }
    }


    public void DocumentReaderService() {
    }


    public void importPDF() {
        try {
            pdfParser.parse(inputStream, handler, metaData, parseContext);
        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }
    }

    public String getDocumentText() {
        return handler.toString();
    }

    public Map<String, String> getMetaData() {
        String[] metadatanames = metaData.names();
        Map<String, String> metamap = new HashMap<>();
        for (String name : metadatanames) {
            metamap.put(name, metaData.get(name));
        }
        return metamap;
    }

//    public List<PDFSignatureInfo> getSignatureInfo(PDFFile file) {
//        byte[] bytes = Base64Utils.decodeFromString(file.getBase64());
//        InputStream stream = new ByteArrayInputStream(bytes);
//        List<PDFSignatureInfo> lpsi = new ArrayList<PDFSignatureInfo>();
//        try (PDDocument document = PDDocument.load(stream)) {
//            for (PDSignature sig : document.getSignatureDictionaries()) {
//                PDFSignatureInfo psi = new PDFSignatureInfo();
//                lpsi.add(psi);
//
//                COSDictionary sigDict = sig.getCOSObject();
//
//                psi.reason = sig.getReason();
//                psi.name = sig.getName();
//                psi.signDate = sig.getSignDate().getTime();
//                psi.subFilter = sig.getSubFilter();
//                psi.contactInfo = sig.getContactInfo();
//                psi.filter = sig.getFilter();
//                psi.location = sig.getLocation();
//
//                int[] byteRange = sig.getByteRange();
//                if (byteRange.length != 4) {
//                    throw new IOException("Signature byteRange must have 4 items");
//                } else {
//                    long fileLen = bytes.length;
//                    long rangeMax = byteRange[2] + (long) byteRange[3];
//                    int contentLen = sigDict.getString(COSName.CONTENTS).length() * 2 + 2;
//                    if (fileLen != rangeMax || byteRange[0] != 0 || byteRange[1] + contentLen != byteRange[2]) {
//                        psi.coversWholeDocument = false;
//                    } else {
//                        psi.coversWholeDocument = true;
//                    }
//                }
//
//            }
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
//        return lpsi;

}
