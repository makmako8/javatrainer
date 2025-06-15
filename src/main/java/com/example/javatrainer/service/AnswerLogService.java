package com.example.javatrainer.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.javatrainer.entity.AnswerLog;
import com.example.javatrainer.repository.AnswerLogRepository;

@Service
public class AnswerLogService {

    private final AnswerLogRepository answerLogRepository;

    public AnswerLogService(AnswerLogRepository answerLogRepository) {
        this.answerLogRepository = answerLogRepository;
    }

    public long getTotalCount() {
        return answerLogRepository.count();
    }

    public long getCorrectCount() {
        return answerLogRepository.countByCorrectTrue();
    }

    public long getIncorrectCount() {
        return answerLogRepository.countByCorrectFalse();
    }

    public double getAccuracyRate() {
        long total = getTotalCount();
        if (total == 0) return 0;
        return (getCorrectCount() * 100.0) / total;
    }
    public List<AnswerLog> getAllLogs() {
        return answerLogRepository.findAll();
    }
}
