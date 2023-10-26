package com.ssafy.journeymate.journeyservice.service;

import com.ssafy.journeymate.journeyservice.client.CategoryServiceClient;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyDeletePutReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyModifyReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyRegistPostReq;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import com.ssafy.journeymate.journeyservice.exception.JourneyNotFoundException;
import com.ssafy.journeymate.journeyservice.entity.Journey;
import com.ssafy.journeymate.journeyservice.repository.JourneyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JourneyServiceImpl implements JourneyService {

    JourneyRepository journeyRepository;
    CategoryServiceClient categoryServiceClient;

    @Autowired
    public JourneyServiceImpl(JourneyRepository journeyRepository, CategoryServiceClient categoryServiceClient) {
        this.journeyRepository = journeyRepository;
        this.categoryServiceClient = categoryServiceClient;
    }


    @Override
    public JourneyGetRes findByJourneyId(Long journeyId) {
        Journey journeyEntity = journeyRepository.findById(journeyId)
                .orElseThrow(JourneyNotFoundException::new);
        JourneyGetRes journeyGetRes =new ModelMapper().map(journeyEntity, JourneyGetRes.class);

        return journeyGetRes;
    }

    @Override
    public JourneyGetRes findByMateId(Long mateId) {
        Journey journeyEntity = journeyRepository.findByMateId(mateId)
                .orElseThrow(JourneyNotFoundException::new);
        JourneyGetRes journeyGetRes =new ModelMapper().map(journeyEntity, JourneyGetRes.class);

        return journeyGetRes;
    }


    @Override
    public JourneyGetRes registJourney(JourneyRegistPostReq registInfo) {

        ModelMapper mapper = new ModelMapper();
        Journey journeyEntity = mapper.map(registInfo, Journey.class);
        journeyRepository.save(journeyEntity);
        JourneyGetRes journeyGetRes =mapper.map(journeyEntity, JourneyGetRes.class);

        return journeyGetRes;
    }

    @Override
    public boolean deleteJourney(JourneyDeletePutReq deleteInfo) {
        return false;
    }

    @Override
    public boolean updateJourney(JourneyModifyReq modifyReq) {
        return false;
    }
}