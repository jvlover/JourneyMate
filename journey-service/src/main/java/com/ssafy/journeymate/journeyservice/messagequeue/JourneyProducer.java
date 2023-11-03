package com.ssafy.journeymate.journeyservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssafy.journeymate.journeyservice.dto.kafka.Field;
import com.ssafy.journeymate.journeyservice.dto.kafka.KafkaJourneyDto;
import com.ssafy.journeymate.journeyservice.dto.kafka.Payload;
import com.ssafy.journeymate.journeyservice.dto.kafka.Schema;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyRegistPostReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@JsonDeserialize
@JsonSerialize
public class JourneyProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(
            new Field("int64", true, "id"),
            new Field("int64", true, "mateId"),
            new Field("int64", true, "categoryId"),
            new Field("String", true, "title"),
            new Field("int32", true, "day"),
            new Field("int32", true, "sequence"),
            new Field("float64", true, "xcoordinate"),
            new Field("float64", true, "ycoordinate"),
            new Field("String", true, "createdAt"),
            new Field("String", true, "updatedAt"),
            new Field("int32", true, "isDeleted")
    );

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("journey")
            .build();

    @Autowired
    public JourneyProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void insertJourney(String topic, JourneyRegistPostReq journeyRegistPostReq) {
        Payload payload = Payload.builder()
                .id(0l)
                .mate_id(journeyRegistPostReq.getMateId())
                .category_id(journeyRegistPostReq.getCategoryId())
                .title(journeyRegistPostReq.getTitle())
                .day(journeyRegistPostReq.getDay())
                .sequence(journeyRegistPostReq.getSequence())
                .xcoordinate(journeyRegistPostReq.getXcoordinate())
                .ycoordinate(journeyRegistPostReq.getYcoordinate())
                .createdAt(LocalDateTime.now().toString())
                .updatedAt(LocalDateTime.now().toString())
                .is_deleted(0)
                .build();

        KafkaJourneyDto kafkaJourneyDto = new KafkaJourneyDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaJourneyDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from the Journey " + kafkaJourneyDto.toString());
    }
}





