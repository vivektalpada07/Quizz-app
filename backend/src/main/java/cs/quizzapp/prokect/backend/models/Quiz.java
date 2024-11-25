package cs.quizzapp.prokect.backend.models;

import jakarta.persistence.*;

import java.util.*;

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
    private int rating; // Average rating

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Default constructor
    public Quiz() {
    }

    // Constructor with fields


    public Quiz(Long id, String name, String category, String difficulty, Date startDate, Date endDate, int likesCount, int rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.likesCount = likesCount;
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
