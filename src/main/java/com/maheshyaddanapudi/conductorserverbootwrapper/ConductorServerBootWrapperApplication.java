package com.maheshyaddanapudi.conductorserverbootwrapper;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import com.maheshyaddanapudi.conductorserverbootwrapper.config.EnvironmentVariablesToSystemPropertiesMappingConfiguration;
import com.maheshyaddanapudi.conductorserverbootwrapper.constants.CSBWConstants;
import com.netflix.conductor.bootstrap.Main;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,ElasticsearchAutoConfiguration.class, RestClientAutoConfiguration.class})
public class ConductorServerBootWrapperApplication {

	private static String[] args_buffer;
	
	private Logger logger = LoggerFactory.getLogger(ConductorServerBootWrapperApplication.class.getSimpleName());
	
	@Autowired
	EnvironmentVariablesToSystemPropertiesMappingConfiguration environmentVariablesToSystemPropertiesMappingConfiguration;
	
	public static void main(String[] args) {
		args_buffer = args;
		SpringApplication.run(ConductorServerBootWrapperApplication.class, args);
	}
	
	@EventListener(ApplicationStartedEvent.class)
	public void mapEnvToProp()
	{
		this.environmentVariablesToSystemPropertiesMappingConfiguration.mapEnvToProp();
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void startConductorServer() {
		
		try {
			
			if(null!=args_buffer && args_buffer.length > 0)
		      {	
		        String[] args_new = new String[args_buffer.length-1];
		        
		        for(String arg: args_buffer)
		        {
		          if(!CSBWConstants.SPRING_ANSI_ENABLED_FLAG.equalsIgnoreCase(arg))
		          {
		            args_new[args_new.length] = arg;
		          }
		        }
		        
		        Main.main(args_new);
		      }
		      else
		      {	        
		        Main.main(args_buffer);
		    }

		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
	}

}
