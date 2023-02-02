package com.example.secondProject.repository;

import com.example.secondProject.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    //첫번쨰인자: 사용할 엔티티, 두번째 인자: pk타입

    //이메일로 회원정보(select * from member_table where member_email=?)
    Optional<MemberEntity> findByMemberEmail(String memberEmail);

}
