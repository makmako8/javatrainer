package com.example.javatrainer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.javatrainer.service.QuestionCsvService;

@Controller
public class QuestionUploadController {

    private final QuestionCsvService questionCsvService;

    public QuestionUploadController(QuestionCsvService questionCsvService) {
        this.questionCsvService = questionCsvService;
    }

    @GetMapping("/admin/questions/upload")
    public String showUploadForm() {
        return "admin/question-upload";
    }

    @PostMapping("/admin/questions/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        int count = questionCsvService.importFromCsv(file);
        model.addAttribute("message", count + " 件の問題を追加しました");
        return "admin/question-upload";
    }
}