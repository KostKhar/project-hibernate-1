package com.game.config;

import com.mysql.cj.xdevapi.SessionFactory;

import java.lang.module.Configuration;
import java.util.Properties;

public class MySessionFactory extends SessionFactory {
private SessionFactory sessionFactory;

    public MySessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
