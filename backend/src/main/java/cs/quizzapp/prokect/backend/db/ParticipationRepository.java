package cs.quizzapp.prokect.backend.db;

import cs.quizzapp.prokect.backend.models.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByUserIdAndQuizId(Long userId, Long quizId);
}
