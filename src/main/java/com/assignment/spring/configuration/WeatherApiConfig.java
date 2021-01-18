package com.assignment.spring.configuration;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class WeatherApiConfig {

  @Value("${maxPerRoute}")
  private int maxPerRoute;

  @Value("${maxTotal}")
  private int maxTotal;

  @Value("${connectionRequestTimeout}")
  private int connectionRequestTimeout;

  @Value("${connectTimeout}")
  private int connectTimeout;

  @Value("${socketTimeout}")
  private int socketTimeout;
  

  @Bean(value = "weatherRestTemplate")
  public RestTemplate weatherRestTemplate(HttpClient httpClient) {
    HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    httpRequestFactory.setHttpClient(httpClient);
    return new RestTemplate(httpRequestFactory);
  }

  @Bean
  public PoolingHttpClientConnectionManager weatherPoolingHttpClientConnectionManager() {
    PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
    result.setDefaultMaxPerRoute(maxPerRoute);
    result.setMaxTotal(maxTotal);
    return result;
  }

  @Bean
  public RequestConfig weatherRequestConfig() {
    return RequestConfig.custom()
        .setConnectionRequestTimeout(connectionRequestTimeout)
        .setConnectTimeout(connectTimeout)
        .setSocketTimeout(socketTimeout)
        .build();
  }

  @Bean
  public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
    return HttpClientBuilder
        .create()
        .setConnectionManager(poolingHttpClientConnectionManager)
        .setDefaultRequestConfig(requestConfig)
        .build();
  }
}