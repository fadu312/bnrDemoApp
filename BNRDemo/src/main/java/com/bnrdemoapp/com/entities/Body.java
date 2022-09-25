package com.bnrdemoapp.com.entities;


import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

  @XmlAccessorType(XmlAccessType.FIELD)
  public class Body implements Serializable {

    @XmlElement(name="Subject")
    private String subject;

    @XmlElement(name="OrigCurrency")
    private String origCurrency;

    @XmlElement(name="Cube")
    private Cube cube;


    public Body(String subject, String origCurrency, Cube cube) {
      this.subject = subject;
      this.origCurrency = origCurrency;
      this.cube = cube;
    }

    public Body() {
    }

    public String getSubject() {
      return subject;
    }

    public void setSubject(String subject) {
      this.subject = subject;
    }

    public String getOrigCurrency() {
      return origCurrency;
    }

    public void setOrigCurrency(String origCurrency) {
      this.origCurrency = origCurrency;
    }

    public Cube getCube() {
      return cube;
    }

    public void setCube(Cube cube) {
      this.cube = cube;
    }

  }
