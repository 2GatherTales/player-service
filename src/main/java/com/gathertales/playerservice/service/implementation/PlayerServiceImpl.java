package com.gathertales.playerservice.service.implementation;

import com.gathertales.playerservice.model.player.Player;
import com.gathertales.playerservice.repository.PlayerRepository;
import com.gathertales.playerservice.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("playerService")
public class PlayerServiceImpl implements GenericService<Player> {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Iterable<Player> findAll() { return playerRepository.findAll(); }

    @Override
    public Player find(Long id) { return (Player) playerRepository.findById(id).get(); }

    @Override
    public Player create(Player player) {   return (Player) playerRepository.save(player); }

    @Override
    public void update(Player player) { playerRepository.save(player); }

    @Override
    public void delete(Long id) { playerRepository.delete(id); }
}
