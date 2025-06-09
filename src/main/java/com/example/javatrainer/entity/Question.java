package com.example.javatrainer.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Question {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String level; // Bronze / Silver
    private String category;
    @Column(length = 1000)
    private String questionText;
 
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String correctAnswer;
    @Column(length = 1000)
    private String explanation;
    private boolean favorite; 
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getCategoty() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    public String getCoiceA() {
        return choiceA;
    }

    public void setCoiceA(String choiceA) {
        this.choiceA = choiceA;
    }
    public String getCoiceB() {
        return choiceA;
    }

    public void setCoiceB(String choiceB) {
        this.choiceB = choiceB;
    }
    public String getCoiceC() {
        return choiceC;
    }

    public void setCoiceC(String choiceC) {
        this.choiceC = choiceC;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    public boolean favorite() {
        return favorite;
    }
    public void setEnabled(boolean favorite) {
        this.favorite = favorite;
    }


}
