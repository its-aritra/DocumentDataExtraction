package com.example.restApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

@SpringBootApplication
public class Main {

    public static String path(){
        return "src/main/resources/sample2.pdf";
    }

    public static void main(String[] args) throws FileNotFoundException {

        DocumentReader docReader = new DocumentReader();

        try{
            docReader.importPDF();
        }
        catch (Exception e){
            System.out.println("Failed: "+ e.getMessage());
        }

        SpringApplication.run(Main.class, args);
    }
}