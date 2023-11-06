package com.journeymate.checkservice.messageQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.journeymate.checkservice.dto.request.ChecklistKafkaReq;
import com.journeymate.checkservice.dto.request.ChecklistKafkaReq.DefaultItem;
import com.journeymate.checkservice.dto.response.ChecklistRegistRes;
import com.journeymate.checkservice.service.ChecklistService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    private final ChecklistService checklistService;

    @Autowired
    public KafkaConsumer(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @KafkaListener(topics = "checklist-update")
    public void updateCategoryToJourney(String kafkaMessage) {

        log.info("Kafka_Message : " + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        /* checklistDto{Long mateId, Long journeyId, List<itemGetRes> items} 받는 로직 */

        Long mateId, journeyId;

        String type = (String) map.get("type");

        List<DefaultItem> defaultItems = new ArrayList<>();

        Object mateIdObject = map.get("mateId");

        if (mateIdObject instanceof Integer) {

            mateId = ((Integer) mateIdObject).longValue();

        } else if (mateIdObject instanceof Long) {

            mateId = (Long) mateIdObject;

        } else {

            throw new IllegalArgumentException("Invalid type for mateId");

        }

        Object journeyIdObject = map.get("journeyId");

        if (journeyIdObject instanceof Integer) {

            journeyId = ((Integer) journeyIdObject).longValue();

        } else if (journeyIdObject instanceof Long) {

            journeyId = (Long) journeyIdObject;

        } else {

            throw new IllegalArgumentException("Invalid type for journeyId");

        }

        Object itemObject = map.get("items");

        try {
            if (itemObject instanceof List<?>) {

                defaultItems = mapper.convertValue(itemObject, new TypeReference<>() {
                });

            }
        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException("Failed to convert 'items' to List<itemGetRes>");

        }

        log.info("Kafka_message_mateId_check: " + mateId);

        log.info("Kafka_message_journeyId_check: " + journeyId);

        log.info("Kafka_message_items+check: " + defaultItems.toString());

        log.info("Kafka_message_service+type : " + type);

        ChecklistKafkaReq checklistKafkaReq = new ChecklistKafkaReq(journeyId, mateId,
            defaultItems);

        if (type.equals("REGIST")) {

            log.info("Kafka_Consumer_Checklist_Regist_start:  " + checklistKafkaReq);

            List<ChecklistRegistRes> res = checklistService.registChecklist(checklistKafkaReq);

            log.info("Kafka_Consumer_Checklist_Regist_end:  " + res);

        } else if (type.equals("DELETE")) {

            log.info("Kafka_Consumer_Checklist_Delete_start:  " + checklistKafkaReq);

            checklistService.deleteChecklist(checklistKafkaReq.getJourneyId());

            log.info("Kafka_Consumer_Checklist_Delete_end: SUCCESS");

        } else if (type.equals("UPDATE")) {

            log.info("Kafka_Consumer_Checklist_Update start:  " + checklistKafkaReq);

            List<ChecklistRegistRes> res = checklistService.updateChecklist(checklistKafkaReq);

            log.info("Kafka_Consumer_Checklist_Update end:  " + res);

        }
    }
}
