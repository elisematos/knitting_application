package com.application.knitting;

import com.application.knitting.service.PhotoStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class KnittingApplication implements CommandLineRunner {
    @Resource
    PhotoStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(KnittingApplication.class, args);
    }

    @Override
    public void run(String... arg) {
        storageService.deleteAll();
        storageService.init();
    }

}
