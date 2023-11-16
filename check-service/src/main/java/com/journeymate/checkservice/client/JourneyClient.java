package com.journeymate.checkservice.client;

import com.journeymate.checkservice.dto.response.JourneyFindRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "journey-service")
public interface JourneyClient {

    @GetMapping("journey-service/{mateId}")
    JourneyFindRes findJourneyByMateId(@PathVariable Long mateId);
}
