package com.example.javatrainer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/quiz")
    public String quiz(Model model) {
    	 cachedQuestions = questionService.findRandomQuestions();
        model.addAttribute("questions",  cachedQuestions);
        return "quiz";
    }
    @PostMapping("/quiz")
    public String checkAnswers(@RequestParam Map<String, String> params, Model model) {
        int score = 0;
        Map<Long, String> userAnswers = new HashMap<>();
        Map<Long, String> correctAnswers = new HashMap<>();
        Map<Long, String> explanations = new HashMap<>();

        for (Question q : cachedQuestions) {
            String userAnswer = params.get("answer_" + q.getId());
            if (userAnswer != null && userAnswer.equals(q.getCorrectAnswer())) {
                score++;
            }
            userAnswers.put(q.getId(), userAnswer);
            correctAnswers.put(q.getId(), q.getCorrectAnswer());
            explanations.put(q.getId(), q.getExplanation());
        }

        model.addAttribute("questions", cachedQuestions);
        model.addAttribute("userAnswers", userAnswers);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("explanations", explanations);
        model.addAttribute("score", score);
        model.addAttribute("total", cachedQuestions.size());
        return "quiz-result";
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