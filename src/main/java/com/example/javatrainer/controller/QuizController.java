package com.example.javatrainer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.AnswerLogRepository;
import com.example.javatrainer.repository.QuestionRepository;
import com.example.javatrainer.service.QuestionService;

@Controller
public class QuizController {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final AnswerLogRepository answerLogRepository;
    
    @Autowired
    public QuizController(QuestionService questionService, AnswerLogRepository answerLogRepository, QuestionRepository questionRepository) {
        this.questionService = questionService;
        this.answerLogRepository = answerLogRepository;
        this.questionRepository = questionRepository;
    }


    @GetMapping("/quiz")
    public String showSingleQuiz(@RequestParam(defaultValue = "all") String type,
                                 Model model,
                                 HttpSession session) {

        // 出題タイプの制御
        String lastType = (String) session.getAttribute("lastQuestionType");
        String nextType = "text".equals(lastType) ? "code" : "text";

        List<Question> questions;

        if ("text".equals(type) || "code".equals(type)) {
            questions = questionRepository.findByQuestionType(type);
            session.setAttribute("lastQuestionType", type);
        } else if ("alternate".equals(type)) {
            questions = questionRepository.findByQuestionType(nextType);
            session.setAttribute("lastQuestionType", nextType);
        } else {
            questions = questionRepository.findAll();
        }

        if (questions.isEmpty()) {
            model.addAttribute("message", "⚠️ 該当する問題が見つかりません。");
            return "quiz";
        }

        Collections.shuffle(questions);
        Question question = questions.get(0);  // ここで選ぶ

        if (question.getQuestionText() == null) {
            model.addAttribute("message", "⚠️ 問題文が空です。");
            return "quiz";
        }

        List<Map.Entry<String, String>> shuffledChoices =
            new ArrayList<>(question.getChoiceMap().entrySet());
        Collections.shuffle(shuffledChoices);

        model.addAttribute("question", question);
        model.addAttribute("shuffledChoices", shuffledChoices);
        return "quiz";
    }


    @PostMapping("/quiz/submit")
    public String submitAnswer(@RequestParam Map<String, String> formData, Model model) {
        String questionIdStr = formData.get("questionId");
        String answer = formData.get("answer");

        if (questionIdStr == null || answer == null) {
            model.addAttribute("message", "⚠ 質問または回答がありません。");
            return "quiz";
        }
        Long questionId = Long.parseLong(questionIdStr);
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question == null) {
            model.addAttribute("message", "⚠ 問題が見つかりません。");
            return "quiz";
        }
        boolean isCorrect = question.getCorrectAnswer().equals(answer.trim());
        model.addAttribute("question", question);
        model.addAttribute("selectedAnswer", answer);
        model.addAttribute("isCorrect", isCorrect);
        
        model.addAttribute("score", isCorrect ? 1 : 0);
        model.addAttribute("total", 4);

        return "quiz-result";
    }


    

    @GetMapping("/start")
    public String showRandomQuestion(Model model) {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) return "quiz/no-questions";

        Question random = questions.get(new Random().nextInt(questions.size()));
        model.addAttribute("question", random);
        return "quiz/question";
    }
    
}
