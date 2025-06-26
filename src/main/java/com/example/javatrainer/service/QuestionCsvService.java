package com.example.javatrainer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
                if (isFirstLine) { isFirstLine = false; continue; }

                String[] tokens = line.split(",", -1);
                if (tokens.length < 16) {
                    System.out.println("⚠️ 列数不足：" + Arrays.toString(tokens));
                    continue;
                }
                String questionText = tokens[0];
                if (questionRepository.existsByQuestionText(questionText)) {
                    continue; // 重複をスキップ
                }

                Question q = new Question();
                q.setQuestionText(tokens[0]);
                q.setBookType(tokens[1]);
                q.setChapter(tokens[2]);
                q.setNumber(tokens[3]);
                q.setChoice1(tokens[4]);
                q.setChoice2(tokens[5]);
                q.setChoice3(tokens[6]);
                q.setChoice4(tokens[7]);
                q.setChoice5(tokens[8]);
                q.setChoice6(tokens[9]);
                q.setCorrectAnswer(tokens[10]);
                q.setCorrectAnswers(tokens[11]);
                q.setQuestionType(tokens[12]);
                q.setExplanation(tokens[13]);
                q.setDifficulty(tokens[14]);
                q.setSource(tokens[15]); // ← 追加

                questionRepository.save(q);
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException("CSVの読み取りに失敗しました", e);
        }
        return count;
    }

}
