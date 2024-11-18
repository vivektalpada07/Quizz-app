package cs.quizzapp.prokect.backend.controllers;

import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.models.Quiz;
import cs.quizzapp.prokect.backend.payload.QuizRequest;
import cs.quizzapp.prokect.backend.services.QuizService;
import cs.quizzapp.prokect.backend.services.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private TriviaService triviaService;

    // Create a new quiz tournament
    @PostMapping
    public ResponseEntity<String> createQuiz(@RequestBody QuizRequest quizRequest) {
        try {
            // Create a new quiz
            Quiz quiz = quizService.createQuizWithQuestions(quizRequest);

            // Fetch and save questions
            List<Question> questions = triviaService.fetchAndSaveQuestions(quizRequest, quiz);

            if (questions.isEmpty()) {
                return ResponseEntity.badRequest().body("Failed to fetch questions. Check your inputs.");
            }

            return ResponseEntity.ok("Quiz created successfully with " + questions.size() + " questions.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating quiz: " + e.getMessage());
        }
    }

    // Get all quizzes
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    // Get a quiz by ID
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return ResponseEntity.of(quizService.getQuizById(id));
    }

    // Delete a quiz
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        boolean isDeleted = quizService.deleteQuiz(id);
        return isDeleted ? ResponseEntity.ok("Quiz deleted successfully.") : ResponseEntity.notFound().build();
    }
}
