package com.bnrdemoapp.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCaching
@RestController
public class BNRDemo {

  public static void main(String[] args) {
    SpringApplication.run(BNRDemo.class, args);
  }
}