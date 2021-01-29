package com.kgh.serverstudy.config;

import lombok.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "resttemplate.config")
public class HttpClientConfig {

    private int maxconntotal;
    private int maxconnperroute;
    private int conntimeout;
    private int readtimeout;

    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(maxconntotal)
                .setMaxConnPerRoute(maxconnperroute)
                .build();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(conntimeout);
        factory.setReadTimeout(readtimeout);
        return new RestTemplate(factory);
    }
}
