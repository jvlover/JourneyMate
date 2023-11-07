package com.journeymate.userservice.dto.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeRegistRes {

    private Long id;

    private UserFindRes user;

    private Long mateId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isCreator;

}
