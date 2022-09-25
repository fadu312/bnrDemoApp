package com.bnrdemoapp.com.entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class Rate  implements Serializable {

  @XmlAttribute(name="currency")
  private String currency;

  @XmlAttribute(name="multiplier")
  private Double multiplier;

  @XmlValue
  private Double value;

  public Rate() {
  }

  public Rate(String currency, Double multiplier, Double value) {
    this.currency = currency;
    this.multiplier = multiplier;
    this.value = value;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Double getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(Double multiplier) {
    this.multiplier = multiplier;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Rate{");
    sb.append("currency='").append(currency).append('\'');
    sb.append(", multiplier=").append(multiplier);
    sb.append(", value=").append(value);
    sb.append('}');
    return sb.toString();
  }
}
