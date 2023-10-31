package ssafy.journeymate.categoryservice.categoryservice.dto.request;


import lombok.Data;

@Data
public class ItemUpatePutReq {

    Long id;
    Long categoryId;
    String name;
    Integer num;

}
