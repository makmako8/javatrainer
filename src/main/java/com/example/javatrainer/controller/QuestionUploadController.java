package com.example.javatrainer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.javatrainer.service.CsvImportService;
import com.example.javatrainer.service.QuestionCsvService;

@Controller
public class QuestionUploadController {

    private final QuestionCsvService questionCsvService;
    private final CsvImportService csvImportService;

    public QuestionUploadController(QuestionCsvService questionCsvService,CsvImportService csvImportService) {
        this.questionCsvService = questionCsvService;
        this.csvImportService = csvImportService;
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
    @PostMapping("/admin/upload-questions")
    public String uploadCsv(@RequestParam("file") MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        try {
            csvImportService.importFromCsv(file);
            redirectAttributes.addFlashAttribute("successMessage", "問題をインポートしました！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "インポートに失敗しました: " + e.getMessage());
        }
        return "redirect:/admin/question-import";
    }
}