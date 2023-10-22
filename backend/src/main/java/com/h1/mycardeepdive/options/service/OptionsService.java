package com.h1.mycardeepdive.options.service;

import com.h1.mycardeepdive.exception.ErrorType;
import com.h1.mycardeepdive.exception.MyCarDeepDiveException;
import com.h1.mycardeepdive.options.domain.Options;
import com.h1.mycardeepdive.options.domain.Packages;
import com.h1.mycardeepdive.options.domain.repository.OptionsRepository;
import com.h1.mycardeepdive.options.domain.repository.PackageRepository;
import com.h1.mycardeepdive.options.mapper.OptionMapper;
import com.h1.mycardeepdive.options.ui.dto.*;
import com.h1.mycardeepdive.tags.domain.OptionTag;
import com.h1.mycardeepdive.tags.domain.Tags;
import com.h1.mycardeepdive.tags.domain.repository.OptionTagRepository;
import com.h1.mycardeepdive.tags.domain.repository.TagRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OptionsService {

    public static final String OPTION = "option";
    public static final String PACKAGE = "package";
    private final OptionsRepository optionsRepository;

    private final PackageRepository packageRepository;

    private final TagRepository tagRepository;

    private final OptionTagRepository optionTagRepository;

    public OptionResponse findAllAdditionalOptions(Long carSpecId) {
        List<Packages> packagesList = packageRepository.findPackageOptions(carSpecId);
        List<Options> additionalOptionsList =
                optionsRepository.findAdditionalOptions(carSpecId, packagesList);
        List<PackageOptionResponse> packageOptionResponses =
                packagesList.stream()
                        .map(
                                pkg ->
                                        OptionMapper.optionToPackageOptionResponse(
                                                pkg,
                                                tagRepository.findTagsByPackageId(pkg.getId()),
                                                optionsRepository.findOptionsByPackageId(
                                                        pkg.getId()),
                                                optionsRepository.findPackageImgUrlFromOption(
                                                        pkg.getId())))
                        .collect(Collectors.toList());
        List<AdditionalOptionResponse> additionalOptionResponses =
                additionalOptionsList.stream()
                        .map(
                                option ->
                                        OptionMapper.optionToAdditionalOptionResponse(
                                                option,
                                                tagRepository.findTagsByOptionId(option.getId())))
                        .collect(Collectors.toList());
        return new OptionResponse(packageOptionResponses, additionalOptionResponses);
    }

    public List<BasicOptionResponse> findAllBasicOptions(Long carSpecId) {
        return optionsRepository.findBasicOptions(carSpecId).stream()
                .map(
                        options ->
                                OptionMapper.optionToBasicOptionResponse(
                                        options, tagRepository.findTagsByOptionId(options.getId())))
                .collect(Collectors.toList());
    }

    public boolean userClickedOptionLog(Long optionId) {
        log.info(Markers.append(OPTION, optionId), OPTION);
        return true;
    }

    public boolean userClickedPackageLog(Long packageId) {
        log.info(Markers.append(PACKAGE, packageId), PACKAGE);
        return true;
    }

    public List<OptionDetailResponse> findPackageOptionDetail(Long optionId) {
        return optionsRepository.findOptionsByPackageId(optionId).stream()
                .map(
                        options ->
                                OptionMapper.optionToOptionDetailResponse(
                                        options, tagRepository.findTagsByOptionId(options.getId())))
                .collect(Collectors.toList());
    }

    public OptionDetailResponse findOptionDetail(Long optionId) {
        Options options =
                optionsRepository
                        .findById(optionId)
                        .orElseThrow(
                                () ->
                                        new MyCarDeepDiveException(
                                                HttpStatus.BAD_REQUEST,
                                                ErrorType.OPTION_NOT_FOUND));
        return OptionMapper.optionToOptionDetailResponse(
                options, tagRepository.findTagsByOptionId(options.getId()));
    }

    public OptionTagResponse findOptionTagDetail(Long tagId, Long carSpecId) {
        Tags tags =
                tagRepository
                        .findById(tagId)
                        .orElseThrow(
                                () ->
                                        new MyCarDeepDiveException(
                                                HttpStatus.BAD_REQUEST,
                                                ErrorType.OPTION_NOT_FOUND));
        List<Options> optionsList =
                optionsRepository.findOptionsByTagIdAndCarSpecId(tagId, carSpecId);
        Map<Long, OptionTag> optionTagMap =
                optionTagRepository.findOptionTagByOptionListAndTagId(optionsList, tagId);

        return new OptionTagResponse(
                tags.getImg_url(),
                optionsList.stream()
                        .map(
                                options -> {
                                    OptionTag optionTag = optionTagMap.get(options.getId());
                                    return OptionMapper.optionToOptionCoordinatesResponse(
                                            options, optionTag);
                                })
                        .collect(Collectors.toList()));
    }
}
