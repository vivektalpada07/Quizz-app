package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.QuestionRepository;
import cs.quizzapp.prokect.backend.db.QuizRepository;
import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.models.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }

    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isPresent()) {
            Quiz existingQuiz = quizOptional.get();
            existingQuiz.setName(updatedQuiz.getName());
            existingQuiz.setCategory(updatedQuiz.getCategory());
            existingQuiz.setDifficulty(updatedQuiz.getDifficulty());
            existingQuiz.setStartDate(updatedQuiz.getStartDate());
            existingQuiz.setEndDate(updatedQuiz.getEndDate());
            return quizRepository.save(existingQuiz);
        }
        return null;
    }

    public boolean deleteQuiz(Long id) {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
            return true;
        }
        return false;
    }
    // Get questions by quiz ID
    public List<Question> getQuestionsByQuizId(Long quizId) {
        Quiz quiz = getQuizById(quizId);
        return quiz != null ? quiz.getQuestions() : null;
    }
}
