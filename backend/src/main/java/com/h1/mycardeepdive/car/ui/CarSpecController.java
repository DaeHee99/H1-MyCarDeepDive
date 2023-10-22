package com.h1.mycardeepdive.car.ui;

import com.h1.mycardeepdive.car.service.CarSpecService;
import com.h1.mycardeepdive.car.ui.dto.CarSpecComparisonResponse;
import com.h1.mycardeepdive.car.ui.dto.CarSpecResponse;
import com.h1.mycardeepdive.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/car-spec")
public class CarSpecController {
    private final CarSpecService carSpecService;

    @GetMapping
    public ApiResponse<CarSpecResponse> getTrimsBySpec(
            @RequestParam("engineId") Long engineId,
            @RequestParam("bodyId") Long bodyId,
            @RequestParam("drivingSystemId") Long drivingSystemId) {
        CarSpecResponse carSpecResponse =
                carSpecService.findCarSpecsBySpec(engineId, bodyId, drivingSystemId);
        return new ApiResponse<>(carSpecResponse);
    }

    @GetMapping("/trims")
    public ApiResponse<CarSpecResponse> getTrimsByCarSpecId(
            @RequestParam("carSpecId") Long carSpecId) {
        CarSpecResponse carSpecResponse = carSpecService.findCarSpecsByCarSpecId(carSpecId);
        return new ApiResponse<>(carSpecResponse);
    }

    @GetMapping("/comparison")
    public ApiResponse<List<CarSpecComparisonResponse>> getComparison(
            @RequestParam("engineId") Long engineId,
            @RequestParam("bodyId") Long bodyId,
            @RequestParam("drivingSystemId") Long drivingSystemId) {
        List<CarSpecComparisonResponse> carSpecComparisons =
                carSpecService.findCarSpecComparisonsBySpec(engineId, bodyId, drivingSystemId);
        return new ApiResponse<>(carSpecComparisons);
    }

    @PostMapping("/activity-log/{trim-id}")
    public ApiResponse<Boolean> userClickedTrimLog(@PathVariable("trim-id") Long trimId) {
        return new ApiResponse<>(carSpecService.userClickedTrimLog(trimId));
    }
}
