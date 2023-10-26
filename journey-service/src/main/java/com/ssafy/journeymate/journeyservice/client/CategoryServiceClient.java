package com.ssafy.journeymate.journeyservice.client;

import com.ssafy.journeymate.journeyservice.dto.response.CategoryGetRes;
import com.ssafy.journeymate.journeyservice.dto.response.ItemGetRes;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="category-service")
public interface CategoryServiceClient {

    @GetMapping("/category-service/{categoryId}")
    CategoryGetRes getCategory(@PathVariable int categoryId);

    @GetMapping("/category-service/{categoryId}/items")
    List<ItemGetRes> getCategoryItems(@PathVariable int categoryId);

}
