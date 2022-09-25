package com.bnrdemoapp.com.conversion;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDate;

public class ConversionResponse {

  @JsonSerialize(using = ToStringSerializer.class)
  LocalDate dateOfConversion;
  Double initialCurrency;
  String initialCurrencyType;
  Double transformedCurrency;
  String transformedCurrencyType;
  Double roundedConversion;

  public ConversionResponse(LocalDate dateOfConversion, Double initialCurrency, String initialCurrencyType,
      Double transformedCurrency, String transformedCurrencyType) {
    this.dateOfConversion = dateOfConversion;
    this.initialCurrency = initialCurrency;
    this.initialCurrencyType = initialCurrencyType;
    this.transformedCurrency = transformedCurrency;
    this.transformedCurrencyType = transformedCurrencyType;
    this.roundedConversion = Math.round(this.transformedCurrency * 100.0) / 100.0;
  }

  public LocalDate getDateOfConversion() {
    return dateOfConversion;
  }

  public void setDateOfConversion(LocalDate dateOfConversion) {
    this.dateOfConversion = dateOfConversion;
  }

  public Double getInitialCurrency() {
    return initialCurrency;
  }

  public void setInitialCurrency(Double initialCurrency) {
    this.initialCurrency = initialCurrency;
  }

  public Double getTransformedCurrency() {
    return transformedCurrency;
  }

  public void setTransformedCurrency(Double transformedCurrency) {
    this.transformedCurrency = transformedCurrency;
  }

  public ConversionResponse() {
  }

  public String getInitialCurrencyType() {
    return initialCurrencyType;
  }

  public void setInitialCurrencyType(String initialCurrencyType) {
    this.initialCurrencyType = initialCurrencyType;
  }

  public String getTransformedCurrencyType() {
    return transformedCurrencyType;
  }

  public void setTransformedCurrencyType(String transformedCurrencyType) {
    this.transformedCurrencyType = transformedCurrencyType;
  }

  public Double getRoundedConversion() {
    return roundedConversion;
  }

  public void setRoundedConversion(Double roundedConversion) {
    this.roundedConversion = roundedConversion;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("ConversionResponse{");
    sb.append("dateOfConversion=").append(dateOfConversion);
    sb.append(", initialCurrency=").append(initialCurrency);
    sb.append(", initialCurrencyType='").append(initialCurrencyType).append('\'');
    sb.append(", transformedCurrency=").append(transformedCurrency);
    sb.append(", transformedCurrencyType='").append(transformedCurrencyType).append('\'');
    sb.append(", roundedConversion=").append(roundedConversion);
    sb.append('}');
    return sb.toString();
  }
}
