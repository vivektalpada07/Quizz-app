package cs.quizzapp.prokect.backend.db;

import cs.quizzapp.prokect.backend.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
