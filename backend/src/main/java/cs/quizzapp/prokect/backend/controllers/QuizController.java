package cs.quizzapp.prokect.backend.controllers;

import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.models.Quiz;
import cs.quizzapp.prokect.backend.payload.QuizRequest;
import cs.quizzapp.prokect.backend.services.QuestionService;
import cs.quizzapp.prokect.backend.services.QuizService;
import cs.quizzapp.prokect.backend.utils.QuizCategoryMapper;
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
    private QuestionService questionService;

    /**
     * Create a new quiz and fetch questions dynamically from OpenTDB.
     */
    @PostMapping
    public ResponseEntity<String> createQuizWithQuestions(@RequestBody QuizRequest quizRequest) {
        try {
            // Validate the category
            Integer categoryId = QuizCategoryMapper.getCategoryId(quizRequest.getCategory());
            if (categoryId == null) {
                return ResponseEntity.badRequest().body("Invalid category. Available categories: " + QuizCategoryMapper.getAllCategories());
            }

            // Set the numeric category ID
            quizRequest.setCategoryId(categoryId);

            // Check if a quiz with the same name already exists
            Quiz quiz = quizService.getQuizByName(quizRequest.getName());
            if (quiz == null) {
                // Create a new quiz if it doesn't exist
                quiz = quizService.createQuizWithQuestions(quizRequest);
            }
            // Fetch and save questions using the injected QuestionService instance
            List<Question> questions = questionService.fetchAndSaveQuestions(quizRequest, quiz);

            if (questions.isEmpty()) {
                return ResponseEntity.badRequest().body("Failed to fetch questions. Check your inputs.");
            }

            return ResponseEntity.ok("Quiz created successfully with " + questions.size() + " questions.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating quiz: " + e.getMessage());
        }
    }

    /**
     * Get all quizzes.
     */
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    /**
     * Get a quiz by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return ResponseEntity.of(quizService.getQuizById(id));
    }
    @GetMapping("/categories")
    public ResponseEntity<?> getAvailableCategories() {
        return ResponseEntity.ok(QuizCategoryMapper.getAllCategories());
    }
    /**
     * Delete a quiz.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        boolean isDeleted = quizService.deleteQuiz(id);
        return isDeleted ? ResponseEntity.ok("Quiz deleted successfully.") : ResponseEntity.notFound().build();
    }
}
