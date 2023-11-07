package com.journeymate.checkservice.dto.request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChecklistModifyPutReq {

    private String userId;

    private Long journeyId;

    private List<PersonalItem> personalItems;

    @Data
    public static class PersonalItem {

        private Long id;

        private String name;

        private Integer num;

        private Boolean isChecked;

        private Boolean isDeleted;

    }
}
