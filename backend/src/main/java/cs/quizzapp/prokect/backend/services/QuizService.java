package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.QuestionRepository;
import cs.quizzapp.prokect.backend.db.QuizRepository;
import cs.quizzapp.prokect.backend.db.ScoreRepository;
import cs.quizzapp.prokect.backend.db.UserRepository;
import cs.quizzapp.prokect.backend.dto.QuestionDTO;
import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.models.Quiz;
import cs.quizzapp.prokect.backend.models.Score;
import cs.quizzapp.prokect.backend.models.User;
import cs.quizzapp.prokect.backend.payload.QuizRequest;
import cs.quizzapp.prokect.backend.utils.QuizCategoryMapper;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionService questionService; // Instance of QuestionService

    @Autowired
    private UserRepository userRepository;

    private ScoreRepository scoreRepository;

    private QuestionRepository questionRepository;

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
     * @param updatedquizRequest The input data for updating the quiz.
     * @return true if the update was successful, false otherwise.
     */
    public Quiz updateQuiz(Long id, QuizRequest updatedquizRequest) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            if (updatedquizRequest.getName() != null) {
                quiz.setName(updatedquizRequest.getName());
            }
            if (updatedquizRequest.getStartDate() != null) {
                quiz.setStartDate(updatedquizRequest.getStartDate());
            }
            if (updatedquizRequest.getEndDate() != null) {
                quiz.setEndDate(updatedquizRequest.getEndDate());
            }
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
    public boolean createCategory(String categoryName) {
        return QuizCategoryMapper.addCategory(categoryName);
    }
    public boolean deleteCategory(String categoryName) {
        return QuizCategoryMapper.deleteCategory(categoryName);
    }

    //Get ongoing or currently active quizzes.
    public List<Quiz> getOngoingQuizzes(Date currentDate) {
        return quizRepository.findOngoingQuizzes(currentDate);
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
    public List<Quiz> getParticipatedQuizzes(Long userId) {
        return quizRepository.findParticipatedQuizzesByUserId(userId);
    }

    //Players can participate in the quiz.
    public List<Question> playQuiz(Long quizId, Long userId) {
        // Fetch the quiz
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Check if the quiz is ongoing
        Date currentDate = new Date();
        if (currentDate.before(quiz.getStartDate()) || currentDate.after(quiz.getEndDate())) {
            throw new IllegalStateException("Player can only join ongoing quizzes");
        }

        // Retrieve the same 10 questions assigned to this quiz
        List<Question> questions = quiz.getQuestions();
        if (questions.size() > 10) {
            questions = questions.subList(0, 10);
        }

        // Get questions without correct answers
        return questions.stream()
                .map(question -> new Question(question.getId(), question.getQuestionText(), question.getOptions()))
                .collect(Collectors.toList());
    }

    //Display feedback according to correct and incorrect answers. Also display the score, no of answers correct.
    public Map<String, Object> submitAnswers(Long quizId, Long userId, Map<Long, String> answers) {
        // Fetch user and quiz from the repositories
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Prepare variables to track score and feedback
        int correctAnswersCount = 0;
        StringBuilder feedbackBuilder = new StringBuilder();

        // Iterate through all the questions in the quiz
        for (Question currentQuestion : quiz.getQuestions()) {
            // Get the answer from the provided map (answers) using the question's ID
            String userAnswer = answers.get(currentQuestion.getId());

            if (userAnswer != null) {
                // Validate the player's answer
                boolean isCorrect = currentQuestion.getCorrectAnswer().equalsIgnoreCase(userAnswer);

                // Update correct answer count
                if (isCorrect) {
                    correctAnswersCount++;
                }

                // Generate feedback for the current question
                if (isCorrect) {
                    feedbackBuilder.append("Question ").append(currentQuestion.getId())
                            .append(": Correct! Well done. ");
                } else {
                    feedbackBuilder.append("Question ").append(currentQuestion.getId())
                            .append(": Incorrect. The correct answer is: ")
                            .append(currentQuestion.getCorrectAnswer()).append(". ");
                }
            } else {
                // If the user did not answer the question, provide feedback
                feedbackBuilder.append("Question ").append(currentQuestion.getId())
                        .append(": No answer provided. ");
            }
        }

        // Calculate the total score out of 10
        int totalQuestions = quiz.getQuestions().size();
        double score = ((double) correctAnswersCount / totalQuestions) * 10;

        // Store the score (you can store this in a database, here it's in memory)
        Map<Long, Double> scoreStorage = new HashMap<>();
        scoreStorage.put(userId, score);  // Store score for the user (this could be in a database)

        // Return response with total score out of 10 and feedback
        Map<String, Object> response = new HashMap<>();
        response.put("correctAnswers", correctAnswersCount);
        response.put("score", score);  // Returning score out of 10
        response.put("feedback", feedbackBuilder.toString());

        return response;
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
    public Map<String, Object> replayQuiz(Long quizId, Long userId, Map<Long, String> playerAnswers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Ensure the quiz is ongoing
        Date currentDate = new Date();
        if (currentDate.before(quiz.getStartDate()) || currentDate.after(quiz.getEndDate())) {
            throw new IllegalStateException("Quiz is no longer active");
        }

        // Validate player answers
        if (playerAnswers == null || playerAnswers.isEmpty()) {
            throw new IllegalArgumentException("Player answers are missing");
        }

        for (Question question : quiz.getQuestions()) {
            if (!playerAnswers.containsKey(question.getId())) {
                throw new IllegalArgumentException("Answer missing for question ID: " + question.getId());
            }
        }

        // Calculate score and feedback
        int correctAnswersCount = 0;
        StringBuilder feedbackBuilder = new StringBuilder();

        for (Question question : quiz.getQuestions()) {
            String userAnswer = playerAnswers.get(question.getId());
            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(userAnswer);
            if (isCorrect) {
                correctAnswersCount++;
                feedbackBuilder.append("Question ").append(question.getId()).append(": Correct! ");
            } else {
                feedbackBuilder.append("Question ").append(question.getId())
                        .append(": Incorrect. Correct answer: ").append(question.getCorrectAnswer()).append(". ");
            }
        }

        // Calculate score
        double score = ((double) correctAnswersCount / quiz.getQuestions().size()) * 10;

        // Log replay action (can be removed after debugging)
        System.out.println("Player " + userId + " replayed Quiz " + quizId + " with score: " + score);

        // Return score and feedback
        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        response.put("feedback", feedbackBuilder.toString());

        return response;
    }




    //Add a rating
    public void addRating(Long quizId, int rating) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Update the rating
        quiz.setRating(rating);

        // Save the updated quiz
        quizRepository.save(quiz);
    }




}
