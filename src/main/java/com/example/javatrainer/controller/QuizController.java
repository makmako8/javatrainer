package com.example.javatrainer.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.javatrainer.entity.AnswerLog;
import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.AnswerLogRepository;
import com.example.javatrainer.repository.QuestionRepository;
import com.example.javatrainer.service.QuestionService;

@Controller
public class QuizController {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final AnswerLogRepository answerLogRepository;

    public QuizController(QuestionService questionService, AnswerLogRepository answerLogRepository, QuestionRepository questionRepository) {
        this.questionService = questionService;
        this.answerLogRepository = answerLogRepository;
        this.questionRepository = questionRepository;
    }

    private List<Question> cachedQuestions = new ArrayList<>();
    // === /quest 模擬試験モード ===
    @GetMapping("/quest")
    public String showQuiz(Model model) {
    	List<Question> randomQuestions  = questionService.getRandomQuestions(1);
        model.addAttribute("questions", randomQuestions);
        return "quiz-multi";
    }

    
    @PostMapping("/quest")
    public String submitAnswers(@RequestParam Map<String, String> params, Model model) {
        int score = 0;
        Map<Long, String> userAnswers = new HashMap<>();
        Map<Long, String> correctAnswers = new HashMap<>();
        Map<Long, String> explanations = new HashMap<>();

        for (Question question : cachedQuestions) {
            String answer = params.get("answer_" + question.getId());
            if (answer != null && answer.equals(question.getCorrectAnswer())) {
                score++;
            }
            userAnswers.put(question.getId(), answer);
            correctAnswers.put(question.getId(), question.getCorrectAnswer());
            explanations.put(question.getId(), question.getExplanation());
        }

        model.addAttribute("questions", cachedQuestions);
        model.addAttribute("userAnswers", userAnswers);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("explanations", explanations);
        model.addAttribute("score", score);
        model.addAttribute("total", cachedQuestions.size());

        return "quiz-result";
    }

    
    @GetMapping("/quiz")
    public String showSingleQuiz(Model model) {
        long count = questionRepository.count();
        System.out.println("登録されている問題数：" + count);  
        
        List<Question> all = questionRepository.findAll();
        if (all.isEmpty()) {
            model.addAttribute("message", "問題がありません");
            return "quiz";
        }
        Question question = all.get(new Random().nextInt(all.size()));
        model.addAttribute("question", question);
        return "quiz";
    }

    @PostMapping("/quiz/answer")
    public String checkAnswer(@RequestParam Long questionId,
                              @RequestParam(required = false) List<String> collectAnswers,
                              @RequestParam String userAnswer,
                              Model model) {
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question == null) return "redirect:/quiz";

        // 正解セット
        Set<String> correctSet = Arrays.stream(question.getCorrectAnswers().split(","))
                                       .map(String::trim)
                                       .collect(Collectors.toSet());

        // ユーザーの選択をどちらかから取得
        Set<String> userSet = new HashSet<>();
        if (collectAnswers != null) {
            userSet.addAll(collectAnswers.stream().map(String::trim).toList());
        } else if (userAnswer != null) {
            userSet.add(userAnswer.trim());
        }

        boolean correct = correctSet.equals(userSet);

        // ログ記録（オプション）
        AnswerLog log = new AnswerLog();
        log.setQuestion(question);
        log.setUserAnswer(String.join(",", userSet));
        log.setCorrect(correct);
        log.setAnsweredAt(LocalDateTime.now());
        answerLogRepository.save(log);

        // 表示用
        model.addAttribute("question", question);
        model.addAttribute("collectAnswers", userSet);
        model.addAttribute("correct", correct);

        // ✅ スコア表示用
        model.addAttribute("score", correct ? 1 : 0);
        model.addAttribute("total", 1);

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
