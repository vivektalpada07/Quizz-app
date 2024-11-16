package cs.quizzapp.prokect.backend.services;

import cs.quizzapp.prokect.backend.models.Question;
import cs.quizzapp.prokect.backend.db.QuestionRepository;
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

    private static final String API_URL = "https://opentdb.com/api.php?amount=10&category=17&difficulty=easy&type=multiple";

    public List<Question> fetchAndSaveQuestions() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);

        List<Question> questions = new ArrayList<>();
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            for (Map<String, Object> result : results) {
                Question question = new Question();
                question.setQuestionText((String) result.get("question"));
                question.setCorrectAnswer((String) result.get("correct_answer"));

                // Initialize options with incorrect answers and add the correct answer
                List<String> options = new ArrayList<>((List<String>) result.get("incorrect_answers"));
                options.add(question.getCorrectAnswer()); // Add the correct answer to options
                Collections.shuffle(options); // Shuffle the options

                question.setOptions(options); // Set the shuffled options

                // Save the question to the database
                questions.add(questionRepository.save(question));
            }
        }
        return questions;
    }
}
