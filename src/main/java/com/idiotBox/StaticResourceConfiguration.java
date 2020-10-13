package com.idiotBox;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("Hey in config");
    	registry.addResourceHandler("/css/**").addResourceLocations("classpath:/META-INF/static");
    	registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/META-INF/static");
    	registry.addResourceHandler("/img/**").addResourceLocations("classpath:/META-INF/static");
    	registry.addResourceHandler("/js/**").addResourceLocations("classpath:/META-INF/static");
    	super.addResourceHandlers(registry);
    }
}
