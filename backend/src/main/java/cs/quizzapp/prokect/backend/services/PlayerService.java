package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.PlayerRepository;
import cs.quizzapp.prokect.backend.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new player
    public void registerPlayer(Player player) {
        if (playerRepository.findByUsername(player.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (playerRepository.findByEmail(player.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        player.setPassword(passwordEncoder.encode(player.getPassword())); // Encode the password
        playerRepository.save(player); // Save player
    }

    //Get a list of all the players
    public List<Player> getAllPlayers() {
        return playerRepository.findAll(); // Retrieve all players
    }

    //Get a player by the id
    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id); // Retrieve player by ID
    }

    //Update a player
    public Optional<Player> updatePlayer(Player player, Long id){
        return playerRepository.findById(id).map(existingPlayer -> {
            existingPlayer.setUsername(player.getUsername() != null ? player.getUsername() : existingPlayer.getUsername());
            existingPlayer.setEmail(player.getEmail() != null ? player.getEmail() : existingPlayer.getEmail());
            if (player.getPassword() != null && !player.getPassword().isEmpty()) {
                existingPlayer.setPassword(passwordEncoder.encode(player.getPassword()));
            }
            return playerRepository.save(existingPlayer);
        });
    }

    public void deletePlayer(Long id){
        playerRepository.deleteById(id);
    }
}
