package com.bnrdemoapp.com.entities;


import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Header implements Serializable {

  @XmlElement(name="Publisher")
  private String publisher;

  @XmlElement(name="PublishingDate")
  private String publishingDate;

  @XmlElement(name="MessageType")
  private String messageType;

  public Header() {
  }

  public Header(String publisher, String publishingDate, String messageType) {
    this.publisher = publisher;
    this.publishingDate = publishingDate;
    this.messageType = messageType;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getPublishingDate() {
    return publishingDate;
  }

  public void setPublishingDate(String publishingDate) {
    this.publishingDate = publishingDate;
  }

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }
}
