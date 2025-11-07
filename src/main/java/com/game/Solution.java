package com.game;

import com.game.entity.Player;
import com.game.repository.PlayerRepositoryDB;

import java.util.Date;
import java.util.List;

import static com.game.entity.Profession.WARLOCK;
import static com.game.entity.Race.HUMAN;
import static java.time.Instant.now;

public class Solution {
    public static void main(String[] args) {
        Player player = new Player("Fibius", "Приходящий Без Шума", HUMAN, WARLOCK, Date.from(now()), false, 22);
        PlayerRepositoryDB playerRepositoryDB = new PlayerRepositoryDB();
//        playerRepositoryDB.save(player);
        List<Player> playerList = playerRepositoryDB.getAll(1, 3);
        for (Player p : playerList) {
            System.out.println(p.toString());
        }

//        playerRepositoryDB.getAllCount();
//        playerRepositoryDB.delete(player);
//        System.out.println(playerRepositoryDB.findById(player.getId()).toString());

//        playerRepositoryDB.delete(player);


    }
}
