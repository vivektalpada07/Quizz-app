package cs.quizzapp.prokect.backend.db;

import cs.quizzapp.prokect.backend.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByName(String name);
}
