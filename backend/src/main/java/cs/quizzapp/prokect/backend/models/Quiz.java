package cs.quizzapp.prokect.backend.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String difficulty;
    private Date startDate;
    private Date endDate;

    private int likesCount = 0; // Tracks likes count
    private Double rating = 0.0; // Average rating
    private Integer ratingCount = 0; // Total number of ratings

    @ElementCollection
    private List<Long> participants = new ArrayList<>(); // Stores player IDs

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions;

    // Default constructor
    public Quiz() {
    }

    // Constructor with fields
    public Quiz(Long id, String name, String category, String difficulty, Date startDate, Date endDate, int likesCount, Double rating, Integer ratingCount, List<Question> questions, List<Long> participants) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.likesCount = likesCount;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.questions = questions;
        this.participants = participants;
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }

    public List<Question> getQuestions() {
        if (questions == null) return null;
        return questions.stream()
                .limit(10) // Limit to 10 questions
                .toList();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
