package com.h1.mycardeepdive.options.ui;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.h1.mycardeepdive.ControllerTestConfig;
import com.h1.mycardeepdive.options.service.OptionsService;
import com.h1.mycardeepdive.options.ui.dto.*;
import com.h1.mycardeepdive.tags.ui.TagResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(OptionsController.class)
class OptionsControllerTest extends ControllerTestConfig {
    private static final String DEFAULT_URL = "/api/v1";

    private TagResponse comfortTag = new TagResponse(1L, "사용편의");
    private TagResponse safeTag = new TagResponse(2L, "주행안전");
    @MockBean private OptionsService optionsService;

    @BeforeEach
    void setup() {}

    @DisplayName("차량 사양의 모든 추가 옵션을 조회에 성공한다.")
    @Test
    void getAllAdditionalOptions() throws Exception {
        // given
        Long carSpecId = 1L;
        when(optionsService.findAllAdditionalOptions(carSpecId))
                .thenReturn(
                        new OptionResponse(
                                List.of(
                                        new PackageOptionResponse(
                                                1L,
                                                "https://www.hyundai.co.kr/image/upload/asset_library/MDA00000000000000388/e435f2e0b5f246ccaa8ce260dac16c9b.jpg",
                                                10.12,
                                                "컴포트 II",
                                                "편의성을 위해 구성된 세트 옵션",
                                                List.of(comfortTag, safeTag),
                                                "None",
                                                10090000,
                                                List.of(1L, 2L))),
                                List.of(
                                        new AdditionalOptionResponse(
                                                1L,
                                                "https://img.etnews.com/photonews/2011/1352481_20201113164311_199_0001.jpg",
                                                5.5,
                                                "빌트인 캠(보조배터리 포함)",
                                                "차량 내부에 카메라를 설치하여 녹화가 가능한 블랙박스",
                                                List.of(comfortTag, safeTag),
                                                "H Genuine Accessories",
                                                109000))));

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get(
                                                DEFAULT_URL
                                                        + "/car-spec/{car-spec-id}/additional-options",
                                                carSpecId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "options-docs1",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("옵션")
                                                        .description("차량 사양에 따른 추가 옵션 목록 리스트 조회")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("차량 사양의 모든 기본 옵션을 조회에 성공한다.")
    @Test
    void getAllBasicOptions() throws Exception {
        // given
        Long carSpecId = 1L;
        when(optionsService.findAllBasicOptions(carSpecId))
                .thenReturn(
                        List.of(
                                new BasicOptionResponse(
                                        1L,
                                        "https://www.hyundai.co.kr/image/upload/asset_library/MDA00000000000000388/e435f2e0b5f246ccaa8ce260dac16c9b.jpg",
                                        "다중 충돌방지 자동 제동 시스템",
                                        List.of(comfortTag, safeTag))));

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get(
                                                DEFAULT_URL
                                                        + "/car-spec/{car-spec-id}/basic-options",
                                                carSpecId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "options-docs2",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("옵션")
                                                        .description("차량 사양에 따른 기본 옵션 목록 리스트 조회")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("옵션클릭 로그 전송 기능에 성공한다.")
    @Test
    void clickOptionLogTest() throws Exception {
        // given
        Long optionId = 1L;
        when(optionsService.userClickedOptionLog(optionId)).thenReturn(true);

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post(
                                                DEFAULT_URL + "/options/activity-log/{option-id}",
                                                optionId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "options-docs3",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("옵션")
                                                        .description("기본 또는 추가 옵션 클릭 시, 로그 전송")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("패키지옵션 로그 전송 기능에 성공한다.")
    @Test
    void clickPackageLogTest() throws Exception {
        // given
        Long optionId = 1L;
        when(optionsService.userClickedPackageLog(optionId)).thenReturn(true);

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.post(
                                                DEFAULT_URL
                                                        + "/package-options/activity-log/{option-id}",
                                                optionId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "options-docs4",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("옵션")
                                                        .description("패키지 옵션 클릭 시, 로그 전송")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("패키지 옵션에 포함된 옵션들의 상세 정보를 제공한다.")
    @Test
    void getPackageOptionDetailTest() throws Exception {
        // given
        Long optionId = 1L;
        when(optionsService.findPackageOptionDetail(optionId))
                .thenReturn(
                        List.of(
                                new OptionDetailResponse(
                                        1L,
                                        "빌트인 캠(보조배터리 포함)",
                                        "빌트인 적용된 영상기록장치로, 내비게이션 화면을 통해 영상 확인 및 앱 연동을 통해 영상 확인 및 SNS 공유가 가능합니다.",
                                        List.of(comfortTag, safeTag),
                                        109000,
                                        "option.img.url")));

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get(
                                                DEFAULT_URL
                                                        + "/options/package/{option-id}/details",
                                                optionId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "options-docs5",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("옵션")
                                                        .description(
                                                                "패키지 옵션에 포함된 옵션들의 상세 정보를 제공합니다.")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("기본 또는 추가 옵션의 상세 정보를 제공한다.")
    @Test
    void getOptionDetailTest() throws Exception {
        // given
        Long optionId = 1L;
        when(optionsService.findOptionDetail(optionId))
                .thenReturn(
                        new OptionDetailResponse(
                                1L,
                                "빌트인 캠(보조배터리 포함)",
                                "빌트인 적용된 영상기록장치로, 내비게이션 화면을 통해 영상 확인 및 앱 연동을 통해 영상 확인 및 SNS 공유가 가능합니다.",
                                List.of(comfortTag, safeTag),
                                109000,
                                "option.img.url"));

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get(
                                                DEFAULT_URL + "/options/{option-id}/details",
                                                optionId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "options-docs6",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("옵션")
                                                        .description("기본 또는 추가 옵션의 상세 옵션 정보를 제공한다.")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("옵션 태그의 상세 정보를 제공한다.")
    @Test
    void getOptionTagDetailTest() throws Exception {
        // given
        Long tagId = 1L;
        Long carSpecId = 2L;
        when(optionsService.findOptionTagDetail(tagId, carSpecId))
                .thenReturn(
                        new OptionTagResponse(
                                "https://www.hyundai.co.kr/image/upload/asset_library/MDA00000000000033027/bebeb59b7c7447f7be0a1f8238821cce.jpg",
                                List.of(
                                        new OptionCoordinatesResponse(
                                                1L,
                                                "빌트인 캠(보조배터리 포함)",
                                                "빌트인 적용된 영상기록장치로, 내비게이션 화면을 통해 영상 확인 및 앱 연동을 통해 영상 확인 및 SNS 공유가 가능합니다.",
                                                "https://img.etnews.com/photonews/2011/1352481_20201113164311_199_0001.jpg",
                                                109000,
                                                12.1,
                                                44.6))));

        // then
        ResultActions resultActions =
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get(
                                                DEFAULT_URL
                                                        + "/options/{car-spec-id}/tags/{tag-id}",
                                                carSpecId,
                                                tagId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(
                                MockMvcRestDocumentationWrapper.document(
                                        "options-docs7",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        resource(
                                                ResourceSnippetParameters.builder()
                                                        .tag("옵션")
                                                        .description("태그의 옵션 상세 정보를 제공한다.")
                                                        .requestFields()
                                                        .responseFields()
                                                        .build())));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
