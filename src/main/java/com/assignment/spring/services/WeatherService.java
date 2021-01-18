package com.assignment.spring.services;

import com.assignment.spring.entities.WeatherEntity;
import com.assignment.spring.models.CityWeatherModel;
import com.assignment.spring.repositories.WeatherRepository;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@Slf4j
public class WeatherService {

  @Autowired
  RestTemplate weatherRestTemplate;

  @Value("${weatherToken}")
  private String weatherToken;
  @Autowired
  WeatherRepository weatherRepository;
  @Value("${weatherAPI}")
  private String weatherAPI;

  @CacheEvict(allEntries = true, cacheNames = {"cities"})
  @Scheduled(fixedDelay = 3000)
  public void cacheEvict() {
    log.info("Evicting Cache");
  }


  public WeatherEntity getCity(String city) {
    log.info("Searching for city: {}", city);
    WeatherEntity cityEntity = weatherRepository.findByCity(city);
    if (Objects.nonNull(cityEntity)) {
      return cityEntity;
    } else {
      CityWeatherModel weatherModel = weatherRestTemplate.getForObject(
          weatherAPI,
          CityWeatherModel.class, city, weatherToken);
      cityEntity = WeatherEntity.builder()
          .city(city)
          .country(weatherModel.getSys().getCountry())
          .temperature(weatherModel.getMain().getTemp()).build();
      weatherRepository.save(cityEntity);
      return cityEntity;
    }
  }
}
