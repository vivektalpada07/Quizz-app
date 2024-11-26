package cs.quizzapp.prokect.backend.db;

import cs.quizzapp.prokect.backend.models.Quiz;
import cs.quizzapp.prokect.backend.models.Score;
import cs.quizzapp.prokect.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    //This will get a score by user and quiz.
    Optional<Score> findByUserAndQuiz(User user, Quiz quiz);
}
