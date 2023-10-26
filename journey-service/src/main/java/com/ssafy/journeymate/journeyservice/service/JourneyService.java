package com.ssafy.journeymate.journeyservice.service;

import com.ssafy.journeymate.journeyservice.dto.request.JourneyDeletePutReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyModifyReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyRegistPostReq;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

public interface JourneyService {
    JourneyGetRes findByJourneyId(Long journeyId);
    JourneyGetRes findByMateId(Long mateId);
    JourneyGetRes registJourney(JourneyRegistPostReq registInfo);
    boolean deleteJourney(JourneyDeletePutReq deleteInfo);
    boolean updateJourney(JourneyModifyReq modifyReq);


}
