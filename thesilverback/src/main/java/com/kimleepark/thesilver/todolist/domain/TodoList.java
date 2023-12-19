package com.kimleepark.thesilver.todolist.domain;

import com.kimleepark.thesilver.todolist.domain.type.CompleteType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;

import static com.kimleepark.thesilver.todolist.domain.type.CompleteType.COMPLETE;
import static com.kimleepark.thesilver.todolist.domain.type.CompleteType.INCOMPLETE;

@Entity
@Table(name = "tbl_todolist")
@Where(clause = "status = 'N'")
@ToString
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE tbl_todolist SET status = 'Y' WHERE todo_no=?")
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoNo;

    @CreatedDate
    private LocalDate todoDate;

    private long employeeCode;

    private String todoContent;

    @Enumerated(value = EnumType.STRING)
    private CompleteType todoComplete = INCOMPLETE;

    private char status = 'N';

    public TodoList(long empNo, String content) {
        this.employeeCode = empNo;
        this.todoContent = content;
    }

    public static TodoList of(long empNo, String content) {
        return new TodoList(empNo, content);
    }


    public void updateContent(String content) {
        this.todoContent = content;
    }

    public void updateComplete(String message) {
        if(message.equals("COMPLETE")){
            this.todoComplete = COMPLETE;
        }else if(message.equals("INCOMPLETE")){
            this.todoComplete = INCOMPLETE;
        }

    }
}
