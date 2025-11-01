package com.game.repository;

import com.game.config.MySessionFactory;
import com.game.entity.Player;
import com.mysql.cj.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final MySessionFactory sessionFactory;

    public PlayerRepositoryDB(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        String sql = "from players";


        return null;
    }

    @Override
    public int getAllCount() {
        return 0;
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

    @Override
    public void delete(Player player) {

    }

    @PreDestroy
    public void beforeStop() {

    }
}