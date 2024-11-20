package cs.quizzapp.prokect.backend.db;

import cs.quizzapp.prokect.backend.models.PlayerParticipation;
import cs.quizzapp.prokect.backend.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerParticipationRepository extends JpaRepository<PlayerParticipation, Long> {
    //Find participated quizzes by player id
    @Query("SELECT p.quiz FROM PlayerParticipation p WHERE p.player.id = :playerId")
    List<Quiz> findParticipatedQuizzesByPlayerId(Long playerId);

    //Find whether player participated in quiz using quiz id and player id
    @Query("SELECT p FROM PlayerParticipation p WHERE p.quiz.id = :quizId AND p.player.id = :playerId")
    Optional<PlayerParticipation> findByQuizIdAndPlayerId(@Param("quizId") Long quizId, @Param("playerId") Long playerId);
}
