package com.ssafy.journeymate.journeyservice.messagequeue;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.journeymate.journeyservice.dto.ChecklistDto;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    /*  예시 코드

    public JourneyGetRes registJourney(String topic, JourneyGetRes journeyGetRes) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(journeyGetRes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from the Journey");

        return journeyGetRes;

    }

     */

    public ChecklistDto sendItems(String topic, ChecklistDto checklistDto) {

        log.info("Kafka Producer sent Items data from the Journey-service start: " + checklistDto.toString());
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(checklistDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent Items data from the Journey-service end");

        return checklistDto;

    }


}
