package com.kimleepark.thesilver.account.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_login_history")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LoginHistory {

    @Id
    private Long loginCode;
}
