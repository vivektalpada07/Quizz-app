package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.QuizRepository;
import cs.quizzapp.prokect.backend.models.Quiz;
import cs.quizzapp.prokect.backend.payload.QuizRequest;
import cs.quizzapp.prokect.backend.utils.QuizCategoryMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionService questionService; // Instance of QuestionService

    /**
     * Creates a new quiz and fetches questions from OpenTDB.
     * @param quizRequest The input data for the quiz.
     * @return The saved Quiz object.
     */
    public Quiz createQuizWithQuestions(QuizRequest quizRequest) {
        // Map category name to ID using QuizCategoryMapper
        Integer categoryId = QuizCategoryMapper.getCategoryId(quizRequest.getCategory());
        if (categoryId == null) {
            throw new IllegalArgumentException("Invalid category name provided: " + quizRequest.getCategory());
        }
        // Create a new Quiz object
        Quiz quiz = new Quiz();
        quiz.setName(quizRequest.getName());
        quiz.setCategory(quizRequest.getCategory());
        quiz.setDifficulty(quizRequest.getDifficulty());
        quiz.setStartDate(quizRequest.getStartDate());
        quiz.setEndDate(quizRequest.getEndDate());

        // Save the Quiz
        Quiz savedQuiz = saveQuiz(quiz);

        // Fetch and save questions for the quiz using the QuestionService
        questionService.fetchAndSaveQuestions(quizRequest, savedQuiz);

        return savedQuiz;
    }

    /**
     * Saves the Quiz object in the database.
     * @param quiz The quiz to save.
     * @return The saved Quiz object.
     */
    private Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    /**
     * Updates an existing quiz by ID.
     * @param id The ID of the quiz to update.
     * @param updatedQuiz The updated Quiz object.
     * @return The updated Quiz object, or null if not found.
     */
    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            quiz.setName(updatedQuiz.getName());
            quiz.setStartDate(updatedQuiz.getStartDate());
            quiz.setEndDate(updatedQuiz.getEndDate());
            return quizRepository.save(quiz);
        }
        return null;
    }

    /**
     * Retrieves all quizzes from the database.
     * @return A list of quizzes.
     */
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    /**
     * Retrieves a quiz by its ID.
     * @param id The ID of the quiz.
     * @return An optional Quiz object.
     */
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }
    public Quiz getQuizByName(String name) {
        return quizRepository.findByName(name).orElse(null);
    }
    /**
     * Deletes a quiz by its ID.
     * @param id The ID of the quiz.
     * @return true if the quiz was deleted, false otherwise.
     */
    public boolean deleteQuiz(Long id) {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
