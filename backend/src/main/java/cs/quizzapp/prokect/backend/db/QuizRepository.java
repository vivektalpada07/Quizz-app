package cs.quizzapp.prokect.backend.db;

import cs.quizzapp.prokect.backend.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
