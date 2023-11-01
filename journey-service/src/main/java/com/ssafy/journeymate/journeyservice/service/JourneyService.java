package com.ssafy.journeymate.journeyservice.service;

import com.ssafy.journeymate.journeyservice.dto.response.ItemGetRes;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import java.util.List;

public interface JourneyService {
    JourneyGetRes findByJourneyId(Long journeyId);

    List<JourneyGetRes> findByMateId(Long mateId);

    JourneyGetRes deleteJourney(Long journeyId);

    List<JourneyGetRes> deleteJourneysinMate(Long MateId);

    List<ItemGetRes> getItemsInCategory(Long categoryId);

}
