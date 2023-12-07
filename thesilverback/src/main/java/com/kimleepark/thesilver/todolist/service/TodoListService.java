package com.kimleepark.thesilver.todolist.service;

import com.kimleepark.thesilver.todolist.domain.TodoList;
import com.kimleepark.thesilver.todolist.domain.repository.TodoListRepository;
import com.kimleepark.thesilver.todolist.dto.response.ResponseTodoList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TodoListService {

    private final TodoListRepository todoListRepository;

    private Pageable getPageable(int page) {
        return PageRequest.of(page-1,4);
    }
    public Page<ResponseTodoList> getTodoLists(int page, int empNo) {

        Page<TodoList> todoLists = todoListRepository.findByEmployeeCode(getPageable(page),empNo);

        log.info("todolist : {}",todoLists.getContent());

        return todoLists.map(todolist -> ResponseTodoList.from(todolist));
    }

    public void putTodoList(String content, Long todoNo) {
        TodoList todoList = todoListRepository.findById(todoNo).orElseThrow(()-> new IllegalArgumentException());
        todoList.updateContent(content);
    }

    public void postTodoList(int empNo, String content) {
        TodoList  newTodolist = TodoList.of(empNo,content);
        todoListRepository.save(newTodolist);
    }

    public void deleteTodo(Long todoNo) {
        todoListRepository.deleteById(todoNo);
    }
}
