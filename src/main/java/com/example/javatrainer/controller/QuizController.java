package com.example.javatrainer.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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
    	String nextType;

    	if ("text".equals(lastType)) {
    	    nextType = "code";
    	} else if ("code".equals(lastType)) {
    	    nextType = "text";
    	} else {
    	    nextType = "text"; // 初回やnullのときのデフォルト
    	}

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
    public String submitAnswer(@RequestParam Long questionId,@RequestParam(value = "answer", required = false) List<String> answers, Model model,  HttpSession session) {

        // スコア管理（セッション）
    	Integer score = (Integer) session.getAttribute("quizScore");
        Integer total = (Integer) session.getAttribute("quizTotal");
        Integer correct = (Integer) session.getAttribute("quizCorrect");
		
		if (score == null) score = 0;
		if (total == null) total = 0;
		if (correct == null) correct = 0;
        // 状態管理
        List<Question> allQuestions = questionRepository.findAll();
        Object solvedObj = session.getAttribute("solvedQuestionIds");
        Set<Long> solvedIds;
        if (solvedObj instanceof Set) {
            solvedIds = (Set<Long>) solvedObj;
        } else {
            solvedIds = new HashSet<>();
        }
        
        Object wrongObj = session.getAttribute("wrongQuestionIds");
        Set<Long> wrongIds;
        if (wrongObj instanceof Set) {
            wrongIds = (Set<Long>) wrongObj;
        } else {
            wrongIds = new HashSet<>();
        }

     


        List<Question> unsolvedQuestions = allQuestions.stream()
            .filter(q -> !solvedIds.contains(q.getId()))
            .collect(Collectors.toList());
        
     // 2. 回答した問題（questionId）の取得とチェック
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question == null || answers == null || answers.isEmpty()) {
            model.addAttribute("message", "⚠ 回答がありません。");
   
            
            return "quiz";
        }
    
  
        // 正答チェック
        boolean isCorrect = false;
        if ("multiple".equals(question.getQuestionType())) {
            // 複数回答問題
            String correctAnswersRaw = question.getCorrectAnswers();
            if (correctAnswersRaw == null || correctAnswersRaw.trim().isEmpty()) {
                model.addAttribute("message", "⚠️ この問題には複数の正解が設定されていません。");
                return "quiz";
            }
            Set<String> correctSet = new HashSet<>(Arrays.asList(correctAnswersRaw.split(",")));
            Set<String> answerSet = new HashSet<>(answers);
            isCorrect = correctSet.equals(answerSet);
     
        } else {
            // 単一回答判定（correctAnswer を使う）
        	String correctAnswer = question.getCorrectAnswer();
        	if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
        		 model.addAttribute("message", "⚠️ この問題には正解が設定されていません。");
        	    return "quiz";
        }
        	isCorrect = correctAnswer.equals(answers.get(0));
        }

     // 4. スコア更新
        if (isCorrect) {
            solvedIds.add(question.getId());
            correct++;
            score++;
            model.addAttribute("isCorrect", true);
        } else {
           if (wrongIds == null) wrongIds = new HashSet<>();
            wrongIds.add(question.getId());
            session.setAttribute("wrongQuestionIds", wrongIds);
            model.addAttribute("isCorrect", false);
        }
        total++;

     // 5. セッション更新
        session.setAttribute("quizScore", score);
        session.setAttribute("quizTotal", total);
        session.setAttribute("quizCorrect", correct);
        session.setAttribute("solvedQuestionIds", solvedIds);
        
     // 6. 次回の出題準備（あとで /quiz にリダイレクトして使う）
        if (!unsolvedQuestions.isEmpty()) {
            unsolvedQuestions.removeIf(q -> q.getId().equals(question.getId()));
            if (!unsolvedQuestions.isEmpty()) {
                Collections.shuffle(unsolvedQuestions);
                Question nextQuestion = unsolvedQuestions.get(0);
                session.setAttribute("nextQuestion", nextQuestion);
            }else if (unsolvedQuestions.isEmpty()) {
                model.addAttribute("message", "🎉 全問解答済みです！");
        }
        }
     // 7. 採点結果画面表示（ここでは「今回の出題」を表示）
        model.addAttribute("question", question);
        model.addAttribute("selectedAnswer", String.join(",", answers));
        model.addAttribute("score", score);
        model.addAttribute("total", total);

        return "quiz-result";

        }


    @GetMapping("/quiz/reset")
    public String resetQuizProgress(HttpSession session) {
        session.removeAttribute("quizTotal");
        session.removeAttribute("quizCorrect");
        return "redirect:/quiz";
    }

    

    @GetMapping("/start")
    public String showRandomQuestion(Model model) {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) return "quiz/no-questions";

        Question random = questions.get(new Random().nextInt(questions.size()));
        model.addAttribute("question", random);
        return "quiz/question";
    }
    @GetMapping("/quiz/review")
    public String reviewWrongQuestion(Model model, HttpSession session) {
        Set<Long> wrongIds = (Set<Long>) session.getAttribute("wrongQuestionIds");
        if (wrongIds == null || wrongIds.isEmpty()) {
            model.addAttribute("message", "🎉 間違えた問題はありません！");
            return "quiz";
        }

        List<Question> wrongQuestions = questionRepository.findAllById(wrongIds);
        Collections.shuffle(wrongQuestions);
        Question question = wrongQuestions.get(0);

        List<Map.Entry<String, String>> shuffledChoices =
            new ArrayList<>(question.getChoiceMap().entrySet());
        Collections.shuffle(shuffledChoices);

        model.addAttribute("question", question);
        model.addAttribute("shuffledChoices", shuffledChoices);
        model.addAttribute("reviewMode", true);  // 復習モード表示用フラグ
        return "quiz";
    }
    @PostMapping("/quiz/review/submit")
    public String submitReview(@RequestParam Map<String, String> formData,
                               Model model, HttpSession session) {
        Long questionId = Long.parseLong(formData.get("questionId"));
        String answer = formData.get("answer");

        Question question = questionRepository.findById(questionId).orElse(null);
        if (question == null) {
            model.addAttribute("message", "⚠ 問題が見つかりません。");
            return "quiz";
        }

        boolean isCorrect = question.getCorrectAnswer().equals(answer.trim());
        Set<Long> wrongIds = (Set<Long>) session.getAttribute("wrongQuestionIds");
        if (isCorrect && wrongIds != null) {
            wrongIds.remove(questionId);
            session.setAttribute("wrongQuestionIds", wrongIds);
        }

        model.addAttribute("question", question);
        model.addAttribute("isCorrect", isCorrect);
        model.addAttribute("selectedAnswer", answer);
        return "quiz-result";
    }


}
