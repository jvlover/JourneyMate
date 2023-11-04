package com.journeymate.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "journey-service")
public interface JourneyClient {

//    @GetMapping("/journey-service/mateBridge/{mateId}")
//    MateBridgeFindRes findUserByMateId(@PathVariable Long mateId);

}
