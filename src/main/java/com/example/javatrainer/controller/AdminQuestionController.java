package com.example.javatrainer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.javatrainer.repository.QuestionRepository;

@Controller
@RequestMapping("/admin/question")
public class AdminQuestionController {

    private final QuestionRepository questionRepository;

    
    @Autowired
    public AdminQuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/questions")
    public String showQuestionList(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "admin_question_list"; // ← このテンプレートファイル名
    }
}