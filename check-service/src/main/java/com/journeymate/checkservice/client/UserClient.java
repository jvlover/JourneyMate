package com.journeymate.checkservice.client;

import com.journeymate.checkservice.dto.response.MateBridgeFindRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", path = "/user-service")
public interface UserClient {

    @GetMapping("/feign/{mateId}")
    MateBridgeFindRes findUserByMateIdForFeign(@PathVariable Long mateId);

}
