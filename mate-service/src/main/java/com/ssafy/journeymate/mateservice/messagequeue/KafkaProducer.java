package com.ssafy.journeymate.mateservice.messagequeue;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.journeymate.mateservice.dto.request.messagequeue.MateDeleteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, MateDeleteDto mateDeleteDto){
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try{
            jsonInString = mapper.writeValueAsString(mateDeleteDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Kafka 전송 에러가 발생했습니다");
        }

        kafkaTemplate.send(topic,jsonInString);

        log.info("Kafka Producer sent data from Mate microservice : " + mateDeleteDto.toString());
    }




}
