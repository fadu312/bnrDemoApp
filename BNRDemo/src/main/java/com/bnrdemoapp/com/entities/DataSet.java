package com.bnrdemoapp.com.entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="DataSet")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSet implements Serializable {

  @XmlElement(name="Header")
  public Header header;
  @XmlElement(name="Body")
  public Body body;

  public DataSet(Header header, Body body) {
    this.header = header;
    this.body = body;
  }

  public DataSet() {
  }

  public Header getHeader() {
    return header;
  }

  public void setHeader(Header header) {
    this.header = header;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }
}
