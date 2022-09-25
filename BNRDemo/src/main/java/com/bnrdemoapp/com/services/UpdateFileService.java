package com.bnrdemoapp.com.services;

import java.io.File;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UpdateFileService {

  @Scheduled(cron = " 0 0 14 ? * MON-SUN ", zone = "Europe/Athens")
  public void deleteFile(){
    new File("BNRRATES.txt").delete();
  }
}
