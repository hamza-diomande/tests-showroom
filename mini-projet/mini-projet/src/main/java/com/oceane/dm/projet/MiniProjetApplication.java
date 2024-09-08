package com.oceane.dm.projet;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 *

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.oceane.dm.models.repository")
@EntityScan(basePackages = "com.oceane.dm.models.model")
@ComponentScan(
        basePackages = {"com.oceane.dm.projet","com.oceane.dm.feedback.service"}
       // includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CaptchaFeedbackService.class)//,
        //useDefaultFilters = false // Désactive le scan par défaut pour éviter de scanner tout le package "com.oceane.dm.feedback.service"
) */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.oceane.dm.models.repository")
@EntityScan(basePackages = "com.oceane.dm.models.model")
@ComponentScan(basePackages = {"com.oceane.dm.projet"})
public class MiniProjetApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(MiniProjetApplication.class,args);
    }
}
