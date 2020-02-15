package com.maheshyaddanapudi.conductorserverbootwrapper.config;

/*-
 * ========================LICENSE_START=================================
 * conductor-server-boot-wrapper
 * %%
 * Copyright (C) 2020 Mahesh Yaddanapudi
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * =========================LICENSE_END==================================
 */

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

@Configuration
@ConditionalOnProperty(
	    value="wrapper_db", 
	    havingValue = "mariadb4j", 
	    matchIfMissing = false)
public class EmbeddedMariaDbConfig {
	
	@Value("${mariaDB4j.maxConnections:1000}")
	public String maxConnections;
	
	@Value("${mariadb4j.dataDir:NONE}")
	public String mariadb4jDataDir;
	
	@Autowired
	Environment environment;
	
	@Autowired
	EnvironmentVariablesToSystemPropertiesMappingConfiguration environmentVariablesToSystemPropertiesMappingConfiguration;
	

    @Bean("mariaDB4jSpringService")
    public MariaDB4jSpringService mariaDB4jSpringService() {
    	MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
    	
    	mariaDB4jSpringService.getConfiguration().addArg("--max-connections="+maxConnections);
    	mariaDB4jSpringService.getConfiguration().addArg("--wait-timeout=31536000");
    	mariaDB4jSpringService.getConfiguration().addArg("--connect-timeout=31536000");
    	
    	
    	if(null == mariadb4jDataDir || "NONE".equalsIgnoreCase(mariadb4jDataDir))
    		mariaDB4jSpringService.getConfiguration().setDeletingTemporaryBaseAndDataDirsOnShutdown(true);
    	
    	mariaDB4jSpringService.getConfiguration().setSecurityDisabled(true);
    	
        return mariaDB4jSpringService;
    }

    @Bean("datasource")
    @DependsOn("mariaDB4jSpringService")
    public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService,
                          @Value("${app.mariaDB4j.databaseName:conductor}") String databaseName,
                          @Value("${spring.datasource.username:conductor}") String datasourceUsername,
                          @Value("${spring.datasource.password:password}") String datasourcePassword,
                          @Value("${spring.datasource.driver-class-name:org.mariadb.jdbc.Driver}") String datasourceDriver) throws ManagedProcessException {
        //Create our database with default root user and no password
        mariaDB4jSpringService.getDB().createDB(databaseName);

        DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();
        
        String databaseUrl = config.getURL(databaseName)+"?autoReconnect=true";
	    
	    System.setProperty("jdbc.url", databaseUrl);
	   
       return DataSourceBuilder
                .create()
                .username(datasourceUsername)
                .password(datasourcePassword)
                .url(databaseUrl)
                .driverClassName(datasourceDriver)
                .build();
    }
}
