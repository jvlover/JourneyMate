package com.journeymate.userservice.dto.response;

import com.journeymate.userservice.entity.User;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeRegistRes {

    private Long id;

    private User user;

    private Long mateId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isCreator;

}
