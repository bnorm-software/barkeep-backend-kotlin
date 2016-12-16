package com.bnorm.barkeep;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.bnorm.barkeep.service.BarkeepService;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Component
  public static class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
      registerEndpoints();
    }

    private void registerEndpoints() {
      register(BarkeepService.class);
    }
  }

  @Bean
  public Jackson2ObjectMapperBuilder jacksonBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    builder.indentOutput(true);
    return builder;
  }
}
