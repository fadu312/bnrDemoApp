package com.bnrdemoapp.com.controller;

import com.bnrdemoapp.com.domain.WebSocketChatMessage;
import com.bnrdemoapp.com.entities.Rate;
import com.bnrdemoapp.com.services.ConversionService;
import com.bnrdemoapp.com.services.RatesService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

  @Autowired
	RatesService ratesService;

	@Autowired
	ConversionService conversionService;

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/chatbnr")
	public WebSocketChatMessage sendMessage(@Payload WebSocketChatMessage webSocketChatMessage) {
		String currency1 ="RON";
		String currency2 = "RON";
		if (webSocketChatMessage.getContent().length() == 7) {
			for (Rate rate : ratesService.getXMLData().getBody().getCube().getRates()) {
				if ((webSocketChatMessage.getContent().toUpperCase().contains(rate.getCurrency()) || webSocketChatMessage.getContent().toUpperCase().contains("RON"))  && webSocketChatMessage.getContent()
						.toUpperCase().contains("/")) {
					if (webSocketChatMessage.getContent().toUpperCase().substring(0,3).equals(rate.getCurrency())){
						currency1 = rate.getCurrency();
					}
					else if (webSocketChatMessage.getContent().toUpperCase().substring(0,3).equals("RON")){
						currency1 = "RON";
					}
					if (webSocketChatMessage.getContent().toUpperCase().substring(4,7).equals(rate.getCurrency())){
						currency2 = rate.getCurrency();
					}
				}
			}
			webSocketChatMessage.setContent(conversionService.convertCurrencies(currency1,currency2));
		}
		else if (webSocketChatMessage.getContent().length() == 3){
			for (Rate rate : ratesService.getXMLData().getBody().getCube().getRates()){
			if  (webSocketChatMessage.getContent().toUpperCase().contains(rate.getCurrency()) ||webSocketChatMessage.getContent().toUpperCase().equals("RON") ){
					currency1 = rate.getCurrency();
					webSocketChatMessage.setContent(conversionService.convertCurrencies(currency1,currency2));
				}
			}
		}
		else {
			webSocketChatMessage.setContent("Wrong format for currencies exchange or currency not available ");
		}
		return webSocketChatMessage;
	}

	@MessageMapping("/chat.newUser")
	@SendTo("/topic/chatbnr")
	public WebSocketChatMessage addUser(@Payload WebSocketChatMessage webSocketChatMessage,
			SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", webSocketChatMessage.getSender());
		return webSocketChatMessage;
	}

}
