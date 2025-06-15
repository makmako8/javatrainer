package com.example.javatrainer.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;

@Service
public class QuestionCsvService {

    private final QuestionRepository questionRepository;

    public QuestionCsvService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public int importFromCsv(MultipartFile file) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }
                String[] tokens = line.split(",", -1);
                if (tokens.length < 9) continue;

                Question q = new Question();
//                q.setLevel(tokens[0]);
//                q.setCategory(tokens[1]);
//                q.setPurposeId(tokens[2]);
//                q.setQuestionText(tokens[3]);
//                q.setChoiceA(tokens[4]);
//                q.setChoiceB(tokens[5]);
//                q.setChoiceC(tokens[6]);
//                q.setCorrectAnswer(tokens[7]);
//                q.setExplanation(tokens[8]);

                questionRepository.save(q);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
