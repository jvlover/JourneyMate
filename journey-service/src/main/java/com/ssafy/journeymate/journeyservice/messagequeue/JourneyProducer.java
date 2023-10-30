package com.ssafy.journeymate.journeyservice.messagequeue;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.journeymate.journeyservice.dto.kafka.Field;
import com.ssafy.journeymate.journeyservice.dto.kafka.KafkaJourneyDto;
import com.ssafy.journeymate.journeyservice.dto.kafka.Payload;
import com.ssafy.journeymate.journeyservice.dto.kafka.Schema;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
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


    /*  예시 코드 */
    public JourneyGetRes sendJourney(String topic, JourneyGetRes journeyGetRes) {
        
//        Payload payload = Payload.builder()
//                .id(journeyGetRes.getId())
//                .mate_id()
//                .category_id()
//                .title()
//                .day()
//                .sequence()
//                .xcoordinate()
//                .ycoordinate()
//                .created_at()
//                .updated_at()
//                .is_deleted()
//                .build();

        Payload payload = Payload.builder().build();

        KafkaJourneyDto kafkaJourneyDto = new KafkaJourneyDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaJourneyDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from the Journey " + kafkaJourneyDto);

        return journeyGetRes;

    }


}
