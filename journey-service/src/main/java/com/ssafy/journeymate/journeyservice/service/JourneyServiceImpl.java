package com.ssafy.journeymate.journeyservice.service;

import com.ssafy.journeymate.journeyservice.dto.request.JourneyDeletePutReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyModifyReq;
import com.ssafy.journeymate.journeyservice.dto.request.JourneyRegistPostReq;
import com.ssafy.journeymate.journeyservice.dto.response.JourneyGetRes;
import com.ssafy.journeymate.journeyservice.exception.JourneyNotFoundException;
import com.ssafy.journeymate.journeyservice.jpa.JourneyEntity;
import com.ssafy.journeymate.journeyservice.jpa.JourneyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JourneyServiceImpl implements JourneyService {

    JourneyRepository journeyRepository;

    @Autowired
    public JourneyServiceImpl(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }


    @Override
    public JourneyGetRes findJourney(Long journeyId) throws NotFoundException {
        JourneyEntity journeyEntity = journeyRepository.findById(journeyId)
                .orElseThrow(JourneyNotFoundException::new);
        JourneyGetRes journeyGetRes =new ModelMapper().map(journeyEntity, JourneyGetRes.class);

        return journeyGetRes;
    }

    @Override
    public JourneyGetRes registJourney(JourneyRegistPostReq registInfo) {

        ModelMapper mapper = new ModelMapper();
        JourneyEntity journeyEntity = mapper.map(registInfo, JourneyEntity.class);
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