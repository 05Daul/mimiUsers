package com.example.mimi.il.entity;

import com.example.mimi.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false, unique=true)
    private String email;
    @Column(nullable = false)
    private String password;
    // default 값은 null이고 관리자계정에서 설정으로만 바꿀수 있음
    @Column(nullable = false)
    private Boolean role = false;
    @Column(nullable = false)
    private String phoneNum;



    // 생성용
    public User(String userName, String email, String password, String phoneNum) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    // 수정용(email은 변경 불가)
    public User(Long userId, String userName, String password, String phoneNum) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    // 관리자가 유저 role 변경
    public User(Long userId, Boolean role) {
        this.role = role;
        this.userId = userId;
    }
}
