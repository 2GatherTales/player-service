package com.gathertales.playerservice.model.player;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="player", schema = "avarum_game")
@TypeDef(name = "json", typeClass = JsonType.class)
@Data
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "userid")
    private Long userid;
    @Column(name = "weaponid")
    private Long weaponID;

    @Type(type = "json")
    private Map<String, Object> weapon = new HashMap<>();

    @Column(name = "maxhp")
    private Integer maxhp;
    @Column(name = "hp")
    private Integer hp;
    @Column(name = "str")
    private Integer str;
    @Column(name = "speed")
    private Integer speed;
    @Column(name = "potions")
    private Integer potions;

    @Column(name = "dead", columnDefinition = "int2")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean dead;

    public Integer calcGetAttacked(Integer dmg){
        return this.hp-dmg;
    }
}