package com.example.javatrainer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.javatrainer.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM question ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Question> findRandomQuestions();
    Optional<Question> findByQuestionText(String questionText);
    
    boolean existsByQuestionText(String questionText);
    List<Question> findByQuestionType(String questionType);
   

}