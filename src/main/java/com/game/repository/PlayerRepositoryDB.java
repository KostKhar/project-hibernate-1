package com.game.repository;

import com.game.entity.Player;
import jakarta.annotation.PreDestroy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.game.config.MySessionFactory.getSessionFactory;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private final static Logger logger = LoggerFactory.getLogger(PlayerRepositoryDB.class);
    private SessionFactory sessionFactory;


    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        String sql = "FROM players";
        int offset = (pageNumber - 1) * pageSize;
        int limit = pageSize;

        try(Session Session = getSessionFactory().openSession()){
            NativeQuery<Player> query = Session.createNativeQuery(sql);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.list();
        } catch(Exception e){
            logger.error(e.getMessage());

        }
        throw new RuntimeException("Check data and retry");
    }

    @Override
    public int getAllCount() {
        try(Session Session = getSessionFactory().openSession()){
            Query<Player> query = Session.createNamedQuery("player.findAll");
            return query.list().size();
        }
    }

    @Override
    public Player save(Player player) {
        return null;
    }

    @Override
    public Player update(Player player) {
        return null;
    }

    @Override
    public Optional<Player> findById(long id) {
        return Optional.empty();
    }

    public PlayerRepositoryDB() {
        this.sessionFactory = getSessionFactory();
    }

    @Override
    public void delete(Player player) {

    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}