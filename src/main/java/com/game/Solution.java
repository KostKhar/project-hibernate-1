package com.game;

import com.game.entity.Player;
import com.game.repository.PlayerRepositoryDB;

import java.util.Date;

import static com.game.entity.Profession.WARLOCK;
import static com.game.entity.Profession.WARRIOR;
import static com.game.entity.Race.HOBBIT;
import static com.game.entity.Race.HUMAN;
import static java.time.Instant.now;

public class Solution {
    public static void main(String[] args) {
        Player player = new Player( "Fibius", "Приходящий Без Шума", HUMAN, WARLOCK, Date.from(now()),false, 22);
        PlayerRepositoryDB playerRepositoryDB = new PlayerRepositoryDB();
        playerRepositoryDB.save(player);
//        System.out.println(playerRepositoryDB.getAll(1, 10).size());

//        playerRepositoryDB.getAllCount();
        playerRepositoryDB.delete(player);
        System.out.println(playerRepositoryDB.findById(1).toString());

//        playerRepositoryDB.delete(player);


    }
}
