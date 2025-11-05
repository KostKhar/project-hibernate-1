package com.game.repository;

import com.game.config.AppConfig;
import com.game.entity.Player;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NamedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;



@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private static final Logger log = LoggerFactory.getLogger(PlayerRepositoryDB.class);
    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        try {
            this.sessionFactory = AppConfig.getSessionFactory();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;

        try (Session session = sessionFactory.openSession()) {
            NativeQuery<Player> query = session.createNativeQuery(
                    "SELECT * FROM rpg.player", Player.class);
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
            return query.list();

        } catch (Exception ex) {
            log.error("Error in getAll method", ex);
            throw new IllegalArgumentException("getAll player failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public int getAllCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createNamedQuery("player.getCount", Long.class);
            Long count = query.uniqueResult();
            return count.intValue();
        }catch(Exception ex) {
            log.error(ex.getMessage());
        }
        log.error("getAllCount player failed");
        throw  new IllegalArgumentException("getAllCount player failed");
    }

    @Override
    public Player save(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
            return player;
        }catch(Exception ex) {
            log.error(ex.getMessage());
        }
        log.error("Save player failed");
        throw  new IllegalArgumentException("Save player failed");
    }

    @Override
    public Player update(Player player) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
            return player;
        }catch(Exception ex) {
            log.error(ex.getMessage());
        }
        log.error("Update player failed");
        throw  new IllegalArgumentException("Update player failed");
    }

    @Override
    public Optional<Player> findById(long id) {
        try(Session session = sessionFactory.openSession()) {
            Query<Player> query = session.createNamedQuery("player.findById", Player.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        }catch(Exception ex) {
            log.error(ex.getMessage());
        }
        log.error("Find By id  player failed");
        throw  new IllegalArgumentException("Find By id player failed");
    }

    @Override
    public void delete(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Проверяем, существует ли игрок в базе
            Player existingPlayer = session.get(Player.class, player.getId());
            if (existingPlayer == null) {
                throw new IllegalArgumentException("Player with ID " + player.getId() + " not found");
            }

            session.delete(existingPlayer);
            transaction.commit();
            log.info("Player deleted successfully with ID: {}", player.getId());

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error deleting player with ID: {}", player.getId(), ex);
            throw new IllegalArgumentException("Delete player failed: " + ex.getMessage(), ex);
        }
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}