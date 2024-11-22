package cs.quizzapp.prokect.backend.dto;

import java.util.Date;
import java.util.List;

public class QuizDTO {
    private Long id;
    private String name;
    private String category;
    private String difficulty;
    private Date startDate;
    private Date endDate;
    private List<QuestionDTO> questions;

    // Constructors
    public QuizDTO() {}

    public QuizDTO(Long id, String name, String category, String difficulty, Date startDate, Date endDate, List<QuestionDTO> questions) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.questions = questions;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    // ...
    // (Include getters and setters for all fields)
}
