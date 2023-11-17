package com.ssafy.journeymate.journeyservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.journeymate.journeyservice.dto.ChecklistDto;
import com.ssafy.journeymate.journeyservice.dto.response.ItemGetRes;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import com.ssafy.journeymate.journeyservice.entity.Journey;
import com.ssafy.journeymate.journeyservice.repository.JourneyRepository;
import com.ssafy.journeymate.journeyservice.service.JourneyService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {
    JourneyRepository journeyRepository;

    JourneyService journeyService;

    KafkaProducer kafkaProducer;

    @Autowired
    public KafkaConsumer(JourneyRepository journeyRepository, JourneyService journeyService,
                         KafkaProducer kafkaProducer) {
        this.journeyRepository = journeyRepository;
        this.journeyService = journeyService;
        this.kafkaProducer = kafkaProducer;
    }

    @KafkaListener(topics = "journeys-delete")
    public void updateCategoryToJourney(String kafkaMessage) {
        log.info("Kafka Message: -> " + kafkaMessage);
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long mateId;
        Object mateIdObject = map.get("mateId");
        if (mateIdObject instanceof Integer) {
            mateId = ((Integer) mateIdObject).longValue();
        } else if (mateIdObject instanceof Long) {
            mateId = (Long) mateIdObject;
        } else {
            throw new IllegalArgumentException("Invalid type for mateId");
        }

        log.info("Kafka message mateId check: " + mateId);
        try {
            if (mateId != null) {
                List<JourneyGetRes> journeyGetResponses = journeyService.deleteJourneysInMate(mateId);
//                for (JourneyGetRes journeyGetRes : journeyGetResponses) {
//                    List<ItemGetRes> items = new ArrayList<>();
//                    ChecklistDto checklistDto = new ChecklistDto(journeyGetRes.getMateId(),
//                            journeyGetRes.getId(), "DELETE", items);
//                    kafkaProducer.sendItems("checklist-update", checklistDto);
//                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

    }


}
