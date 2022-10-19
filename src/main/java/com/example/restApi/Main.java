package com.example.restApi;

import com.example.restApi.Service.DocumentReaderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        DocumentReaderService docReader = new DocumentReaderService();

        try{
            docReader.importPDF();
        }
        catch (Exception e){
            System.out.println("Failed: "+ e.getMessage());
        }

        SpringApplication.run(Main.class, args);
    }
}