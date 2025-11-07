package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@ToString
@Table(name = "player", schema = "rpg")
@NamedQueries({
        @NamedQuery(name = "player.getCount", query = "SELECT count(p) FROM Player p"),
        @NamedQuery(name = "player.findById", query = "SELECT p FROM Player p WHERE p.id = :id")
})
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 12)
    private String name;
    @Column(name = "title", nullable = false, length = 30)
    private String title;
    @Column(name = "race", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Race race;
    @Column(name = "profession", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Profession profession;
    @DateTimeFormat
    @Column(name = "birthday", nullable = false)
    private Date birthday;
    @Column(name = "banned", nullable = false)
    private Boolean banned;
    @Column(name = "level", nullable = false)
    private Integer level;

    @CreationTimestamp
    private Date created;
    @UpdateTimestamp
    private Date updated;

    public Player() {
    }

    public Player(String name, String title, Race race, Profession profession, Date birthday, Boolean banned, Integer level) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.level = level;
    }

    public Player(Long id, String name, String title, Race race, Profession profession, Date birthday, Boolean banned, Integer level) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.level = level;
    }

}