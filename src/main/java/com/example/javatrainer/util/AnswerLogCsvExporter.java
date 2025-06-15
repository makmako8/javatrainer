package com.example.javatrainer.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import com.example.javatrainer.entity.AnswerLog;

public class AnswerLogCsvExporter {

    public static void export(List<AnswerLog> logs, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=answerlog.csv");

        PrintWriter writer = response.getWriter();

        // ヘッダー
        writer.println("question_id,question_text,user_answer,correct,answered_at");

        // 本文
        for (AnswerLog log : logs) {
            writer.printf("%d,\"%s\",%s,%b,%s%n",
                log.getQuestion().getId(),
                log.getQuestion().getQuestionText().replace("\"", "\"\""),
                log.getUserAnswer(),
                log.isCorrect(),
                log.getAnsweredAt()
            );
        }

        writer.flush();
        writer.close();
    }
}