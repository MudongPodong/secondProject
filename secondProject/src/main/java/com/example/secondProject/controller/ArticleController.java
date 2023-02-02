package com.example.secondProject.controller;



import com.example.secondProject.DTO.ArticleForm;
import com.example.secondProject.entity.Article;
import com.example.secondProject.repository.ArticleRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j     //로깅을 위한 어노테이션
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;  //객체안만들어도됨 (스프링부트가 자동으로 만들어줌) -> Autowired
    @GetMapping("/articles/new")
    public String newArticleForm(){
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){  //여기로 자동으로 받아옴
        log.info(form.toString());

        //1. DTO -> Entity
        //2. Repository에 Entity를 DB안에 저장

        Article article = form.toEntity();  //1. DTO -> Entity

        Article saved=articleRepository.save(article);   //CrudRepository의 내용을 상속받기때문에 그대로 사용가능
        log.info(saved.toString());
        return "articles/create";
    }


}