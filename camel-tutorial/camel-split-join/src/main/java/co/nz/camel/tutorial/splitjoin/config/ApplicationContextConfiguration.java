package co.nz.camel.tutorial.splitjoin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import co.nz.camel.tutorial.config.CamelSpringContextConfig;

@Configuration
@ComponentScan(basePackages = "co.nz.camel.tutorial.splitjoin")
@Import(value = CamelSpringContextConfig.class)
public class ApplicationContextConfiguration {

}
