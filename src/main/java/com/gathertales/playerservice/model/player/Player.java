package com.gathertales.playerservice.model.player;

import com.google.common.primitives.Bytes;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="player", schema = "avarum_game")
@TypeDefs({
        @TypeDef(
                name = "int-array",
                typeClass = IntArrayType.class
        )
})
@Data
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "userid")
    private Long userid;
    @Column(name = "potion_chance")
    private Short potionChance;
    @Column(name = "potion_quantity")
    private Short potionQuantity;
    @Column(name = "potion_heal", columnDefinition = "integer[]")
    @Type( type = "int-array" )
    private Integer[] potionHeal;

    @Column(name = "max_hp")
    private Integer maxhp;
    @Column(name = "hp")
    private Integer hp;
    @Column(name = "str")
    private Integer str;
    @Column(name = "speed")
    private Integer speed;
    @Column(name = "dmg")
    private Integer dmg;



}