package ssafy.journeymate.categoryservice.categoryservice.dto.kafka;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaJourneyDto implements Serializable {

    private Schema schema;
    private Payload payload;

}
