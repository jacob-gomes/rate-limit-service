package com.blue.optima.assignment;


import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author Jacob
 *
 */

@SpringBootApplication
@EnableSwagger2
public class App 
{
	public static void main( String[] args ){
    	SpringApplication.run(App.class, args);
    }
	
	@PostConstruct
	public void initTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}
}
