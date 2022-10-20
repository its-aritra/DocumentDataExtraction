package com.example.restApi.Service;

import com.example.restApi.Model.PDFSignatureInfo;

import java.util.List;
import java.util.Map;

public interface DataExtractionService {

    Map<String, String> getMetadata(String file);

    String getExtractedText(String file);

    List<PDFSignatureInfo> getSignatureInfo(String file);

}
