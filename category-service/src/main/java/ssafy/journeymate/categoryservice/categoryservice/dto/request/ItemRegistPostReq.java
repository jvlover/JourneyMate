package ssafy.journeymate.categoryservice.categoryservice.dto.request;


import lombok.Data;

@Data
public class ItemRegistPostReq {

    Long categoryId;
    String name;
    Integer num;

}
