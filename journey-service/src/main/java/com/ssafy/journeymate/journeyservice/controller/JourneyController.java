package com.ssafy.journeymate.journeyservice.controller;


import com.ssafy.journeymate.journeyservice.client.CategoryServiceClient;
import com.ssafy.journeymate.journeyservice.dto.ChecklistDto;
import com.ssafy.journeymate.journeyservice.dto.ResponseDto;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyModifyPutReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyRegistPostReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneySwitchSequenceReq;
import com.ssafy.journeymate.journeyservice.dto.response.ItemGetRes;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import com.ssafy.journeymate.journeyservice.entity.Journey;
import com.ssafy.journeymate.journeyservice.exception.JourneyNotFoundException;
import com.ssafy.journeymate.journeyservice.messagequeue.JourneyProducer;
import com.ssafy.journeymate.journeyservice.messagequeue.KafkaProducer;
import com.ssafy.journeymate.journeyservice.service.JourneyService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/journey-service/")
public class JourneyController {

    private Environment env;
    private JourneyService journeyService;
    private KafkaProducer kafkaProducer;
    private JourneyProducer journeyProducer;
    private CategoryServiceClient categoryServiceClient;

    @Autowired
    public JourneyController(Environment env, JourneyService journeyService, KafkaProducer kafkaProducer,
                             JourneyProducer journeyProducer) {
        this.env = env;
        this.journeyService = journeyService;
        this.kafkaProducer = kafkaProducer;
        this.journeyProducer = journeyProducer;
    }


    /* 포트 확인 테스트용 */
    @GetMapping("/health_check")
    public String status() {

        return String.format("It's Working in User Service"
                + ", port(local.sever.port)=" + env.getProperty("local.server.port"))
                + ", port(sever.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time");
    }

    @GetMapping("/journey/{journeyId}")
    public ResponseEntity<ResponseDto> getJourney(@PathVariable Long journeyId) throws
            JourneyNotFoundException {

        log.info("journey_getJourney_start: " + journeyId);
        JourneyGetRes journeyGetRes = journeyService.findByJourneyId(journeyId);
        log.info("journey_getJourney_end: " + journeyGetRes.toString());
        return new ResponseEntity<>(new ResponseDto("일정 상세 정보입니다", journeyGetRes), HttpStatus.OK);
    }

    @GetMapping("/{mateId}")
    public ResponseEntity<ResponseDto> getMateJourneys(@PathVariable Long mateId) throws JourneyNotFoundException {

        log.info("journey_getMateJourneys_start: " + mateId);
        List<JourneyGetRes> journeyGetResponses = journeyService.findByMateId(mateId);
        log.info("journey_getMateJourneys_end: " + journeyGetResponses.toString());

        return new ResponseEntity<>(new ResponseDto("그룹내 일정들 상세 정보입니다.", journeyGetResponses), HttpStatus.OK);
    }

    /*  create, update 등 db에 연관된 사항 kafka 사용  */

    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> registJourney(@RequestBody JourneyRegistPostReq journeyRegistPostReq) {

        log.info("journey_registJourney_start: " + journeyRegistPostReq.toString());
        Journey journey = new Journey(journeyRegistPostReq);
        journeyProducer.upsertJourney("journey-topic", journey);
        List<ItemGetRes> items = journeyService.getItemsInCategory(journeyRegistPostReq.getCategoryId());
        log.info("journey_registJourney_middle_getItems: " + items.toString());

        ChecklistDto checklistDto = new ChecklistDto(journeyRegistPostReq.getMateId(),
                journeyRegistPostReq.getJourneyId(), "REGIST", items);
        kafkaProducer.sendItems("checklist-update", checklistDto);

        log.info("journey_registJourney_end");

        return new ResponseEntity<>(new ResponseDto("일정 등록 완료", null), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateJourney(@RequestBody JourneyModifyPutReq journeyModifyReq) {

        log.info("journey_updateJourney_start: " + journeyModifyReq.toString());
        Journey journey = new Journey(journeyModifyReq);
        journeyProducer.upsertJourney("journey-topic", journey);
        List<ItemGetRes> items = journeyService.getItemsInCategory(journeyModifyReq.getCategoryId());
        log.info("journey_registJourney_middle_getItems: " + items.toString());

//        ChecklistDto checklistDto = new ChecklistDto(journeyModifyReq.getMateId(),
//                journeyModifyReq.getJourneyId(), "UPDATE", items);
//        kafkaProducer.sendItems("checklist-update", checklistDto);

        log.info("journey_updateJourney_end");

        return new ResponseEntity<>(new ResponseDto("일정 수정 완료", null), HttpStatus.OK);
    }


    /* delete는 jpa를 활용해서 구현, 프론트 로직이 너무 복잡해서 */

    @PutMapping("/delete/{journeyId}")
    public ResponseEntity<ResponseDto> deleteJourney(@PathVariable Long journeyId) {

        log.info("journey_deleteJourney_start: " + journeyId.toString());
        JourneyGetRes journeyGetRes = journeyService.deleteJourney(journeyId);
//        List<ItemGetRes> items = new ArrayList<>();
//        ChecklistDto checklistDto = new ChecklistDto(journeyGetRes.getMateId(),
//                journeyGetRes.getId(), "DELETE", items);
//        kafkaProducer.sendItems("checklist-update", checklistDto);

        log.info("journey_deleteJourney_end");

        return new ResponseEntity<>(new ResponseDto("일정 삭제 완료", journeyGetRes), HttpStatus.OK);
    }

    @PutMapping("/deletejourneys/{mateId}")
    public ResponseEntity<ResponseDto> deleteJourneysInMate(@PathVariable Long mateId) {

        log.info("journey_deleteJourneysInMate_start: " + mateId.toString());
        List<JourneyGetRes> journeyGetResponses = journeyService.deleteJourneysinMate(mateId);
//        for (JourneyGetRes journeyGetRes : journeyGetResponses) {
//            List<ItemGetRes> items = new ArrayList<>();
//            ChecklistDto checklistDto = new ChecklistDto(journeyGetRes.getMateId(),
//                    journeyGetRes.getId(), "DELETE", items);
//            kafkaProducer.sendItems("checklist-update", checklistDto);
//        }

        log.info("journey_deleteJourneysInMate_end");

        return new ResponseEntity<>(new ResponseDto("그룹 내 일정들 삭제 완료", journeyGetResponses), HttpStatus.OK);
    }

    /* 추가 기능 일정 위치 스왑 */
    @PutMapping("/updatesequence")
    public ResponseEntity<ResponseDto> updateSequenceJourney(
            @RequestBody JourneySwitchSequenceReq journeySwitchSequenceReq) {
        return new ResponseEntity<>(new ResponseDto("일정 순서 스왑 완료", null), HttpStatus.OK);
    }


}
