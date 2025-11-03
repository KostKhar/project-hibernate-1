package com.game.config;

import com.game.entity.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MySessionFactory {

    private static SessionFactory sessionFactory;

    private MySessionFactory() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                SessionFactory sessionFactory  = new Configuration().configure()
                .addAnnotatedClass(Player.class).buildSessionFactory();
                return sessionFactory;
            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}
