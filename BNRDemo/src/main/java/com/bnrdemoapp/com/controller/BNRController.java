package com.bnrdemoapp.com.controller;

import com.bnrdemoapp.com.MyUserDetailsService;
import com.bnrdemoapp.com.filters.JwtRequestFilter;
import com.bnrdemoapp.com.models.AuthenticationRequest;
import com.bnrdemoapp.com.models.AuthenticationResponse;
import com.bnrdemoapp.com.services.AuthorizationService;
import com.bnrdemoapp.com.services.ConversionService;
import com.bnrdemoapp.com.util.JwtUtil;
import com.squareup.okhttp.OkHttpClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.io.IOException;
import javax.xml.ws.Response;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BNRController {

  @Autowired
  ConversionService conversionService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtTokenUtil;

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private AuthorizationService authorizationService;

  @GetMapping("/api/test")
  public String getBankName(){
    return  "TEST";
  }

  //to test from postman with security please remove configure /api/** and then generate the token via post and use it in get as bearer auth
  @GetMapping({"api/{currency1}" })
  @ApiOperation(notes = "", value = "Return Conversion for Currencies")
  @ApiResponses({@ApiResponse(code = 200,
      message = "Conversion Returned."), @ApiResponse(code = 400,
      message = "Invalid request."), @ApiResponse(code = 500,
      message = "Internal server error.")})
  public String getRates(@PathVariable("currency1") String currency1,@RequestParam(value = "transform",defaultValue = "RON")  String currency2){

    if (currency2 == null){
      currency2 = "RON";
    }

    return conversionService.convertCurrencies(currency1,currency2);
  }



  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public String createAuthenticationToken() throws Exception {

    //details taken from app properties file
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(userDetailsService.getUsername(), userDetailsService.getPassword())
      );
    }
    catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password", e);
    }


    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(userDetailsService.getUsername());

    final String jwt = jwtTokenUtil.generateToken(userDetails);


    return "Bearer "+jwt;
  }

}

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserDetailsService myUserDetailsService;
  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(myUserDetailsService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  //Here added api because FE should store the token for user after authenticate and BE only validates that is a Bearer token
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable()
        .authorizeRequests().antMatchers("/**","/authenticate","/api/**").permitAll().
        anyRequest().authenticated().and().
        exceptionHandling().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

  }
}

