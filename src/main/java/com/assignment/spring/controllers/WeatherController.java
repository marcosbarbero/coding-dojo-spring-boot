package com.assignment.spring.controllers;

import com.assignment.spring.entities.WeatherEntity;
import com.assignment.spring.services.WeatherService;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping(value = "/{city}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public WeatherEntity weather(@NotNull @PathVariable String city) {
        return weatherService.getCity(city);
    }

}
