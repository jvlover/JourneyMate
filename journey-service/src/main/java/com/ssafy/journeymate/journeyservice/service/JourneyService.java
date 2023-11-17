package com.ssafy.journeymate.journeyservice.service;

import com.ssafy.journeymate.journeyservice.dto.request.JourneyModifyPutReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyRegistPostReq;
import com.ssafy.journeymate.journeyservice.dto.response.ItemGetRes;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import com.ssafy.journeymate.journeyservice.entity.Journey;
import java.util.List;

public interface JourneyService {

    JourneyGetRes registJourney(JourneyRegistPostReq journeyRegistPostReq);

    JourneyGetRes findByJourneyId(Long journeyId);

    List<JourneyGetRes> findByMateId(Long mateId);

    JourneyGetRes deleteJourney(Long journeyId);

    List<JourneyGetRes> deleteJourneysInMate(Long MateId);

    List<ItemGetRes> getItemsInCategory(Long categoryId);

    boolean modifyJourney(JourneyModifyPutReq journeyModifyPutReq);

}
