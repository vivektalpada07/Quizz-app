package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.db.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // Get all questions
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Get question by ID
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    // Get questions by Quiz ID
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    // Add a new question
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Update an existing question by ID
    public Optional<Question> updateQuestion(Long id, Question updatedQuestion) {
        return questionRepository.findById(id).map(existingQuestion -> {
            existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
            existingQuestion.setOptions(updatedQuestion.getOptions());
            existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            return questionRepository.save(existingQuestion);
        });
    }

    // Delete a question by ID
    public boolean deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
