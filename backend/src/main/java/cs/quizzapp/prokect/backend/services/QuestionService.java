package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.QuestionRepository;
import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.models.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Optional<Question> updateQuestion(Long id, Question updatedQuestion) {
        return questionRepository.findById(id).map(existingQuestion -> {
            existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
            existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            existingQuestion.setOptions(updatedQuestion.getOptions());
            return questionRepository.save(existingQuestion);
        });
    }

    public boolean deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
