package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.QuizRepository;
import cs.quizzapp.prokect.backend.models.Quiz;
import cs.quizzapp.prokect.backend.payload.QuizRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Quiz createQuizWithQuestions(QuizRequest quizRequest) {
        Quiz quiz = new Quiz();
        quiz.setName(quizRequest.getName());
        quiz.setCategory(quizRequest.getCategory());
        quiz.setDifficulty(quizRequest.getDifficulty());
        quiz.setStartDate(quizRequest.getStartDate());
        quiz.setEndDate(quizRequest.getEndDate());
        return quizRepository.save(quiz);
    }

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

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public boolean deleteQuiz(Long id) {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
