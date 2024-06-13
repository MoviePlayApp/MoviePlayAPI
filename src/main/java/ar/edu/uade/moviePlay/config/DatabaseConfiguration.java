package ar.edu.uade.moviePlay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {
    @Value("${spring.datasource.driverClassName}")
    private String DRIVER_CLASS_NAME;

    @Value("${spring.datasource.url}")
    private String DATA_SOURCE_URL;

    @Value("${spring.datasource.username}")
    private String DATA_SOURCE_USERNAME;

    @Value("${spring.datasource.password}")
    private String DATA_SOURCE_PASSWORD;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(DATA_SOURCE_URL);
        dataSource.setUsername(DATA_SOURCE_USERNAME);
        dataSource.setPassword(DATA_SOURCE_PASSWORD);
        return dataSource;
    }
}
