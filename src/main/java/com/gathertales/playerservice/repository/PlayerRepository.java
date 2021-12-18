package com.gathertales.playerservice.repository;

import com.gathertales.playerservice.model.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository<T> extends JpaRepository<Player, Long> {
}
