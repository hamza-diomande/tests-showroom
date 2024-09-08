package com.oceane.dm.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.oceane.dm.models.repository")
@EntityScan(basePackages = "com.oceane.dm.models.model")
public class ExternalFeedbackApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ExternalFeedbackApplication.class,args);
    }
}
