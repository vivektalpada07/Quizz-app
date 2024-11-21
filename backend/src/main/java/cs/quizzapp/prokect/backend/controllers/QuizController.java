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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Get ongoing quizzes
    @GetMapping("/ongoing")
    public ResponseEntity<List<Quiz>> getOngoingQuizzes() {
        return ResponseEntity.ok(quizService.getOngoingQuizzes());
    }

    // Get upcoming quizzes
    @GetMapping("/upcoming")
    public ResponseEntity<List<Quiz>> getUpcomingQuizzes() {
        return ResponseEntity.ok(quizService.getUpcomingQuizzes());
    }

    // Get past quizzes
    @GetMapping("/past")
    public ResponseEntity<List<Quiz>> getPastQuizzes() {
        return ResponseEntity.ok(quizService.getPastQuizzes());
    }

    // Get participated quizzes
    @GetMapping("/participate")
    public ResponseEntity<List<Quiz>> getParticipatedQuizzes(@RequestParam Long playerId) {
        List<Quiz> quizzes = quizService.getParticipatedQuizzes(playerId);
        return ResponseEntity.ok(quizzes);
    }

    // Participate in quiz
    @PostMapping("/{quizId}/participate")
    public ResponseEntity<?> participateInQuiz(
            @PathVariable Long quizId,
            @RequestParam Long playerId) {
        try {
            // Call the service method to get the list of questions
            List<Question> questions = quizService.participateInQuiz(quizId, playerId);

            // Return the list of questions in the response
            return ResponseEntity.ok(questions);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get 1 question at a time for a specific player
    @GetMapping("/{quizId}/question/{playerId}")
    public ResponseEntity<Map<String, Object>> getCurrentQuestion(
            @PathVariable Long quizId,
            @PathVariable Long playerId) {
        try {
            // Call the service method to get the current question as a map
            Map<String, Object> question = quizService.getCurrentQuestion(quizId, playerId);
            return ResponseEntity.ok(question);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build(); // Handle errors gracefully
        }
    }


    // After submit answer it will display the feedback according to correct or incorrect answers.
    @PostMapping("/{quizId}/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @PathVariable Long quizId,
            @RequestParam Long playerId,
            @RequestParam String answer) {
        try {
            // Call the service method to get the response
            Map<String, Object> response = quizService.submitAnswer(quizId, playerId, answer);

            // Return the response map
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Handle exceptions and create an error response map
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("isCorrect", false);
            errorResponse.put("feedback", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get the score and display out of 10
    @GetMapping("/{quizId}/results/{playerId}")
    public ResponseEntity<Map<String, Object>> getPlayerResults(
            @PathVariable Long quizId,
            @PathVariable Long playerId) {
        try {
            int score = quizService.getScore(quizId, playerId);
            Map<String, Object> results = new HashMap<>();
            results.put("score", score);
            results.put("total", 10); // Assuming each quiz has 10 questions
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Post a like
    @PostMapping("/{quizId}/like")
    public ResponseEntity<String> likeQuiz(@PathVariable Long quizId) {
        try {
            quizService.likeQuiz(quizId);
            return ResponseEntity.ok("Liked successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error liking quiz");
        }
    }

    // Post an unlike
    @PostMapping("/{quizId}/unlike")
    public ResponseEntity<String> unlikeQuiz(@PathVariable Long quizId) {
        try {
            quizService.unlikeQuiz(quizId);
            return ResponseEntity.ok("Unliked successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error unliking quiz");
        }
    }

    // Additional features
    // Play the quiz again
    @PostMapping("/replay/{quizId}/{playerId}")
    public ResponseEntity<?> replayQuiz(@PathVariable Long quizId, @PathVariable Long playerId) {
        try {
            // Call the service method to reset the player's participation
            quizService.replayQuiz(quizId, playerId);

            // Fetch the first question after resetting progress
            Map<String, Object> currentQuestion = quizService.getCurrentQuestion(quizId, playerId);

            // Return the first question in the response
            return ResponseEntity.ok(currentQuestion);
        } catch (Exception e) {
            // Handle exceptions (like invalid quiz ID or player ID)
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get the rating
    @GetMapping("/{quizId}/rating")
    public ResponseEntity<Map<String, Object>> getQuizRating(@PathVariable Long quizId) {
        Quiz quiz = quizService.getQuizById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("rating", quiz.getRating());
        response.put("ratingCount", quiz.getRatingCount());
        return ResponseEntity.ok(response);
    }

    // Add the rating
    @PostMapping("/{quizId}/rate")
    public ResponseEntity<String> rateQuiz(
            @PathVariable Long quizId,
            @RequestParam int rating) {
        if (rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5.");
        }

        try {
            quizService.addRating(quizId, rating);
            return ResponseEntity.ok("Rating submitted successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
