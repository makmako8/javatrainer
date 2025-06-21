package com.example.javatrainer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.example.javatrainer.entity.Question;
import com.example.javatrainer.repository.QuestionRepository;

@SpringBootApplication
@EntityScan("com.example.javatrainer.entity")
public class JavatrainerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavatrainerApplication.class, args);

	}
	
	@Bean
	public CommandLineRunner testQuestionSeeder(QuestionRepository repo) {
	    return args -> {
	        // 「特定の問題が存在しなければ追加」する
	        boolean alreadyExists = repo.existsByQuestionText("Javaの予約語はどれ？");
	        if (!alreadyExists) {
	            Question q = new Question();
	            q.setQuestionText("Javaの予約語はどれ？");
	            q.setChoice1("class");
	            q.setChoice2("define");
	            q.setChoice3("function");
	            q.setChoice4("print");
	            q.setCorrectAnswer("1");
	            q.setQuestionType("text");
	            repo.save(q);
	            System.out.println("✅ テスト問題を追加しました");
	        }
	    };
	}
	
//	CommandLineRunner cleanAndSeed2506192101に保存
//	@Profile("dev")
	//@Bean
//	public CommandLineRunner cleanAndSeed(QuestionRepository repo) {
//		return args -> {
//    		 repo.deleteAll(); 
//            Question q = new Question();
//            q.setQuestionText("Javaで正しい予約語は？");
//            q.setChoice1("class");
//            q.setChoice2("define");
//            q.setChoice3("function");
//            q.setChoice4("print");
//            q.setCorrectAnswer("1");
//            repo.save(q);
//            System.out.println("✅ テスト問題1を追加しました！");
//            
//            Question q2 = new Question();
//            q2.setQuestionText("Javaのプリミティブ型に含まれるものは？");
//            q2.setChoice1("int");
//            q2.setChoice2("String");
//            q2.setChoice3("Date");
//            q2.setChoice4("Integer");
//            q2.setCorrectAnswer("1");
//            repo.save(q2);
//            System.out.println("✅ テスト問題2を追加しました！");
//            
//            Question q3 = new Question();
//            q3.setQuestionText(
//       		    "public class Main {\n" +
//       		    "    public static void main(String[] args) {\n" +
//       		    "        System.out.println(5/2);\n" +
//       		    "    }\n" +
//       		    "}"
//       		);
//
//            q2.setChoice1("2.5が表示される");
//            q2.setChoice2("2が表示される");
//            q2.setChoice3("コンパイルエラーが発生する");
//            q2.setChoice4("実行時に例外がスローされる");
//            q2.setCorrectAnswer("2");
//            repo.save(q3);
//            System.out.println("✅ テスト問題3を追加しました！");
//        
//  
//  };
//}

}
