package com.example.javatrainer.service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }



    public List<Question> findRandomQuestions(int count) {
        List<Question> all = questionRepository.findAll();
        Collections.shuffle(all);
        return all.size() <= count ? all : all.subList(0, count);
    }
    public List<Question> getRandomQuestions(int count) {
        // 仮で10問取得（必要に応じて拡張）
        return questionRepository.findRandomQuestions();
    }
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    public void save(Question question) {
        questionRepository.save(question);
    }

}
