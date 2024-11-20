package cs.quizzapp.prokect.backend;

import cs.quizzapp.prokect.backend.db.PlayerRepository;
import cs.quizzapp.prokect.backend.models.Player;
import cs.quizzapp.prokect.backend.models.User;
import cs.quizzapp.prokect.backend.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// Check if admin user exists, if not, create it
		if (userRepository.findByUsername("admin").isEmpty()) {
			User admin = new User(
					"admin",
					"admin@example.com",
					passwordEncoder.encode("P@ss123"),
					"ADMIN",  // Role as ADMIN
					"Admin",  // First Name
					"User",   // Last Name
					null      // No profile picture for the admin
			);
			userRepository.save(admin);
			System.out.println("Admin user created: Username: admin, Password: P@ss123");
		} else {
			System.out.println("Admin user already exists.");
		}

		// Check if player exists in the player table
		Optional<Player> playerOptional = playerRepository.findByUsername("player");

		if (playerOptional.isPresent()) {
			// Player exists, so now check if the user already exists in the users table
			Optional<User> existingUser = userRepository.findByUsername("player");

			if (existingUser.isEmpty()) {
				// Player exists, and user does not exist, so create a new user
				Player player = playerOptional.get(); // Retrieve the player

				// Create a new User based on the Player data
				User newUser = new User(
						player.getUsername(),
						player.getEmail(),
						player.getPassword(),  // Ensure password is encoded
						"PLAYER",  // Assign the role as PLAYER
						player.getFirstName(),
						player.getLastName(),
						null  // No profile picture for now (you can add one if needed)
				);

				// Save the new user to the users table
				userRepository.save(newUser);
				System.out.println("User created in users table: Username: " + newUser.getUsername());
			} else {
				// User already exists
				System.out.println("User already exists in users table.");
			}
		} else {
			// Player not found in the player table
			System.out.println("Player not found in player table.");
		}

	}
}
