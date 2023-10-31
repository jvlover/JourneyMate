package ssafy.journeymate.categoryservice.categoryservice.dto.request;


import lombok.Data;

@Data
public class CategoryUpdatePutReq {

    Integer id;
    String name;
    String icon;

}
