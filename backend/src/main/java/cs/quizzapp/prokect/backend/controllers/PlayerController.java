package cs.quizzapp.prokect.backend.controllers;

import cs.quizzapp.prokect.backend.models.Player;
import cs.quizzapp.prokect.backend.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping
    public void registerPlayer(@RequestBody Player player){
        playerService.registerPlayer(player);
    }

    @GetMapping
    public Optional<Player> getPlayerById(Long id){
        return playerService.getPlayerById(id);
    }

    @GetMapping
    public List<Player> getAllPlayers(){
        return playerService.getAllPlayers();
    }

    @PutMapping("/{id}")
    public void updatePlayer(@RequestBody Player player, @PathVariable Long id){
        playerService.updatePlayer(player, id);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id){
        playerService.deletePlayer(id);
    }
}
