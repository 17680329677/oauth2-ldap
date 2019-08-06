package com.aispeech.corpoauthserver.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @Author: hezhe.du
 * @Date: 2019/7/19 0019 18:14
 */

@Configuration
public class DataSourceConfiguration {

    private static String MYSQL_CONNECTION_STRING = "BA_KG_OAUTH_MYSQL";

    @Autowired
    private Environment environment;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.url(environment.getProperty(MYSQL_CONNECTION_STRING));
        return dataSourceBuilder.build();
    }

}
