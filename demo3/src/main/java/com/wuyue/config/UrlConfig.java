package com.wuyue.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "logconfig")
@Configuration
@Getter
@Setter
public class UrlConfig {


	private String urlStaticPic;

	private String accesslog;

	private String cxzjOneBarCodeUrl;


}
