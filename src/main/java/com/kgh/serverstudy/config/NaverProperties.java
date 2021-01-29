package com.kgh.serverstudy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="naver.openapi")
public class NaverProperties {
    private String movieUrl;
    private String newsUrl;
    private String clientId;
    private String clientSecret;
    private String url;
}
