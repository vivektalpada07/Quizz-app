package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.PlayerRepository;
import cs.quizzapp.prokect.backend.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    //Register a new player
    public void registerPlayer(Player player){
        playerRepository.save(player);
    }

    //Find a player by their id
    public Optional<Player> getPlayerById(Long id){
        return playerRepository.findById(id);
    }

    //Get a list of all the players
    public List<Player> getAllPlayers(){
        List<Player> players = new ArrayList<>();
        playerRepository.findAll().forEach(players::add);
        return players;
    }

    //Update a player
    public Optional<Player> updatePlayer(Player player, Long id){
        return playerRepository.findById(id).map(existingPlayer -> {
            existingPlayer.setUsername(player.getUsername() != null ? player.getUsername() : existingPlayer.getUsername());
            existingPlayer.setEmail(player.getEmail() != null ? player.getEmail() : existingPlayer.getEmail());

            return playerRepository.save(existingPlayer);
        });
    }

    public void deletePlayer(Long id){
        playerRepository.deleteById(id);
    }
}
