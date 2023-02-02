package com.example.secondProject.controller;

import com.example.secondProject.DTO.MemberDTO;
import com.example.secondProject.DTO.StockDTO;
import com.example.secondProject.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor   //사용하는 서비스의 생성자를 자동으로 소환(final 변수)
public class HomeController {

    private final MemberService memberService;
    private WebDriver driver;    //웹과 연결하는 변수
    private WebElement element;  //dom객체라고 보면됨
    public static String WEB_DRIVER_ID="webdriver.chrome.driver";
    public static String WEB_DRIVER_PATH="C:/Users/jhng7/Desktop/chromedriver_win32/chromedriver.exe";


    @GetMapping("/test")
    public String getTest(){
        return "hihi!!";
    }

    @GetMapping("/member/save")
    public String saveForm(){
        return "/member/save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){   //DTO의 변수와 html의 변수이름이 똑같으면 자동으로 연결시켜줌
        memberService.save(memberDTO);
        return "/member/login";
        }

    @GetMapping("/member/login")
    public String loginForm(){
        return "/member/login";
    }
    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,Model model){   //DTO의 변수와 html의 변수이름이 똑같으면 자동으로 연결시켜줌
        MemberDTO loginResult=memberService.login(memberDTO);
        if(loginResult!=null){
            //login성공
            session.setAttribute("loginEmail",loginResult.getMemberEmail());
            session.setAttribute("result",loginResult);
            List<MemberDTO> memberDTOList=memberService.findAll();
            model.addAttribute("memberList",memberDTOList);
            return "/member/main";
        }else{
            return "/member/login";
        }
    }

    @GetMapping("/member/main")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList=memberService.findAll();
        model.addAttribute("memberList",memberDTOList);

        String header="";
        List<StockDTO> contents=new ArrayList<>();
        final String stockList="https://finance.naver.com/sise/lastsearch2.naver";
        Connection connection= Jsoup.connect(stockList);

        try{
            Document document=connection.get();  // Documemt객체는 Jsoup걸로 사용
            header=getStockHeader(document);
            contents=getStockList(document);
        }catch(IOException e){
        }

        String[] stockTitle=header.split(" ");

        model.addAttribute("rank",stockTitle[0]);
        model.addAttribute("name",stockTitle[1]);
        model.addAttribute("searchrate",stockTitle[2]);
        model.addAttribute("presentprice",stockTitle[3]);
        model.addAttribute("updown",stockTitle[5]);
        model.addAttribute("quantity",stockTitle[6]);

        model.addAttribute("contents",contents);
        chrome();
        return "/member/main";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id,Model model){  // 경로상에 있는 변수를 받아옴
        MemberDTO memberDTO=memberService.findById(id);
        model.addAttribute("member",memberDTO);
        return "/member/detail";
    }

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model){
        MemberDTO memberDTO=(MemberDTO) session.getAttribute("result");
        MemberDTO memberDTO1=memberService.updateForm(memberDTO);
        model.addAttribute("updateMember",memberDTO1);
        return "/member/update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/member/"+memberDTO.getId();  //만약 바로 조회화면으로 간다면 빈 화면이 출력된다(이전화면에서 memberDTO정보를 못받아왔기 때문)
    }

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id){
        memberService.deleteById(id);
        return "redirect:/member/main";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "/member/login";
    }

    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail){
        memberService.emailCheck(memberEmail);
        String checkResult;
        if(memberService.emailCheck(memberEmail)){ //DB에 있는 이메일(사용불가능한 이메일)
            checkResult="No";

        }else{  //DB에 없는 이메일(사용가능한 이메일)
            checkResult="OK";
        }
        return checkResult;
    }

    public static String getStockHeader(Document document){
        Elements stockTableBody=document.select("table.type_5 tbody tr");
        StringBuilder sb=new StringBuilder();

        for(Element element:stockTableBody){
            for(Element td: element.select("th")){
                sb.append(td.text());
                sb.append(" ");
            }
            break;
        }
        return sb.toString();
    }
    public static List<StockDTO> getStockList(Document document){
        Elements stockTableBody=document.select("table.type_5 tbody tr");
        StringBuilder sb=new StringBuilder();
        List<StockDTO> stockDTOList=new ArrayList<>();
        StockDTO stockDTO=new StockDTO();
        for(Element element:stockTableBody){

            if(element.select("td").hasAttr("colspan") || element.hasClass("type1")){
                continue;
            }
            for(Element td:element.select("td")){
                sb.append(td.text());
                sb.append("@");
            }
            sb.append(System.getProperty("line.separator"));  //줄바꿈

        }
        String[] content=sb.toString().split(System.getProperty("line.separator"));
        for(String stockcontent:content){
            String[] stock=stockcontent.split("@");
            stockDTO=new StockDTO(stock[0],stock[1],stock[2],stock[3],stock[5],stock[6]);
            stockDTOList.add(stockDTO);
        }
        return stockDTOList;
    }

    private void chrome(){   //셀레니움
        System.setProperty(WEB_DRIVER_ID,WEB_DRIVER_PATH);
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");

        String url = "https://fifaonline4.nexon.com/datacenter";
        driver=new ChromeDriver(options);
        //driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        //driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        try{
            driver.get(url);
            driver.manage().window().maximize();
            Thread.sleep(3000);
            //driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

            ((JavascriptExecutor)driver).executeScript("window.scrollTo(0,200)");
            Thread.sleep(1000);
            //element.click();   //셀레니움input클릭검색해보기
            //element= driver.findElement(By.xpath("//*[@id=\"season_bwc\"]"));
            ((JavascriptExecutor)driver).executeScript("document.getElementById(\"season_bwc\").click();");

            //element.sendKeys(Keys.ENTER);   //이미지 누를때
            Thread.sleep(2000);



            element = driver.findElement(By.className("btn_search"));
            //element.sendKeys("jhng77");
            element.click();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            int count=0;
            //만약 리스트별로 뽑고 싶을때는 원하는 범위는 인덱싱 안해도됨
            List<WebElement> players=driver.findElements(By.xpath("//*[@id=\"divPlayerList\"]/div/div[1]/div/div[4]/div[2]"));
            for(WebElement player:players){
                count++;
                System.out.println(count+":"+player.getText());
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            driver.close();
        }
    }

}
