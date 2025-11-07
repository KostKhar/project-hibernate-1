package com.game.repository;

import com.game.config.AppConfig;
import com.game.entity.Player;
import jakarta.annotation.PreDestroy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
        if (pageNumber < 1) pageNumber = 1;
        if (pageSize < 1) pageSize = 10;

        int offset = (pageNumber - 1) * pageSize;

        try (Session session = sessionFactory.openSession()) {
            Query<Player> query = session.createQuery(
                    "FROM Player p ORDER BY p.id DESC", Player.class);
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
            return query.list();

        } catch (Exception ex) {
            log.error("Error in getAll method with pageNumber: {}, pageSize: {}",
                    pageNumber, pageSize, ex);
            throw new IllegalArgumentException("getAll player failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public int getAllCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createNamedQuery("player.getCount", Long.class);
            Long count = query.uniqueResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        log.error("getAllCount player failed");
        throw new IllegalArgumentException("getAllCount player failed");
    }

    @Override
    public Player save(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            Player existingPlayer = session
                    .createQuery("FROM Player WHERE name = :name", Player.class)
                    .setParameter("name", player.getName())
                    .uniqueResult();

            transaction = session.beginTransaction();

            if (existingPlayer != null) {
                log.info("Player with name '" + player.getName() + "' already exists, updating...");
                existingPlayer.setTitle(player.getTitle());
                existingPlayer.setRace(player.getRace());
                existingPlayer.setProfession(player.getProfession());
                existingPlayer.setBirthday(player.getBirthday());
                existingPlayer.setBanned(player.getBanned());
                existingPlayer.setLevel(player.getLevel());
                session.merge(existingPlayer);
                transaction.commit();
                return existingPlayer;
            } else {
                session.save(player);
                transaction.commit();
                log.info("Player '" + player.getName() + "' saved successfully.");
                return player;
            }
        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            log.error("Error saving player: " + ex.getMessage(), ex);
            throw ex;
        }
    }


    @Override
    public Player update(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.update(player);
            transaction.commit();
            return player;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        log.error("Update player failed");
        throw new IllegalArgumentException("Update player failed");
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Player> query = session.createNamedQuery("player.findById", Player.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        log.error("Find By id  player failed");
        throw new IllegalArgumentException("Find By id player failed");
    }

    @Override
    public void delete(Player player) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();
            Player existingPlayer = session.get(Player.class, player.getId());
            if (existingPlayer == null) {
                log.info("Player with ID " + player.getId() + " not found");
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