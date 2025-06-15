CREATE TABLE IF NOT EXISTS question (
    id INT PRIMARY KEY,
    question_text TEXT,
    choice1 TEXT,
    choice2 TEXT,
    choice3 TEXT,
    choice4 TEXT,
    choice5 TEXT,
 　 choice6 TEXT,
    correct_answers TEXT,
　　explanation TEXT
);


INSERT INTO question (id, question_text, choice1, choice2, choice3, choice4, choice5, choice6, correct_answers,  explanation) VALUES
(5, 'Javaのクラスファイルに関する説明として、正しいものを選びなさい。(１つ選択)', 'プラットフォームに依存したネイティブコードが記述されている', 'プラットフォームに依存しないネイティブコードが記述されている', 'JVMだけが理解できるコードが記述されている', '人間が理解できるコードが記述されている', NULL, NULL,'JVMだけが理解できるコードが記述されている', NULL);