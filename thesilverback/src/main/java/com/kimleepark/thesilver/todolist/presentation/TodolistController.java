package com.kimleepark.thesilver.todolist.presentation;


import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.todolist.dto.response.ResponseTodoList;
import com.kimleepark.thesilver.todolist.service.TodoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class TodolistController {
    private final TodoListService todoListService;
    @GetMapping("/todoList")
    public ResponseEntity<PagingResponse> getTodoLists(Integer page){
        int empNo = 1;
        Page<ResponseTodoList> pageTodoList = todoListService.getTodoLists(page,empNo);
        PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(pageTodoList);

        PagingResponse pagingResponse = PagingResponse.of(pageTodoList,pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    @PutMapping("/todoList/{todoNo}")
    public ResponseEntity<Void> putTodoList(@RequestParam String content, @PathVariable int todoNo){
        todoListService.putTodoList(content, (long) todoNo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/todoList")
    public ResponseEntity<Void> postTodoList(String content){
        int empNo = 1;
        todoListService.postTodoList(empNo,content);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/todoList/{todoNo}")
    public ResponseEntity<Void> deleteTodolist(@PathVariable Long todoNo){

todoListService.deleteTodo(todoNo);

        return ResponseEntity.noContent().build();
    }

}
