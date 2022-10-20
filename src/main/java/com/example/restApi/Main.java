package com.example.restApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static String getPath(){
        return "src/main/resources/";
    }

    public static void main(String[] args) {


        SpringApplication.run(Main.class, args);
    }
}