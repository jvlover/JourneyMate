package com.ssafy.journeymate.journeyservice.controller;


import com.ssafy.journeymate.journeyservice.dto.ResponseDto;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import com.ssafy.journeymate.journeyservice.service.JourneyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/journey-service/")
public class JourneyController {

    private Environment env;
    private JourneyService journeyService;


    @Autowired
    public JourneyController(Environment env, JourneyService journeyService) {
        this.env = env;
        this.journeyService = journeyService;
    }


    /* 테스트용 */
    @GetMapping("/health_check")
    public String status(){

        return String.format("It's Working in User Service"
                +", port(local.sever.port)=" + env.getProperty("local.server.port"))
                +", port(sever.port)=" + env.getProperty("server.port")
                +", token secret=" + env.getProperty("token.secret")
                +", token expiration time=" + env.getProperty("token.expiration_time");
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> registJourney() {


        return new ResponseEntity<>(new ResponseDto("일정 등록 완료", null), HttpStatus.OK);
    }

    @PutMapping("/delete/{journeyId}")
    public ResponseEntity<ResponseDto> deleteJourney() {


        return new ResponseEntity<>(new ResponseDto("일정 삭제 완료", null), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateJourney() {

        return new ResponseEntity<>(new ResponseDto("일정 수정 완료", null), HttpStatus.OK);
    }

    @GetMapping("/{mateId}")
    public ResponseEntity<ResponseDto> getMateJourneys() {

        return new ResponseEntity<>(new ResponseDto("그룹내 일정들 상세 정보입니다.", null), HttpStatus.OK);
    }

    @GetMapping("/journey/{journeyId}")
    public ResponseEntity<ResponseDto> getJourney() {

        return new ResponseEntity<>(new ResponseDto("일정 상세 정보입니다.", null), HttpStatus.OK);
    }

    @GetMapping("/journey/journeyicon/{journeyId}")
    public ResponseEntity<ResponseDto> getJourneyIcon() {

        return new ResponseEntity<>(new ResponseDto("일정 아이콘입니다.", null), HttpStatus.OK);
    }

    @GetMapping("/journey/categoryicon/{categoryId}")
    public ResponseEntity<ResponseDto> getCategoryIcon() {

        
        return new ResponseEntity<>(new ResponseDto("카테고리 아이콘입니다.", null), HttpStatus.OK);
    }


}
