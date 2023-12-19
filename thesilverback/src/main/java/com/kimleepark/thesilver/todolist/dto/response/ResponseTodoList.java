package com.kimleepark.thesilver.todolist.dto.response;

import com.kimleepark.thesilver.todolist.domain.TodoList;
import com.kimleepark.thesilver.todolist.domain.type.CompleteType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@ToString
@Getter
public class ResponseTodoList {

    private final Long todoNo;
    private final String todoContent;
    private final CompleteType todoComplete;

    public static ResponseTodoList from(TodoList todolist) {
        return new ResponseTodoList(todolist.getTodoNo(),todolist.getTodoContent(),todolist.getTodoComplete());
    }
}
