package com.ssafy.journeymate.mateservice.dto.request.messagequeue;


import lombok.Builder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import lombok.Data;

@Builder
@JsonSerialize
@Data
public class MateDeleteDto implements Serializable {

    private Long mateId;
}