package com.javaee.elderlycanteen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.io.IOException;

@SpringBootApplication
public class ElderlyCanteenApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(ElderlyCanteenApplication.class, args);
    }
}
