package com.bnrdemoapp.com.services;

import com.bnrdemoapp.com.MyUserDetailsService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthorizationService {

  private static final String AUTHORIZATION = "Authorization";

  @Autowired
  MyUserDetailsService myUserDetailsService;

  public String getBearerToken() {

    String returnToken = null;


    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

    param.add("grant_type", "client_credentials");

    RestTemplate rt = new RestTemplate();

    HttpMessageConverter<?> formHttpMessageConverter = new FormHttpMessageConverter();
    HttpMessageConverter<?> stringHttpMessageConverternew = new StringHttpMessageConverter();

    List<HttpMessageConverter<?>> postFormats = new ArrayList<>();
    postFormats.add(formHttpMessageConverter);
    postFormats.add(stringHttpMessageConverternew);

    rt.setMessageConverters(postFormats);

    try {
      HttpEntity<?> httpEntity = new HttpEntity<Object>(param,
          createHeadersWithAuthorizationSecret(myUserDetailsService.getUsername(), myUserDetailsService.getPassword()));

      ResponseEntity<String> out = rt.exchange("localhost:8080/authenticate", HttpMethod.POST, httpEntity, String.class);

      JSONObject jsonObj = new JSONObject(out.getBody());

      if ("Bearer".equalsIgnoreCase(jsonObj.getString("token_type"))) {
        if (jsonObj.getInt("expires_in") > 0) {
          returnToken = "Bearer " + jsonObj.getString("access_token");
        }
      }
    } catch (HttpStatusCodeException e) {
      e.printStackTrace();
    }

    return returnToken;
  }

  public HttpHeaders createHeadersWithAuthorizationSecret(String username, String password) {
    return new HttpHeaders() {
      /**
       *
       */
      private static final long serialVersionUID = 1L;

      {
        String auth = username + ":" + password;

        Base64.Encoder encoder = Base64.getEncoder();

        byte[] encodedAuth = encoder.encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Bearer " + new String(encodedAuth);
        set(AuthorizationService.AUTHORIZATION, authHeader);
      }
    };
  }

  public HttpHeaders createHttpHeaders() {

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(AUTHORIZATION, getBearerToken());
    return headers;
  }

}
