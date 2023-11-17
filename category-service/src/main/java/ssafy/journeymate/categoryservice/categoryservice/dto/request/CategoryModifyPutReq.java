package ssafy.journeymate.categoryservice.categoryservice.dto.request;


import lombok.Data;

@Data
public class CategoryModifyPutReq {

    Long id;
    String name;
    String icon;

}
