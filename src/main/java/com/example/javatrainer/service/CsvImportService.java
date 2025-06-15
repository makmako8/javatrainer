package com.example.javatrainer.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;

@Service
public class CsvImportService {

 private final QuestionRepository questionRepository;

 public CsvImportService(QuestionRepository questionRepository) {
     this.questionRepository = questionRepository;
 }

 public void importFromCsv(MultipartFile file) throws Exception {
     List<Question> questions = new ArrayList<>();

     try (BufferedReader reader = new BufferedReader(
             new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

         String line;
         boolean isFirst = true;
         while ((line = reader.readLine()) != null) {
             if (isFirst) { isFirst = false; continue; } // ヘッダーをスキップ
             String[] tokens = line.split(",", -1); // 空白列も含めて分割
             if (tokens.length < 9) continue; // 最低限の列数がない行はスキップ

             Question q = new Question();
             q.setQuestionText(tokens[1]);
             q.setChoice1(tokens[2]);
             q.setChoice2(tokens[3]);
             q.setChoice3(tokens[4]);
             q.setChoice4(tokens[5]);
             q.setChoice5(tokens[6]);
             q.setCorrectAnswer(tokens[7]);
             q.setCorrectAnswers(tokens[7]);
             q.setExplanation(tokens[8]);
             questions.add(q);
         }
     }

     questionRepository.saveAll(questions);
 }
}
