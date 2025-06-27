package com.example.javatrainer.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


@Service
public class QuestionCsvService {

    private final QuestionRepository questionRepository;

    public QuestionCsvService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    private String clean(String s) {
        return (s == null) ? null : s.trim().replaceAll("^\"|\"$", "");
    }

    public int importFromCsv(MultipartFile file) {
        int count = 0;
        List<Question> questions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String[] tokens;
            boolean isFirst = true;
    

            while ((tokens = reader.readNext()) != null) {
                if (isFirst) { isFirst = false; continue; }
                // 処理内容…
                String questionText = clean(tokens[0]);
                if (questionRepository.existsByQuestionText(questionText)) {
                    continue;
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
                    q.setSource(tokens[15]);

                    questions.add(q);
                    count++;
                }
            }
        catch (IOException | CsvValidationException e) {
            throw new RuntimeException("CSV読み取りエラー", e);
            


                
        }

        questionRepository.saveAll(questions);
        return count;

        } 

        

       



    
}

