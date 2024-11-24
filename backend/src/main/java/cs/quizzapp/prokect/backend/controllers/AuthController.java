package cs.quizzapp.prokect.backend.controllers;

import cs.quizzapp.prokect.backend.models.User;
import cs.quizzapp.prokect.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    /**
     * Endpoint for user login
     * @param body Contains username and password
     * @return ResponseEntity with success or failure message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        try {
            userService.authenticate(username, password); // Authenticate the user
            return ResponseEntity.ok("Login successful. You can now access the quiz.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials. Please try again.");
        }
    }

    /**
     * Endpoint for user registration
     * @param user User object containing registration details
     * @return ResponseEntity with success or failure message
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.registerUser(user); // Register the user
            return ResponseEntity.ok("User registered successfully. Please log in.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
