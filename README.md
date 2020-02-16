
# Conductor Server Boot Wrapper

Conductor Server Boot Wrapper = Spring Boot wrapped over Conductor Server with Embedded MariaDB4j


The idea is to build a single jar of configurable nature with usual application.yml and the Spring Boot uber Wrapper Jar would include Netflix Conductor jar and also MariaDB4j.

This opens the possibility of also implementing multiple Spring technologies like Cloud Config, Eureka Discovery, Zuul Gateway etc. on top of Conductor by simply applying them to the parent Spring Boot wrapper.

 • Spring Boot - Conductor Server - embedded MariaDB support & external MySQL DB support etc. as offered by Conductor.

The catch point being, it will be a just a wrapper which survives the purpose of bringing Conductor under Spring framework umbrella in a way .

Thus eliminating the need to do a completely do Guice to Spring Boot rewrite.

## Build Status

[![Build Status](https://travis-ci.com/maheshyaddanapudi/conductor-server-boot-wrapper.svg?branch=develop)](https://travis-ci.com/maheshyaddanapudi/conductor-server-boot-wrapper)

## Sample application

Deployed to Pivotal Cloud Foundry (PCF) 

[Sample Aapplication](https://conductor-server-boot-wrapper.cfapps.io/)

This version is working with embedded MariaDB4J as Database than the in memory DynoDB.

## Setup / Build & Startup Instructions

 • Clone / Download and extract the Repo

 • Package the jar first.

    ./mvnw install

 • Run the jar.
 
    Navigate to the target directory (Where the Jar is built)

      cd target

    1. Running in default - In Memory DB mode
    
      java -jar conductor-server-boot-wrapper-2.224.0-all.jar
      
    2. Running with Embedded MariaDB4j - 
    
      Create a folder config. Copy the src/main/resources/application_mariadb4j.yml to target/config/application.yml
        
        mkdir config
        cp ../src/main/resources/application_mariadb4j.yml config/application.yml
        
      Provide property values as shown in sample below. These are already the defaults.
      
        spring:
          application:
            name: conductor-server-boot-wrapper
          datasource:
            driver-class-name: org.mariadb.jdbc.Driver
            username: conductor
            password: password
        wrapper_db: mariadb4j
        db: mysql
        jdbc:
          username: ${spring.datasource.username}
          password: ${spring.datasource.password}  
        conductor:
          mysql:
            connection:
              timeout: 31536000
              idle:
                timeout: 31536000 
              pool:
                idle:
                  min: 2
                size:
                  max: 10000
        
      Run Jar
        
        java -jar conductor-server-boot-wrapper-2.224.0-all.jar
      
    3. Running with External Mysql - 
    
      Create a folder config. Copy the src/main/resources/application_mysql.yml to target/config/application.yml
        
        mkdir config
        cp ../src/main/resources/application_mysql.yml config/application.yml
        
      Provide property values as shown in sample below
      
        spring:
          application:
            name: conductor-server-boot-wrapper
        server:
          port: 0
        logging:
          level:
            root: info
        awaitTermination: 300
        db: mysql
        jdbc:
          url: jdbc:mysql://localhost:3306/conductor
          username: root
          password: Root!23$  
        conductor:
          mysql:
            connection:
              timeout: 31536000
              idle:
                timeout: 31536000 
              pool:
                idle:
                  min: 2
                size:
                  max: 10000
        
      Run Jar
        
        java -jar conductor-server-boot-wrapper-2.224.0-all.jar
        

 • To access the Swagger UI, please use the url sample below for reference.

http://localhost:8080/


## Motivation

There is a saying from recent times that "Every idea comes from a pain point"

For developers who are having affinity to Spring Boot or for projects which have Spring frame work and would like to introduce Netflix Conductor as Orchestrator, there is this hurdle of Guice to Spring conversion.

To avoid all the pain of rewriting an entire code base in different framework, a simple wrapper is created.

## Tech/framework used

 • conductor-server - Presumably downloaded from Maven repository

 • Java Spring Boot : Mariadb4j

 • Maven - License, Shade & Jar plugins


## Inspiration

 • Netflix Conductor
 
 • Spring Framework

 • MariaDb4J


## Credits

 • Mahesh Yaddanapudi - zzzmahesh@live.com
