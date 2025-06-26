package com.example.javatrainer.service;

public class QuestionParser {

    public static class ParsedInfo {
        public String bookType;     // 例: bb, bs, bg
        public String difficulty;   // bronze, silver, gold
        public int chapter;
        public int number;
        public String pureQuestionText;
    }

    public ParsedInfo parseQuestionText(String rawText) {
        ParsedInfo info = new ParsedInfo();

        try {
            // 例: "bb1_1. Javaの予約語はどれ？"
            String prefix = rawText.split("\\.")[0]; // "bb1_1"
            String[] parts = prefix.split("_");       // ["bb1", "1"]
            String typeAndChapter = parts[0];         // "bb1"

            info.bookType = typeAndChapter.substring(0, 1); // "bb"
            info.chapter = Integer.parseInt(typeAndChapter.substring(2));
            info.number = Integer.parseInt(parts[3]);

            // 難易度を識別（例: bb → bronze, bs → silver）
            switch (info.bookType) {
                case "bb": info.difficulty = "bronze"; break;
                case "bs": info.difficulty = "silver"; break;
                case "bg": info.difficulty = "gold"; break;
                default: info.difficulty = "unknown";
            }

            // 質問文本体
            info.pureQuestionText = rawText.substring(rawText.indexOf('.') + 1).trim();

        } catch (Exception e) {
            System.out.println("パース失敗: " + rawText);
            info.pureQuestionText = rawText;  // fallback
        }

        return info;
    }
}
