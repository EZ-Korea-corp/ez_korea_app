package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import com.ezkorea.hybrid_app.service.aws.AWSService;
import com.ezkorea.hybrid_app.web.dto.GasStationDto;
import com.ezkorea.hybrid_app.web.exception.GasStationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasStationService {
    private final GasStationRepository gsRepository;
    private final SaleMbRepository saleMbRepository;
    private final AWSService awsService;

    private final ModelMapper mapper;

    public GasStation findByStationName(String name) {
        return gsRepository.findByStationName(name)
                .orElseThrow(() -> new GasStationNotFoundException(name + "를 찾을 수 없습니다."));
    }

    public List<GasStation> findAllGasStation() {
        return gsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public GasStation findStationById(Long id) {
        return gsRepository.findById(id)
                .orElseThrow( () -> new GasStationNotFoundException("해당 주유소를 찾을 수 없습니다."));
    }

    @Transactional
    public long saveGasStation(GasStationDto dto) {
        GasStation entity = null;

        if(dto.getId() == null) {
            entity = gsRepository.save(mapper.map(dto, GasStation.class));
        } else {
            entity = gsRepository.findById(dto.getId()).orElseThrow(() ->
                    new GasStationNotFoundException("해당 주유소를 찾을 수 없습니다.")
            );

            entity.setStationName(dto.getStationName());
            entity.setStationLocation(dto.getStationLocation());
            entity.setMemo(dto.getMemo());
        }

        return entity.getId();
    }

    @Transactional
    public GasStation updateGasStation(Long id, GasStationDto dto) {
        GasStation currentStation = findStationById(id);
        awsService.deleteImages(currentStation.getImageList());
        currentStation.setBasicInfo(dto);
        return currentStation;
    }

    public void deleteGasStation(GasStation station) {
        awsService.deleteImages(station.getImageList());
        gsRepository.delete(station);
    }
}
