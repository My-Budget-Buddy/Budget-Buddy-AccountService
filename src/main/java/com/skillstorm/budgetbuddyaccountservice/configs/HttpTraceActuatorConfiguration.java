package com.skillstorm.budgetbuddyaccountservice.configs;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpTraceActuatorConfiguration {
    
    //stores the trace data in memory and allows you to access that data from /httpexchanges endpoint
    @Bean
    public HttpExchangeRepository httpTraceRepository(){
        return new InMemoryHttpExchangeRepository();
    }
}
