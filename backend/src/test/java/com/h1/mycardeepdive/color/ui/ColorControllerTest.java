package com.h1.mycardeepdive.color.ui;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.h1.mycardeepdive.color.mapper.ColorMapper.toExteriorColorInfo;
import static com.h1.mycardeepdive.color.mapper.ColorMapper.toInteriorColorInfo;
import static com.h1.mycardeepdive.fixture.ExteriorColorFixture.createExteriorColor;
import static com.h1.mycardeepdive.fixture.InteriorColorFixture.createInteriorColor;
import static com.h1.mycardeepdive.fixture.TrimFixture.createExclusiveTrim;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.h1.mycardeepdive.ControllerTestConfig;
import com.h1.mycardeepdive.color.domain.ExteriorColor;
import com.h1.mycardeepdive.color.domain.InteriorColor;
import com.h1.mycardeepdive.color.service.ColorService;
import com.h1.mycardeepdive.color.ui.dto.*;
import com.h1.mycardeepdive.trims.domain.Trim;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(ColorController.class)
class ColorControllerTest extends ControllerTestConfig {
    private static final String DEFAULT_URL = "/api/v1/color";

    @MockBean private ColorService colorService;

    @Test
    @DisplayName("사용가능한 외장컬러와 불가능한 외장컬러를 조회한다.")
    void getExteriorColors() throws Exception {
        // given
        Long trimId = 1L;
        Long interiorId = 1L;
        ExteriorColor exteriorColor = createExteriorColor();
        Trim trim = createExclusiveTrim();

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        info.add("trimId", String.valueOf(trimId));
        info.add("interiorColorId", String.valueOf(interiorId));

        ColorInfo colorInfo = toExteriorColorInfo(exteriorColor, trim);
        List<ColorInfo> availableColors = new ArrayList<>();
        List<ColorInfo> unavailableColors = new ArrayList<>();
        List<ColorInfo> otherTrimColors = new ArrayList<>();
        availableColors.add(colorInfo);
        unavailableColors.add(colorInfo);
        otherTrimColors.add(colorInfo);
        when(colorService.findExteriorColors(trimId, interiorId))
                .thenReturn(new ColorResponse(availableColors, unavailableColors, otherTrimColors));
        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get(
                                                DEFAULT_URL + "/exterior-colors")
                                        .params(info)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "외장",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("컬러")
                                                        .description("외장 컬러 조회")
                                                        .requestFields()
                                                        .requestParameters(
                                                                parameterWithName("trimId")
                                                                        .description("트림id"),
                                                                parameterWithName("interiorColorId")
                                                                        .description("내장색상id"))
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("사용가능한 내장컬러와 불가능한 내장컬러를 조회한다.")
    void getInteriorColors() throws Exception {
        // given
        Long trimId = 1L;
        Long exteriorId = 1L;
        InteriorColor interiorColor = createInteriorColor();
        Trim trim = createExclusiveTrim();

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        info.add("trimId", String.valueOf(trimId));
        info.add("exteriorColorId", String.valueOf(exteriorId));

        ColorInfo interiorColorInfo = toInteriorColorInfo(interiorColor, trim);
        List<ColorInfo> availableColors = new ArrayList<>();
        List<ColorInfo> unavailableColors = new ArrayList<>();
        List<ColorInfo> otherTrimColors = new ArrayList<>();
        availableColors.add(interiorColorInfo);
        unavailableColors.add(interiorColorInfo);
        otherTrimColors.add(interiorColorInfo);

        when(colorService.findInteriorColors(trimId, exteriorId))
                .thenReturn(new ColorResponse(availableColors, unavailableColors, otherTrimColors));
        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get(
                                                DEFAULT_URL + "/interior-colors")
                                        .params(info)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "내장",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("컬러")
                                                        .description("내장 컬러 조회")
                                                        .requestFields()
                                                        .requestParameters(
                                                                parameterWithName("trimId")
                                                                        .description("트림id"),
                                                                parameterWithName("exteriorColorId")
                                                                        .description("외장색상id"))
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("trimId로 외장색상과 내장색상을 조회한다.")
    void getAllColors() throws Exception {
        // given
        Long trimId = 1L;
        ExteriorColor exteriorColor = createExteriorColor();
        InteriorColor interiorColor = createInteriorColor();
        Trim trim = createExclusiveTrim();

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        info.add("trimId", String.valueOf(trimId));

        ColorInfo colorInfo = toExteriorColorInfo(exteriorColor, trim);
        List<ColorInfo> availableExteriorColors = new ArrayList<>();
        List<ColorInfo> unavailableExteriorColors = new ArrayList<>();
        List<ColorInfo> otherTrimExteriorColors = new ArrayList<>();
        availableExteriorColors.add(colorInfo);
        availableExteriorColors.add(colorInfo);
        availableExteriorColors.add(colorInfo);

        ColorInfo interiorColorInfo = toInteriorColorInfo(interiorColor, trim);
        List<ColorInfo> availableInteriorColors = new ArrayList<>();
        List<ColorInfo> unavailableInteriorColors = new ArrayList<>();
        List<ColorInfo> otherTrimInteriorColors = new ArrayList<>();
        availableInteriorColors.add(interiorColorInfo);
        unavailableInteriorColors.add(interiorColorInfo);
        otherTrimInteriorColors.add(interiorColorInfo);

        ColorResponse colorResponse =
                new ColorResponse(
                        availableExteriorColors,
                        unavailableExteriorColors,
                        otherTrimExteriorColors);
        ColorResponse interiorColorResponse =
                new ColorResponse(
                        availableInteriorColors,
                        unavailableInteriorColors,
                        otherTrimInteriorColors);
        when(colorService.findAllColors(trimId))
                .thenReturn(new AllColorResponse(colorResponse, interiorColorResponse));
        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get(DEFAULT_URL + "/trim-colors")
                                        .params(info)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "트림",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("컬러")
                                                        .description("트림의 모든 컬러 조회")
                                                        .requestFields()
                                                        .requestParameters(
                                                                parameterWithName("trimId")
                                                                        .description("트림id"))
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("외장컬러 로그 전송 기능에 성공한다.")
    @Test
    void clickExteriorColorLogTest() throws Exception {
        // given
        Long exteriorColorId = 1L;
        when(colorService.userClickedExteriorColorLog(exteriorColorId)).thenReturn(true);

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post(
                                                DEFAULT_URL
                                                        + "/exterior-colors/activity-log/{exterior-color-id}",
                                                exteriorColorId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "exterior-color-click",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("컬러")
                                                        .description("외장컬러 클릭 시, 로그 전송")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("내장컬러 로그 전송 기능에 성공한다.")
    @Test
    void clickInteriorColorLogTest() throws Exception {
        // given
        Long interiorColorId = 1L;
        when(colorService.userClickedInteriorColorLog(interiorColorId)).thenReturn(true);

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post(
                                                DEFAULT_URL
                                                        + "/interior-colors/activity-log/{interior-color-id}",
                                                interiorColorId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "interior-color-click",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("컬러")
                                                        .description("내장컬러 클릭 시, 로그 전송")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
