package com.h1.mycardeepdive.car.service;

import static com.h1.mycardeepdive.car.mapper.CarSpecMapper.toCarSpecComparisonResponse;
import static com.h1.mycardeepdive.car.mapper.CarSpecMapper.toCarSpecResponse;

import com.h1.mycardeepdive.car.domain.CarSpec;
import com.h1.mycardeepdive.car.domain.repository.CarSpecRepository;
import com.h1.mycardeepdive.car.ui.dto.CarSpecComparisonResponse;
import com.h1.mycardeepdive.car.ui.dto.CarSpecDto;
import com.h1.mycardeepdive.car.ui.dto.CarSpecResponse;
import com.h1.mycardeepdive.trims.domain.Trim;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CarSpecService {

    private final CarSpecRepository carSpecRepository;

    public CarSpecResponse findCarSpecsBySpec(Long engineId, Long bodyId, Long drivingSystemId) {
        List<CarSpec> carSpecs =
                carSpecRepository.findByEngineIdAndBodyIdAndDrivingSystemId(
                        engineId, bodyId, drivingSystemId);
        List<CarSpecDto> carSpecDtos = new ArrayList<>();
        for (CarSpec carSpec : carSpecs) {
            Trim trim = carSpec.getTrim();
            CarSpecDto carSpecDto =
                    toCarSpecResponse(
                            carSpec,
                            getBasicOptionNames(trim.getId()),
                            getBasicOptionIds(trim.getId()));
            carSpecDtos.add(carSpecDto);
        }
        return new CarSpecResponse(carSpecDtos, 2L);
    }

    public List<CarSpecComparisonResponse> findCarSpecComparisonsBySpec(
            Long engineId, Long bodyId, Long drivingSystemId) {
        List<CarSpec> carSpecs =
                carSpecRepository.findByEngineIdAndBodyIdAndDrivingSystemId(
                        engineId, bodyId, drivingSystemId);
        List<CarSpecComparisonResponse> carSpecComparisonResponses = new ArrayList<>();
        for (CarSpec carSpec : carSpecs) {
            Trim trim = carSpec.getTrim();
            CarSpecComparisonResponse carSpecComparisonResponse =
                    toCarSpecComparisonResponse(
                            carSpec,
                            getBasicOptionNames(trim.getId()),
                            getBasicOptionIds(trim.getId()));
            carSpecComparisonResponses.add(carSpecComparisonResponse);
        }
        return carSpecComparisonResponses;
    }

    private List<String> getBasicOptionNames(Long trimId) {
        if (trimId == 1) {
            return List.of("전방 충돌 방지 보조", "내비 기반 크루즈 컨트롤", "세이프티 파워 윈도우");
        } else if (trimId == 2) {
            return List.of("퀼팅 천연가죽 시트", "12인치 클러스터", "서라운드 뷰 모니터");
        } else if (trimId == 3) {
            return List.of("2열 통풍시트", "스마트 자세제어", "2열 수동식 도어 커튼");
        } else {
            return List.of("20인치 캘리그라피 전용 휠", "KRELL 프리미엄 사운드", "원격 스마트 주차 보조");
        }
    }

    private List<Long> getBasicOptionIds(Long trimId) {
        if (trimId == 1) {
            return List.of(10L, 16L, 108L);
        } else if (trimId == 2) {
            return List.of(80L, 59L, 113L);
        } else if (trimId == 3) {
            return List.of(129L, 86L, 116L);
        } else {
            return List.of(52L, 127L, 27L);
        }
    }

    public boolean userClickedTrimLog(Long trimId) {
        return true;
    }
}
