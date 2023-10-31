package com.ssafy.journeymate.mateservice.dto.request.mate;


import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;


@NoArgsConstructor
@Data
public class MateRegistPostReq {

    private String name;
    private String destination;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    private List<String> users;
    private String creator;

}