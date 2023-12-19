package com.kimleepark.thesilver.todolist.domain.repository;

import com.kimleepark.thesilver.todolist.domain.TodoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TodoListRepository extends JpaRepository<TodoList,Long> {

    Page<TodoList> findByEmployeeCodeAndTodoDate(Pageable pageable, Long empNo, LocalDate date);
}
