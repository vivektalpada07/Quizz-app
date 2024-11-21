package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.PlayerParticipationRepository;
import cs.quizzapp.prokect.backend.db.QuizRepository;
import cs.quizzapp.prokect.backend.models.PlayerParticipation;
import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.models.Quiz;
import cs.quizzapp.prokect.backend.payload.QuizRequest;
import cs.quizzapp.prokect.backend.utils.QuizCategoryMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionService questionService; // Instance of QuestionService

    @Autowired
    private PlayerParticipationRepository playerParticipationRepository;

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

    //Get ongoing or currently active quizzes.
    public List<Quiz> getOngoingQuizzes() {
        return quizRepository.findOngoingQuizzes(new Date());
    }

    //Get upcoming quizzes.
    public List<Quiz> getUpcomingQuizzes() {
        return quizRepository.findUpcomingQuizzes(new Date());
    }

    //Get past quizzes.
    public List<Quiz> getPastQuizzes() {
        return quizRepository.findPastQuizzes(new Date());
    }

    //Get participated quizzes.
    public List<Quiz> getParticipatedQuizzes(Long playerId) {
        return playerParticipationRepository.findParticipatedQuizzesByPlayerId(playerId);
    }

    //Add the players who are participating in the same quiz with same 10 questions.
    public List<Question> participateInQuiz(Long quizId, Long playerId) {
        // Fetch the quiz
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Check if the quiz is ongoing
        Date currentDate = new Date();
        if (currentDate.before(quiz.getStartDate()) || currentDate.after(quiz.getEndDate())) {
            throw new IllegalStateException("Player can only join ongoing quizzes");
        }

        // Check if the player is already participating
        if (quiz.getParticipants().contains(playerId)) {
            throw new IllegalStateException("Player is already participating in this quiz");
        }

        // Add the player to the participants
        quiz.getParticipants().add(playerId);
        quizRepository.save(quiz);

        // Retrieve the 10 questions assigned to this quiz
        List<Question> questions = quiz.getQuestions();
        if (questions.size() > 10) {
            questions = questions.subList(0, 10);
        }

        // Return the questions to the player
        return questions;
    }

    //Get 1 question at a time.
    public Map<String, Object> getCurrentQuestion(Long quizId, Long playerId) {
        // Fetch player's participation
        PlayerParticipation playerParticipation = playerParticipationRepository.findByQuizIdAndPlayerId(quizId, playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in any quiz participation"));

        int currentIndex = playerParticipation.getQuestionIndex();
        Quiz quiz = playerParticipation.getQuiz();

        if (quiz == null || quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            throw new IllegalStateException("Quiz or questions are not properly configured");
        }

        List<Question> questions = quiz.getQuestions();

        if (currentIndex < 0 || currentIndex >= questions.size()) {
            throw new IllegalStateException("No more questions available");
        }

        // Get the current question
        Question currentQuestion = questions.get(currentIndex);

        // Prepare a map with the question details
        Map<String, Object> response = new HashMap<>();
        response.put("question", currentQuestion.getQuestionText());
        response.put("options", currentQuestion.getOptions()); // Assuming getOptions returns a list of options
        response.put("questionId", currentQuestion.getId());

        return response;
    }

    //Display feedback according to correct and incorrect answers
    public Map<String, Object> submitAnswer(Long quizId, Long playerId, String answer) {
        PlayerParticipation playerParticipation = playerParticipationRepository.findByQuizIdAndPlayerId(quizId, playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in this quiz"));

        int currentIndex = playerParticipation.getQuestionIndex();
        Quiz quiz = playerParticipation.getQuiz();
        List<Question> questions = quiz.getQuestions();

        if (currentIndex >= questions.size()) {
            throw new IllegalStateException("No more questions available");
        }

        // Validate answer
        Question currentQuestion = questions.get(currentIndex);
        boolean isCorrect = currentQuestion.getCorrectAnswer().equalsIgnoreCase(answer);

        // Set feedback according to correct or incorrect answer
        String feedback;
        if (isCorrect) {
            feedback = "Correct! Well done.";
            playerParticipation.setScore(playerParticipation.getScore() + 1);
        } else {
            feedback = "Incorrect. The correct answer is: " + currentQuestion.getCorrectAnswer();
        }

        // Move to the next question
        playerParticipation.setQuestionIndex(currentIndex + 1);
        playerParticipationRepository.save(playerParticipation);

        // Return feedback and correctness in a map
        Map<String, Object> response = new HashMap<>();
        response.put("isCorrect", isCorrect);
        response.put("feedback", feedback);
        return response;
    }

    //Get the score
    public int getScore(Long quizId, Long playerId) {
        PlayerParticipation playerParticipation = playerParticipationRepository.findByQuizIdAndPlayerId(quizId, playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found in this quiz"));

        return playerParticipation.getScore();
    }

    //Like a quiz
    public void likeQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
        quiz.setLikesCount(quiz.getLikesCount() + 1);
        quizRepository.save(quiz);
    }

    //Unlike a quiz
    public void unlikeQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
        if (quiz.getLikesCount() > 0) {
            quiz.setLikesCount(quiz.getLikesCount() - 1);
            quizRepository.save(quiz);
        }
    }

    //Additional features
    //Replay a quiz
    public void replayQuiz(Long quizId, Long playerId) {
        // Fetch the quiz and player participation details
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Check if the quiz is ongoing
        Date currentDate = new Date();
        if (currentDate.before(quiz.getStartDate()) || currentDate.after(quiz.getEndDate())) {
            throw new IllegalStateException("Quiz is no longer active");
        }

        // Fetch the player's participation, or create a new one if it doesn't exist
        PlayerParticipation playerParticipation = playerParticipationRepository.findByQuizIdAndPlayerId(quizId, playerId)
                .orElseGet(() -> {
                    PlayerParticipation newParticipation = new PlayerParticipation();
                    newParticipation.setQuiz(quiz);
                    newParticipation.setId(playerId);
                    newParticipation.setQuestionIndex(0); // Start from the first question
                    newParticipation.setScore(0); // Reset score
                    return playerParticipationRepository.save(newParticipation);
                });

        // Reset player's progress (in case they have participated before)
        playerParticipation.setQuestionIndex(0);
        playerParticipation.setScore(0);
        playerParticipationRepository.save(playerParticipation);
    }

    //Add a rating
    public void addRating(Long quizId, int rating) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Update rating logic
        double totalRating = quiz.getRating() * quiz.getRatingCount() + rating;
        quiz.setRatingCount(quiz.getRatingCount() + 1);
        quiz.setRating(totalRating / quiz.getRatingCount());

        // Save updated quiz
        quizRepository.save(quiz);
    }




}
