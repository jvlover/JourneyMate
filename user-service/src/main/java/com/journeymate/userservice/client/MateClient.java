package com.journeymate.userservice.client;

import com.journeymate.userservice.dto.response.DocsListFindRes;
import com.journeymate.userservice.dto.response.MateFindRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mate-service")
public interface MateClient {

    @GetMapping("/mate-service/{mateId}")
    MateFindRes findMate(@PathVariable Long mateId);

    @GetMapping("/docs/list/{mateId}")
    DocsListFindRes findDocs(@PathVariable Long mateId);

}
