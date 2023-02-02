package com.example.secondProject.service;

import com.example.secondProject.DTO.MemberDTO;
import com.example.secondProject.entity.MemberEntity;
import com.example.secondProject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO){   //메소드 이름은 save여야한다 (insert 역할)
        //리포지토리의 save메소드 호출(조건: 엔티티객체를 넘겨줘야 함)
        //1. DTO -> Entity 변환
        MemberEntity memberEntity=MemberEntity.toMemberEntity(memberDTO);

        //2. repository의 save 메소드 호출
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO){
        //1. 회원이 입력한 이메일로 DB에서 조회를 함
        //2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
        Optional<MemberEntity> byMemberEmail=memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(byMemberEmail.isPresent()){
            //조회 결과가 존재(해당 이메일을 가진 회원 정보가 있다)
            MemberEntity memberEntity=byMemberEmail.get();

            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
                //비밀번호가 일치하는 경우
                // entity -> DTO로 변환
                MemberDTO dto=MemberDTO.toMemberDTO(memberEntity);
                return dto;
            }else{
                //비밀번호가 일치하지 않는 경우
                return null;
            }
        }else{
            //조회 결과가 존재X(해당 이메일을 가진 회원 정보가 없다)
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList=memberRepository.findAll();     //리포지토리에서 findAll그냥 지원해줌
        List<MemberDTO> memberDTOList=new ArrayList<>();
        for(MemberEntity memberEntity:memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity=memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else{
            return null;
        }
    }

    public MemberDTO updateForm(MemberDTO memberDTO) {
        Optional<MemberEntity> optionalMemberEntity=
                memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }else{
            return null;
        }
    }

    public void update(MemberDTO memberDTO){
        //save는 id(키값)을 안주면 insert로 인식, 주면 update로 인식한다
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));  //id가 있으면 update쿼리, 없으면 insert쿼리
    }

    public void deleteById(Long id){
        memberRepository.deleteById(id);
    }

    public boolean emailCheck(String memberEmail){
        Optional<MemberEntity> optionalMemberEntity=memberRepository.findByMemberEmail(memberEmail);
        if(optionalMemberEntity.isPresent()){
            return true;
        }else{
            return false;
        }
    }
}
