package com.ssafy.journeymate.mateservice.client;


import com.ssafy.journeymate.mateservice.dto.ResponseDto;
import com.ssafy.journeymate.mateservice.dto.request.client.MateBridgeRegistPostReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/user-service/findbyNickname/{nickname}")
    ResponseDto registMateBridge(@RequestBody MateBridgeRegistPostReq mateBridgeRegistPostReq);

    @GetMapping("/user-service/mateBridge/{mateId}")
    ResponseDto getMateBridgeUsers(@PathVariable Long mateId);

    @GetMapping("/user-service/findbyId/{id}")
    ResponseDto getUserInfo(@PathVariable String id);

}
