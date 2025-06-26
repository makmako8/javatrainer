package com.example.javatrainer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;

@Controller
@RequestMapping("/admin/question")
public class AdminQuestionController {

    private final QuestionRepository questionRepository;

    
    @Autowired
    public AdminQuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @GetMapping("/list")
    public String showQuestionList(Model model) {
        List<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        return "admin/question-list";
    }
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("question", new Question());
        return "admin/question-form";
    }
    
    @GetMapping("/edit/{id}")
    public String editQuestion(@PathVariable Long id, Model model) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("無効なID: " + id));
        model.addAttribute("question", question);
        return "admin/question-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        questionRepository.deleteById(id);
        return "redirect:/admin/question/list";
    }

    @PostMapping("/save")
    public String saveQuestion(@ModelAttribute Question question) {
        questionRepository.save(question);
   
        return "redirect:/admin/question/list";
    }

}