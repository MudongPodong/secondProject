package com.example.secondProject.controller;

import com.example.secondProject.DTO.MemberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AjaxController {
    @GetMapping("/ex01")   //자바스크립트의 url을 인식함
    public String ex01(){
        System.out.println("AjaxController.ex01");
        return "/member/main";  // ajax는 화면변환을 하진 않고, 화면의 정보(코드)를 넘겨줌
    }

    @PostMapping("/ex02")
    public @ResponseBody String ex02(){
        System.out.println("AjaxController.ex02");
        return "/member/main"; // ResponseBody가 body부분(return값 아무거나)를 넘겨줌
    }

    @GetMapping("/ex03")
    public @ResponseBody String ex03(@RequestParam("param1") String param1,
                                     @RequestParam("param2") String param2){
        System.out.println("AjaxController.ex03"+":"+param1+","+param2);
        return "호출완료!"; // ResponseBody가 body부분(return값 아무거나)를 넘겨줌
    }

    @PostMapping("/ex04")
    public @ResponseBody String ex04(@RequestParam("param1") String param1,
                                     @RequestParam("param2") String param2){
        System.out.println("AjaxController.ex04"+":"+param1+","+param2);
        return "호출완료!!"; // ResponseBody가 body부분(return값 아무거나)를 넘겨줌
    }

    @GetMapping("/ex05")  //객체를 프론트(웹)으로 보내준다
    public @ResponseBody MemberDTO ex05(@ModelAttribute MemberDTO memberDTO){
        System.out.println("AjaxController.ex05"+":"+memberDTO);
        return memberDTO;
    }

    @PostMapping("/ex06")  //객체를 프론트(웹)으로 보내준다
    public @ResponseBody MemberDTO ex06(@ModelAttribute MemberDTO memberDTO){
        System.out.println("AjaxController.ex06"+":"+memberDTO);
        return memberDTO;
    }

    @PostMapping("/ex07") //ex06과 결과는 똑같다 하지만 json형식으로 보낸것
    public @ResponseBody MemberDTO ex07(@RequestBody MemberDTO memberDTO){
        System.out.println("AjaxController.ex07"+":"+memberDTO);
        return memberDTO;
    }

    private List<MemberDTO> DTOList(){
        List<MemberDTO> dtoList=new ArrayList<>();
        dtoList.add(new MemberDTO(0L,"jhng1","1234","무중"));
        dtoList.add(new MemberDTO(1L,"jhng2","5678","무동"));
        return dtoList;
    }
    @PostMapping("/ex08")
    public @ResponseBody List<MemberDTO> ex08(@RequestBody MemberDTO memberDTO){
        System.out.println("AjaxController.ex08"+":"+memberDTO);
        List<MemberDTO> dtoList=DTOList();
        dtoList.add(memberDTO);
        return dtoList;
    }
    @PostMapping("/ex09") //ResponseEntity는 페이지 상태에 따른 제어 + ex08
    public ResponseEntity ex09(@RequestBody MemberDTO memberDTO){
        System.out.println("AjaxController.ex09"+":"+memberDTO);
        return new ResponseEntity<>(memberDTO,HttpStatus.OK);   //HttpStatus는 웹의 상태코드를 의미 즉, 여기서는 상태만 리턴
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);   //Not found 유발시키기
    }
    @PostMapping("/ex10") //list형태+ResponseEntity는 페이지 상태에 따른 제어 + ex08
    public ResponseEntity ex10(@RequestBody MemberDTO memberDTO){
        System.out.println("AjaxController.ex10"+":"+memberDTO);
        List<MemberDTO> dtoList=DTOList();
        dtoList.add(memberDTO);
        return new ResponseEntity<>(dtoList,HttpStatus.OK);   //HttpStatus는 웹의 상태코드를 의미 즉, 여기서는 상태만 리턴
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);   //Not found 유발시키기
    }
}
