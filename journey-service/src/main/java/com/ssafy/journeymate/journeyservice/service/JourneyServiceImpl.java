package com.ssafy.journeymate.journeyservice.service;

import com.ssafy.journeymate.journeyservice.client.CategoryServiceClient;
import com.ssafy.journeymate.journeyservice.dto.response.ItemGetRes;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import com.ssafy.journeymate.journeyservice.exception.JourneyNotFoundException;
import com.ssafy.journeymate.journeyservice.entity.Journey;
import com.ssafy.journeymate.journeyservice.repository.JourneyRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JourneyServiceImpl implements JourneyService {

    private JourneyRepository journeyRepository;
    private CategoryServiceClient categoryServiceClient;
    private CircuitBreakerFactory circuitBreakerFactory;


    @Autowired
    public JourneyServiceImpl(JourneyRepository journeyRepository, CategoryServiceClient categoryServiceClient,
                              CircuitBreakerFactory circuitBreakerFactory) {
        this.journeyRepository = journeyRepository;
        this.categoryServiceClient = categoryServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }


    @Override
    public JourneyGetRes findByJourneyId(Long journeyId) {

        log.info("JourneyService_findByJourneyId_start: " + journeyId);

        Journey journeyEntity = journeyRepository.findById(journeyId)
                .orElseThrow(JourneyNotFoundException::new);
        JourneyGetRes journeyGetRes = new ModelMapper().map(journeyEntity, JourneyGetRes.class);

        log.info("JourneyService_findByJourneyId_end");

        return journeyGetRes;
    }

    @Override
    public List<JourneyGetRes> findByMateId(Long mateId) {

        log.info("JourneyService_findByMateId_start: " + mateId);

        List<Journey> journeys = journeyRepository.findAllByMateId(mateId);
        List<JourneyGetRes> journeyGetResponses = new ArrayList<>();
        for (Journey journey : journeys) {
            if (journey.getIsDeleted() == 0) {
                JourneyGetRes journeyGetRes = new ModelMapper().map(journey, JourneyGetRes.class);
                journeyGetResponses.add(journeyGetRes);
            }
        }

        log.info("JourneyService_findByMateId_end");

        return journeyGetResponses;
    }

    @Override
    public JourneyGetRes deleteJourney(Long journeyId) {

        log.info("JourneyService_deleteJourney_start: " + journeyId);

        Journey journeyEntity = journeyRepository.findById(journeyId)
                .orElseThrow(JourneyNotFoundException::new);
        journeyEntity.deleteJourney();
        JourneyGetRes journeyGetRes = new ModelMapper().map(journeyEntity, JourneyGetRes.class);

        log.info("JourneyService_deleteJourney_end");

        return journeyGetRes;
    }

    @Override
    public List<JourneyGetRes> deleteJourneysinMate(Long mateId) {

        log.info("JourneyService_deleteJourneysinMate_start: " + mateId);

        List<Journey> journeys = journeyRepository.findAllByMateId(mateId);
        List<JourneyGetRes> journeyGetResponses = new ArrayList<>();
        for (Journey journey : journeys) {
            if (journey.getIsDeleted() == 0) {
                journey.deleteJourney();
                JourneyGetRes journeyGetRes = new ModelMapper().map(journey, JourneyGetRes.class);
                journeyGetResponses.add(journeyGetRes);
            }
        }

        log.info("JourneyService_deleteJourneysinMate_end");

        return journeyGetResponses;
    }

    @Override
    public List<ItemGetRes> getItemsInCategory(Long categoryId) {

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("journey-category-circuitbreaker");
        List<ItemGetRes> items = circuitBreaker.run(() -> categoryServiceClient.getCategoryItems(categoryId),
                throwable -> new ArrayList<>());

        return items;
    }


}