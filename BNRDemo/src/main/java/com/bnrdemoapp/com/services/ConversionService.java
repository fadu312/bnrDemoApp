package com.bnrdemoapp.com.services;

import com.bnrdemoapp.com.entities.Cube;
import com.bnrdemoapp.com.entities.Rate;
import com.bnrdemoapp.com.conversion.ConversionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversionService {

  @Autowired
  public RatesService ratesService;

  public String convertCurrencies(String currency1, String currency2){
    Cube cube = ratesService.getXMLData().getBody().getCube();
    List<Rate> rates = cube.getRates();
    Rate initialRate = new Rate();
    Rate desiredRate = new Rate();

    if (currency2.equals("RON") || currency2 == null ){
      initialRate.setMultiplier(null);
      initialRate.setCurrency("RON");
      initialRate.setValue(1.0);
    }
    if (currency1.equals("RON")){
      desiredRate.setMultiplier(null);
      desiredRate.setCurrency("RON");
      desiredRate.setValue(1.0);
    }
    if ((rates.stream().anyMatch(match -> match.getCurrency().contains(currency1))||currency1.equals("RON"))
      && (rates.stream().anyMatch(match -> match.getCurrency().contains(currency2))||currency2.equals("RON"))){
      for(Rate rate : rates){
        if(rate.getCurrency().equals(currency2)){
          initialRate.setValue(rate.getValue());
          initialRate.setCurrency(currency2);
          initialRate.setMultiplier(rate.getMultiplier());
        }
        else if (rate.getCurrency().equals(currency1)){
          desiredRate.setValue(rate.getValue());
          desiredRate.setCurrency(currency1);
          desiredRate.setMultiplier(rate.getMultiplier());
        }
      }
      return convertToJson(convert(cube,desiredRate,initialRate));
    }
    return "Can't convert currencies";
  }

  public ConversionResponse convert(Cube cube, Rate rate1, Rate rate2){
    ConversionResponse conversionResponse = new ConversionResponse();
    if (rate2.getMultiplier() == null) {
      rate2.setMultiplier(1.0);
    }
    if (rate1.getMultiplier() == null) {
      rate1.setMultiplier(1.0);
    }
    if (rate2.getCurrency().equals("RON")){
         conversionResponse.setDateOfConversion(convertDate(cube.getDate()));
         conversionResponse.setTransformedCurrency(rate1.getMultiplier()*rate1.getValue());
         conversionResponse.setTransformedCurrencyType(rate1.getCurrency());
         conversionResponse.setInitialCurrencyType(rate2.getCurrency());
         conversionResponse.setInitialCurrency(1.0);
         conversionResponse.setRoundedConversion(roundTransformedCurrency(conversionResponse.getTransformedCurrency()));
      }
    else if (rate1.getCurrency().equals("RON")) {

        conversionResponse.setDateOfConversion(convertDate(cube.getDate()));
        conversionResponse.setTransformedCurrency((rate1.getValue() / rate2.getValue()) / rate2.getMultiplier());
        conversionResponse.setTransformedCurrencyType(rate1.getCurrency());
        conversionResponse.setInitialCurrencyType(rate2.getCurrency());
        conversionResponse.setInitialCurrency(1.0);
        conversionResponse.setRoundedConversion(roundTransformedCurrency(conversionResponse.getTransformedCurrency()));
      }
    else {
      if (rate1.getValue()/rate1.getMultiplier() > rate2.getMultiplier()/rate2.getMultiplier()){
        conversionResponse.setDateOfConversion(convertDate(cube.getDate()));
        conversionResponse.setTransformedCurrency((rate1.getValue() / rate2.getValue()/rate2.getMultiplier()));
        conversionResponse.setTransformedCurrencyType(rate1.getCurrency());
        conversionResponse.setInitialCurrencyType(rate2.getCurrency());
        conversionResponse.setInitialCurrency(1.0);
        conversionResponse.setRoundedConversion(roundTransformedCurrency(conversionResponse.getTransformedCurrency()));
      }
      else{
        conversionResponse.setDateOfConversion(convertDate(cube.getDate()));
        conversionResponse.setTransformedCurrency((rate2.getValue()  / rate1.getValue()/ rate1.getMultiplier()));
        conversionResponse.setTransformedCurrencyType(rate1.getCurrency());
        conversionResponse.setInitialCurrencyType(rate2.getCurrency());
        conversionResponse.setInitialCurrency(1.0);
        conversionResponse.setRoundedConversion(roundTransformedCurrency(conversionResponse.getTransformedCurrency()));

      }
         }

    return conversionResponse;
  }

  public LocalDate convertDate(String dateBeforeConversion) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.ENGLISH);
    return LocalDate.parse(dateBeforeConversion,formatter);
  }

  public String convertToJson(ConversionResponse conversionResponse){
    ObjectMapper Obj = new ObjectMapper();
    try {
      String jsonStr = Obj.writeValueAsString(conversionResponse);
      return jsonStr;
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return "Can't convert TO JSON";
  }

  public Double roundTransformedCurrency(Double transformCurrency){
    return (Math.round(transformCurrency * 1000.0) / 1000.0);
  }
}
