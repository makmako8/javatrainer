package com.example.javatrainer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;

@Service
public class QuestionCsvService {

    @Autowired
    private QuestionRepository questionRepository;

 // QuestionCsvService.java の中

    public int importFromCsv(MultipartFile file) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; } // ヘッダー行スキップ

                String[] tokens = line.split(",", -1);
                if (tokens.length < 6) continue;

                String questionText = tokens[0];
                if (questionRepository.existsByQuestionText(questionText)) {
                    continue; // 重複をスキップ
                }

                Question q = new Question();
                q.setQuestionText(questionText);
                q.setChoice1(tokens[1]);
                q.setChoice2(tokens[2]);
                q.setChoice3(tokens[3]);
                q.setChoice4(tokens[4]);
                q.setCorrectAnswer(tokens[5]);
                q.setExplanation(tokens.length > 6 ? tokens[6] : null);
                q.setQuestionType(tokens.length > 7 ? tokens[7] : "text");

                questionRepository.save(q);
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException("CSVの読み取りに失敗しました", e);
        }
        return count;
    }

}
