package com.example.javatrainer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;
import com.example.javatrainer.service.QuestionService;

public class QuestionController {
	
    private final QuestionRepository questionRepository;
	
	private final QuestionService questionService;


    
    public QuestionController(QuestionService questionService,QuestionRepository questionRepository) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
    }
    

 

    @GetMapping("/practice")
    public String quiz(Model model) {
  
    	List<Question> randomQuestions = questionService.findRandomQuestions(1);
        model.addAttribute("questions",  randomQuestions);
        return "quest";
    }




    @PostMapping("/save")
    public String saveQuestion(Question question) {
        questionService.save(question);
        return "redirect:/questions";
    }


    
    // === /quest 模擬試験モード ===
    @GetMapping("/quest")
    public String showQuiz(Model model) {
    	List<Question> randomQuestions  = questionService.getRandomQuestions(1);
        model.addAttribute("questions", randomQuestions);
        return "quest-multi";
    }
    
    @PostMapping("/quest")
    public String submitAnswers(@RequestParam Map<String, String> formData, Model model) {
        String questionIdStr = formData.get("questionId");
        String answer = formData.get("answer");
        if (questionIdStr == null || answer == null) {
            model.addAttribute("message", "⚠ 質問または回答がありません。");
            return "quest";
        }

        Long questionId = Long.parseLong(questionIdStr);
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question == null) {
            model.addAttribute("message", "⚠ 問題が見つかりません。");
            return "quest";
        }


        boolean isCorrect = question.getCorrectAnswer().trim().equals(answer.trim());

        model.addAttribute("question", question);
        model.addAttribute("selectedAnswer", answer);
        model.addAttribute("isCorrect", isCorrect);
        model.addAttribute("score", isCorrect ? 1 : 0);
        model.addAttribute("total", 1);

        return "quest-result";
    }

    
    
    
}