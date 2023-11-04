package com.journeymate.checkservice.client;

import com.journeymate.checkservice.dto.response.MateBridgeFindRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("user-service/mateBridge/{mateId}")
    MateBridgeFindRes findUserByMateId(@PathVariable Long mateId);

}
