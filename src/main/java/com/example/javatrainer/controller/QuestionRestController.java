package com.example.javatrainer.controller;

import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;

@RestController
@RequestMapping("/api/quiz")
public class QuestionRestController {

    private final QuestionRepository questionRepository;
    private final Random random = new Random();

    public QuestionRestController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // ✅ ランダムで1問出題
    @GetMapping("/random")
    public Question getRandomQuestion() {
        List<Question> all = questionRepository.findAll();
        if (all.isEmpty()) {
            throw new IllegalStateException("問題が登録されていません。");
        }
        return all.get(random.nextInt(all.size()));
    }
}
