package com.example.secondProject.entity;

import com.example.secondProject.DTO.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="member_table")   //테이블 이름
@Getter
@Setter
public class MemberEntity {   //DB테이블을 자바 객체처럼 사용할 수 있게해줌
    @Id  //pk지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //auto-increment
    private Long id;

    @Column(unique=true)
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){  //insert용
        MemberEntity memberEntity= new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        return memberEntity;
    }
    public static MemberEntity toUpdateMemberEntity(MemberDTO memberDTO){   //update용
        MemberEntity memberEntity= new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        return memberEntity;
    }
}
