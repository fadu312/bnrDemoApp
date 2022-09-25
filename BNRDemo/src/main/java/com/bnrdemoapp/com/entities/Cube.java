package com.bnrdemoapp.com.entities;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Cube  implements Serializable {

  @XmlAttribute(name="date")
  private String date;

  @XmlElement(name="Rate")
  private List<Rate> rates;

  public Cube(String date, List<Rate> rates) {
    this.date = date;
    this.rates = rates;
  }

  public Cube() {
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }


  public List<Rate> getRates() {
    return rates;
  }

  public void setRates(List<Rate> rates) {
    this.rates = rates;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Cube{");
    sb.append("date='").append(date).append('\'');
    sb.append(", rates=").append(rates);
    sb.append('}');
    return sb.toString();
  }
}
