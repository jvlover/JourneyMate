package ssafy.journeymate.categoryservice.categoryservice.dto.kafka;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {

    private Long id;
    private Long mate_id;

}
