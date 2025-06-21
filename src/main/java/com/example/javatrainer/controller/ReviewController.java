package com.example.javatrainer.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.javatrainer.entity.AnswerLog;
import com.example.javatrainer.repository.AnswerLogRepository;
@Controller
public class ReviewController {
    private final AnswerLogRepository answerLogRepository;

    public ReviewController(AnswerLogRepository answerLogRepository) {
        this.answerLogRepository = answerLogRepository;
    }


    @GetMapping("/review")
    public String showReviewPage(Model model) {
        // 任意で直近の回答履歴を表示するなど
    	 List<AnswerLog> wrongLogs = answerLogRepository.findByCorrectFalseOrderByAnsweredAtDesc();
        model.addAttribute("message", "ここに復習の問題が表示されます！");
        return "review"; // → templates/review.html を返す
    }
}
