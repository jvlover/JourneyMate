package com.ssafy.journeymate.journeyservice.dto.kafka;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaJourneyDto implements Serializable {

    private Schema schema;
    private Payload payload;

}
