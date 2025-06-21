package com.example.javatrainer.entity;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "question") 
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", unique = true)
    private String questionText;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String choice5;
    private String choice6;
    private String explanation;
  
    
    @Column(name = "question_type")
    private String questionType;
    @Column(name = "correct_answer")
    private String correctAnswer;  
    private String difficulty;// 例："A", "B", "C", "D"
    @Column(name = "correct_answers")
    private String correctAnswers;
  
    public String getExplanation() {
        return explanation;
    }
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    // getter & setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getCorrectAnswes() { return correctAnswers; }
    public void setCorrectAnswers(String correctAnswers) { this.correctAnswers = correctAnswers; }
    public String getCorrectAnswers() {
        return correctAnswers;
    }
    public String getChoice1() { return choice1; }
    public void setChoice1(String choice1) { this.choice1 = choice1; }

    public String getChoice2() { return choice2; }
    public void setChoice2(String choice2) { this.choice2 = choice2; }

    public String getChoice3() { return choice3; }
    public void setChoice3(String choice3) { this.choice3 = choice3; }

    public String getChoice4() { return choice4; }
    public void setChoice4(String choice4) { this.choice4 = choice4; }
    
    public String getChoice5() { return choice5; }
    public void setChoice5(String choic5) { this.choice5 = choice5; }

    public String getChoice6() { return choice6; }
    public void setChoice6(String choice6) { this.choice6 = choice6; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
    @Transient
    public Map<String, String> getChoiceMap() {
       Map<String, String> map = new LinkedHashMap<>();
        if (choice1 != null) map.put("1", choice1);
        if (choice2 != null) map.put("2", choice2);
        if (choice3 != null) map.put("3", choice3);
        if (choice4 != null) map.put("4", choice4);
        if (choice5 != null) map.put("5", choice5);
        if (choice6 != null) map.put("6", choice6);
        return map;
      }

}