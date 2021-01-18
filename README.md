Spring Boot Coding Dojo
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. The current implementation has quite a few problems making it a non-production ready product.

### The task

As the new engineer leading this project, your first task is to make it production-grade, feel free to refactor any piece
necessary to achieve the goal.

###Security Configuration

Initialy you will be requested to login in order to use the endpoint
```properties
user=user1
password=user1Pass
```

###Local Configuration

You will need to setup the following environment variables before you can run the program in your local environment
```properties
SPRING_DATASOURCE_USERNAME=compose-postgres
SPRING_DATASOURCE_PASSWORD=compose-postgres
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/compose-postgres
TOKEN=WeatherApiToken
```
###Cluster Configuration

With the repo there have been inlcluded 2 docker compose files one for the database pods
and one for the endpoint microservice.

###API Documentation

You can have access to the documented endpoints here
[api](http://localhost:8080/v3/api-docs/)

### How to deliver the code

Please send an email containing your solution with a link to a public repository.

>**DO NOT create a Pull Request with your solution** 

### Footnote
It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
