package com.example.javatrainer.api;

import com.example.javatrainer.entity.TaskResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class UserManagementApiClient {

 private final RestTemplate restTemplate;

 public UserManagementApiClient(RestTemplate restTemplate) {
     this.restTemplate = restTemplate;
 }

 // 🔹 タスク一覧を取得（ログインユーザーに対して）
 public List<TaskResponse> fetchUserTasks() {
     String url = "http://localhost:8082/api/tasks"; // usermanagement-app 側のAPI
     ResponseEntity<TaskResponse[]> response = restTemplate.getForEntity(url, TaskResponse[].class);
     return Arrays.asList(response.getBody());
 }

 // 🔹 タスクを登録（ログインユーザーに紐づけ）
 public TaskResponse postNewTask(TaskResponse task) {
     String url = "http://localhost:8082/api/tasks";
     return restTemplate.postForObject(url, task, TaskResponse.class);
 }

 // 🔹 タスクを削除（自分のIDのみに対応）
 public void deleteTask(Long id) {
     String url = "http://localhost:8082/api/tasks/" + id;
     restTemplate.delete(url);
 }
}

