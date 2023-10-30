package com.ssafy.journeymate.mateservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/user-service/findbyNickname/{nickname}")
    

}
