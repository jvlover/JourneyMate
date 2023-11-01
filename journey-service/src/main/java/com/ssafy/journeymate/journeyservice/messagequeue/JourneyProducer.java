package com.ssafy.journeymate.journeyservice.messagequeue;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.journeymate.journeyservice.dto.kafka.Field;
import com.ssafy.journeymate.journeyservice.dto.kafka.KafkaJourneyDto;
import com.ssafy.journeymate.journeyservice.dto.kafka.Payload;
import com.ssafy.journeymate.journeyservice.dto.kafka.Schema;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import com.ssafy.journeymate.journeyservice.entity.Journey;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JourneyProducer {

    /*  journey-service와 DB간 카프카 통신을 위한 코드 */
    private KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(new Field("int64", true, "id"),
            new Field("int64", true, "mated_id"),
            new Field("string", true, "category_id"),
            new Field("string", true, "title"),
            new Field("int32", true, "day"),
            new Field("int32", true, "sequence"),
            new Field("FLOAT", true, "xcoordinate"),
            new Field("FLOAT", true, "ycoordinate"),
            new Field("datetime", true, "created_at"),
            new Field("datetime", true, "updated_at"),
            new Field("int32", true, "is_deleted")
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

    public Journey upsertJourney(String topic, Journey journey) {

        Payload payload = Payload.builder()
                .id(journey.getId())
                .mate_id(journey.getMateId())
                .category_id(journey.getCategoryId())
                .title(journey.getTitle())
                .day(journey.getDay())
                .sequence(journey.getSequence())
                .xcoordinate(journey.getXcoordinate())
                .ycoordinate(journey.getYcoordinate())
                .created_at(journey.getCreatedAt())
                .updated_at(journey.getUpdatedAt())
                .is_deleted(journey.getIsDeleted())
                .build();

        KafkaJourneyDto kafkaJourneyDto = new KafkaJourneyDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaJourneyDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from the Journey " + kafkaJourneyDto.toString());

        return journey;

    }


}
