package cs.quizzapp.prokect.backend.db;

import cs.quizzapp.prokect.backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
