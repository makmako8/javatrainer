package com.example.javatrainer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.service.QuestionService;
@Controller
public class QuestionController {
	
	private final QuestionService questionService;
    private List<Question> cachedQuestions = new ArrayList<>();

    
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    

    @GetMapping("/questions")
    public String listQuestions(Model model) {
    	 model.addAttribute("questions", questionService.getAllQuestions());
        return "question-list";
    }

    @GetMapping("/practice")
    public String quiz(Model model) {
    	List<Question> randomQuestions = questionService.findRandomQuestions(1);
        model.addAttribute("questions",  cachedQuestions);
        return "quiz";
    }


    @GetMapping("/admin/questions/new")
    public String showForm(Model model) {
        model.addAttribute("question", new Question());
        return "admin/question-form";
    }

    @PostMapping("/admin/questions/save")
    public String saveQuestion(Question question) {
        questionService.save(question);
        return "redirect:/questions";
    }
    
    
}