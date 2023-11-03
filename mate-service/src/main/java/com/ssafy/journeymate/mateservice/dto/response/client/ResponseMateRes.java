package com.ssafy.journeymate.mateservice.dto.response.client;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize
public class ResponseMateRes implements Serializable {

    private String message;
    private Object data;

}
