package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.db.QuestionRepository;
import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.models.Quiz;
import cs.quizzapp.prokect.backend.payload.QuizRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class TriviaService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> fetchAndSaveQuestions(QuizRequest quizRequest, Quiz quiz) {
        // Build the dynamic URL
        String apiUrl = "https://opentdb.com/api.php?amount=" + quizRequest.getAmount() +
                "&category=" + quizRequest.getCategory() +
                "&difficulty=" + quizRequest.getDifficulty() +
                "&type=multiple";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

        List<Question> questions = new ArrayList<>();
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            for (Map<String, Object> result : results) {
                Question question = new Question();
                question.setQuestionText((String) result.get("question"));
                question.setCorrectAnswer((String) result.get("correct_answer"));

                // Combine correct and incorrect answers into options
                List<String> options = new ArrayList<>((List<String>) result.get("incorrect_answers"));
                options.add(question.getCorrectAnswer());
                Collections.shuffle(options);

                question.setOptions(options);
                question.setQuiz(quiz);

                // Save question to database
                questions.add(questionRepository.save(question));
            }
        }

        return questions;
    }
}
