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

import java.util.Iterator;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import com.maheshyaddanapudi.conductorserverbootwrapper.constants.CSBWConstants;

@Configuration
public class EnvironmentVariablesToSystemPropertiesMappingConfiguration {

	@Autowired
	ApplicationContext ctx;

	public void mapEnvToProp() {

		Properties props = new Properties();

		for (Iterator it = ((AbstractEnvironment) ctx.getEnvironment()).getPropertySources().iterator(); it.hasNext();) {
			PropertySource propertySource = (PropertySource) it.next();
			if (propertySource instanceof OriginTrackedMapPropertySource) {

				props.putAll(((MapPropertySource) propertySource).getSource());

			}
		}
		
		if(System.getProperty(CSBWConstants.WRAPPER_DB) != null && (props.getProperty(CSBWConstants.WRAPPER_DB) == null || !System.getProperty(CSBWConstants.WRAPPER_DB).equalsIgnoreCase(props.getProperty(CSBWConstants.WRAPPER_DB))))
		{
			System.setProperty(CSBWConstants.WRAPPER_DB, CSBWConstants.NONE);
		}
		
		if(System.getProperty(CSBWConstants.DB) != null && (props.getProperty(CSBWConstants.DB) == null || !System.getProperty(CSBWConstants.DB).equalsIgnoreCase(props.getProperty(CSBWConstants.DB))))
		{
			System.setProperty(CSBWConstants.DB, CSBWConstants.MEMORY);
		}
		
		props.forEach((type, value) -> {
			System.setProperty(String.valueOf(type), String.valueOf(value));
		});
	}

}
