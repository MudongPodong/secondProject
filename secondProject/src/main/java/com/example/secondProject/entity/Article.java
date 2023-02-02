package com.example.secondProject.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue   // 1, 2, 3 .... 자동으로 번호 생성
    private Long id;  //대표값(키값)
    @Column
    private String title;
    @Column
    private String content;

}
