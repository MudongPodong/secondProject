package com.example.secondProject.DTO;


import com.example.secondProject.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class ArticleForm {
    private String title;
    private String content;


    public Article toEntity(){
        return new Article(null,title,content);
    }
}
