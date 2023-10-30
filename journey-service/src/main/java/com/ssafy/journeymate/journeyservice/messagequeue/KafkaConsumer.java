package com.ssafy.journeymate.journeyservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.journeymate.journeyservice.entity.Journey;
import com.ssafy.journeymate.journeyservice.repository.JourneyRepository;
import java.util.HashMap;
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

    @Autowired
    public KafkaConsumer(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    @KafkaListener(topics = "category-journey-topic")
    public void updateCategoryToJourney(String kafkaMessage){
        log.info("Kafka Message: -> "+ kafkaMessage);
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        /*  틀을 위한 예시코드로 상황에 맞게 수정 필요

        강의 예시 (오더 서비스 -> 카탈로그 서비스인 상황에서 물품 id를 받아서 수정, 해당 물품 아이디를 갖는 수량 수정)
        CatagoryEntity entity = repository.findByProductId((String) map.get("productId"));

         */

        Long journeyId =0l;
        Optional<Journey> journey = journeyRepository.findById(journeyId);
        String categoryIdString = (String) map.get("categoryId");
        Integer categoryId = 0;
        try {
            if (categoryIdString != null) {
                categoryId = Integer.valueOf(categoryIdString);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        String categoryName = (String)map.get("name");
        String categoryIcon = (String)map.get("icon");

//        journey.update(categoryId, icon);
//        journeyRepository.save(journey);



    }



}
