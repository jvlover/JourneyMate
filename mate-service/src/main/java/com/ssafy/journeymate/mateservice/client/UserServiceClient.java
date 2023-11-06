package com.ssafy.journeymate.mateservice.client;


import com.ssafy.journeymate.mateservice.dto.request.client.MateBridgeModifyReq;
import com.ssafy.journeymate.mateservice.dto.request.client.MateBridgeRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.response.client.FindUserRes;
import com.ssafy.journeymate.mateservice.dto.response.client.MateBridgeRes;
import com.ssafy.journeymate.mateservice.dto.response.client.MateBridgeUsersRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/user-service/mateBridge")
    MateBridgeRes registMateBridge(@RequestBody MateBridgeRegistPostReq mateBridgeRegistPostReq);

    @GetMapping("/user-service/mateBridge/{mateId}")
    MateBridgeUsersRes getMateBridgeUsers(@PathVariable Long mateId);

    @GetMapping("/user-service//findById/{id}")
    FindUserRes getUserInfo(@PathVariable String id);

    @PutMapping("/user-service/mateBridge")
    MateBridgeRes modifyMateBridge(@RequestBody MateBridgeModifyReq mateBridgeModifyReq);


}
