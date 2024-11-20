package cs.quizzapp.prokect.backend.controllers;

import cs.quizzapp.prokect.backend.models.Player;
import cs.quizzapp.prokect.backend.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/players")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        try {
            playerService.registerPlayer(player);
            return ResponseEntity.ok(player); // Return the created Player object
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Return 400 without a body
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Return 500 without a body
        }
    }

    @GetMapping("/{id}")
    public Optional<Player> getPlayerById(@PathVariable Long id){
        return playerService.getPlayerById(id);
    }

    @GetMapping
    public List<Player> getAllPlayers(){
        return playerService.getAllPlayers();
    }

    @PutMapping("/{id}")
    public void updatePlayer(@RequestBody Player player, @PathVariable Long id) {
        player.setPassword(passwordEncoder.encode(player.getPassword())); // Encode updated password
        playerService.updatePlayer(player, id);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id){
        playerService.deletePlayer(id);
    }
}
