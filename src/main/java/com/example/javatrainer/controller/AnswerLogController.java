package com.example.javatrainer.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.javatrainer.entity.AnswerLog;
import com.example.javatrainer.service.AnswerLogService;
import com.example.javatrainer.util.AnswerLogCsvExporter;

@Controller
public class AnswerLogController {

    private final AnswerLogService answerLogService;

    public AnswerLogController(AnswerLogService answerLogService) {
        this.answerLogService = answerLogService;
    }

    @GetMapping("/summary")
    public String showSummary(Model model) {
        model.addAttribute("total", answerLogService.getTotalCount());
        model.addAttribute("correct", answerLogService.getCorrectCount());
        model.addAttribute("incorrect", answerLogService.getIncorrectCount());
        model.addAttribute("accuracyRate", answerLogService.getAccuracyRate());
        return "summary";
    }
    @GetMapping("/download")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        List<AnswerLog> logs = answerLogService.getAllLogs(); // 全ログを取得
        AnswerLogCsvExporter.export(logs, response);
    }
}
