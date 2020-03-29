package com.blue.optima.assignment.controller.intercept.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blue.optima.assignment.controller.intercept.ControllerFilter;

@Configuration
public class ControllerFilterConfiguration {
	private ControllerFilter controllerFilter;	
	
	@Autowired
	public ControllerFilterConfiguration(ControllerFilter controllerFilter) {
		super();
		this.controllerFilter = controllerFilter;
	}

	@Bean
	public FilterRegistrationBean<ControllerFilter> filter() {
		FilterRegistrationBean<ControllerFilter> bean = new FilterRegistrationBean<>();

		bean.setFilter(controllerFilter);
		bean.addUrlPatterns("/records/*");

		return bean;
	}
}
