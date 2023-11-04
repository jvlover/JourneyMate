package com.journeymate.checkservice.dto.response;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeFindRes {

    private String message;

    private MateBridgeFindData data;

    @Data
    public static class MateBridgeFindData {

        List<UserFindRes> users;

        String creator;

    }
}
