package com.example.secondProject.repository;

import com.example.secondProject.entity.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article,Long> { //첫번째: 관리대상, 두번째:ID

}