package com.assignment.spring.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.assignment.spring.entities.WeatherEntity;
import com.assignment.spring.models.CityWeatherModel;
import com.assignment.spring.models.MainData;
import com.assignment.spring.models.SysData;
import com.assignment.spring.repositories.WeatherRepository;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {WeatherService.class, WeatherRepository.class})
@TestPropertySource("classpath:application-test.properties")
public class WeatherServiceTest extends TestCase {

  public static final String AMSTERDAM = "Amsterdam";
  public static final String NL = "NL";
  public static final double TEMP = 42D;
  @Autowired
  WeatherService weatherService;

  @MockBean
  WeatherRepository weatherRepository;

  @MockBean
  RestTemplate weatherRestTemplate;

  @Captor
  ArgumentCaptor<String> cityCaptor;

  @Captor
  ArgumentCaptor<String> urlCaptor;

  @Captor
  ArgumentCaptor<String> tokenCaptor;

  @Test
  public void testFindFromDatabaseOrCache() {

    WeatherEntity weatherAms = WeatherEntity.builder()
        .id(1L)
        .temperature(TEMP)
        .country(NL)
        .city(AMSTERDAM).build();
    when(weatherRepository.findByCity(eq(AMSTERDAM))).thenReturn(weatherAms);

    WeatherEntity city = weatherService.getCity(AMSTERDAM);

    verify(weatherRepository, times(1)).findByCity(cityCaptor.capture());
    assertEquals(AMSTERDAM, cityCaptor.getValue());
    assertEquals(AMSTERDAM, city.getCity());
    assertEquals(NL, city.getCountry());
    assertEquals(TEMP, city.getTemperature());
  }

  @Test
  public void testFindFromRestApi() {

    CityWeatherModel weatherAms = CityWeatherModel.builder()
        .sys(SysData.builder().country(NL).build())
        .main(MainData.builder().temp(TEMP).build()).build();
    when(weatherRestTemplate
        .getForObject(any(String.class), any(),
            any(String.class), any(String.class))).thenReturn(weatherAms);

    WeatherEntity city = weatherService.getCity(AMSTERDAM);

    verify(weatherRestTemplate, times(1))
        .getForObject(urlCaptor.capture(), any(), cityCaptor.capture(), tokenCaptor.capture());
    assertEquals("WEATHER-URL?q={city}&APPID={weatherToken}", urlCaptor.getValue());
    assertEquals(AMSTERDAM, cityCaptor.getValue());
    assertEquals("TOKEN", tokenCaptor.getValue());
    assertEquals(AMSTERDAM, city.getCity());
    assertEquals(NL, city.getCountry());
    assertEquals(TEMP, city.getTemperature());
  }

  @Test(expected = HttpClientErrorException.class)
  public void testFindFromRestApiWithException() {
    HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND,
        "City Not Found!");
    when(weatherRestTemplate
        .getForObject(any(String.class), any(),
            any(String.class), any(String.class))).thenThrow(exception);

    weatherService.getCity("Amstedam");
  }
}