package com.game.config;


import com.game.entity.Player;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;


@Configuration
@ComponentScan("com.game")
public class AppConfig {
    public static SessionFactory getSessionFactory() throws IOException {
        final Logger log = LoggerFactory.getLogger(AppConfig.class);
        Properties properties = new Properties();

        // Конфигурация для Hibernate 5
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/myDB");
        properties.put("hibernate.connection.username", "postgres");
        properties.put("hibernate.connection.password", "postgres");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.current_session_context_class", "thread");
        properties.put("hibernate.hbm2ddl.auto", "update");

        // Дополнительные настройки для Hibernate 5
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        try {
            SessionFactory sf = new org.hibernate.cfg.Configuration()
                    .setProperties(properties)
                    .addAnnotatedClass(Player.class)
                    .buildSessionFactory();
            log.info("SessionFactory created successfully");
            return sf;
        } catch (Exception e) {
            log.error("Failed to create SessionFactory", e);
            throw e;
        }

    }

}