package com.bnrdemoapp.com.services;


import com.bnrdemoapp.com.entities.DataSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;



@Service
@CacheConfig(cacheNames = {"rate"})
public class RatesService {

  public static String xml;

  public DataSet getXMLData(){


    if (!fileExists()) {
       xml = new BufferedReader(new InputStreamReader(getXML()))
          .lines().parallel().collect(Collectors.joining("\n"))
          .replaceFirst("<DataSet.*", "<DataSet>");
      saveXMLFile(xml);
    }
    else {
      xml=readFile();
    }

    JAXBContext jc = null;
    try {
      jc = JAXBContext.newInstance(DataSet.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      System.out.println(xml);
      DataSet dataSet = (DataSet) unmarshaller.unmarshal(new StringReader(xml));
      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(dataSet, System.out);
      return dataSet;
    } catch (JAXBException e) {
      e.printStackTrace();
    }

    return new DataSet();
  }

  @Cacheable(value="BNRRates")
  public InputStream getXML() {
    String uri = "https://www.bnro.ro/nbrfxrates.xml";
    URL url;
    HttpURLConnection connection;

    {
      try {
        url = new URL(uri);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");
        return connection.getInputStream();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return  null;
  }

  public void saveXMLFile(String xml) {
    FileWriter myWriter = null;
    try {
      myWriter = new FileWriter("BNRRATES.txt");
      myWriter.write(xml);
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean fileExists(){
    try {
      File myObj = new File("BNRRATES.txt");
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        return true;
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
      return  false;
    }
    return false;
  }

  public String readFile(){
    List<String> lines = new ArrayList<>();
    try {
      File myObj = new File("BNRRATES.txt");
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
         lines.add(myReader.nextLine());
      }
      myReader.close();
      return String.join("", lines);
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return "NO FILE";
  }

}
