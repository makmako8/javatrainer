package com.example.javatrainer.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.javatrainer.service.QuestionCsvService;
@Controller
@RequestMapping("/admin/questions/test-upload")
public class QuestionTestUploadController {

    @Autowired
    private QuestionCsvService questionCsvService;

    @GetMapping
    public String showTestUploadForm() {
        return "admin/question-test-upload";
    }

    @PostMapping
    public String handleTestUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            int count = questionCsvService.importFromCsv(file);
            model.addAttribute("message", count + " 件の問題を登録しました（テスト用）");
        } catch (Exception e) {
            model.addAttribute("message", "❌ エラーが発生しました: " + e.getMessage());
        }
        return "admin/question-test-upload";
    }
}
