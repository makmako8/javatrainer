package com.example.javatrainer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.javatrainer.service.CsvImportService;
import com.example.javatrainer.service.QuestionCsvService;
@Controller
@RequestMapping("/admin/questions")
public class QuestionUploadController {

    private final QuestionCsvService questionCsvService;
    private final CsvImportService csvImportService;

    public QuestionUploadController(QuestionCsvService questionCsvService,CsvImportService csvImportService) {
        this.questionCsvService = questionCsvService;
        this.csvImportService = csvImportService;
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "admin/question-upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            int count = questionCsvService.importFromCsv(file);
            model.addAttribute("message", count + " 件の問題を追加しました");
              } catch (Exception e) {
            model.addAttribute("message", "エラーが発生しました: " + e.getMessage());
        }
        return "admin/question-upload";
    }
    @PostMapping("/admin/upload-questions")
    public String uploadCsv(@RequestParam("file") MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        try {
            questionCsvService.importFromCsv(file);
            redirectAttributes.addFlashAttribute("successMessage", "CSVのアップロードと登録が成功しました！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
        }
        return "redirect:/admin/question-import";
    }
}