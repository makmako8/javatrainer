package com.example.javatrainer.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.javatrainer.entity.AnswerLog;

public interface AnswerLogRepository extends JpaRepository<AnswerLog, Long> {
    long count();                      // 全回答数
    long countByCorrectTrue();        // 正解数
    long countByCorrectFalse();       // 不正解数
    List<AnswerLog> findByCorrectFalseOrderByAnsweredAtDesc();
}
