//
//  DetailQuotationPreviewViewModel.swift
//  GetYa
//
//  Created by 양승현 on 2023/08/09.
//

import Foundation
import Combine

// MARK: - View Model
struct QuotationPreviewCarInfoModel {
    var carName: String
    var trimName: String
    var carPrice: String
    var carOptions: String
}

struct QuotationPreviewMainHeaderModel {
    var thumbnailKeywords: [String]
    var recommendCarProductOption: QuotationPreviewCarInfoModel
    var firstSectionTitle: String
    var thumbnailUrl: String
    
    init(
        thumbnailKeywords: [String] = [],
        recommendCarProductOption: QuotationPreviewCarInfoModel = .init(
            carName: "",
            trimName: "",
            carPrice: "",
            carOptions: ""),
        firstSectionTitle: String = "",
        thumbnailUrl: String = ""
    ) {
        self.thumbnailKeywords = thumbnailKeywords
        self.recommendCarProductOption = recommendCarProductOption
        self.firstSectionTitle = firstSectionTitle
        self.thumbnailUrl = thumbnailUrl
    }
}

final class DetailQuotationPreviewViewModel: CommonQuotationPreviewTableViewModel {
    // MARK: - Input
    struct DetailQuotationPreviewInput {
        let viewDidLoadEvent: AnyPublisher<Void, Never>
        let customButtonEvent: AnyPublisher<Void, Never>
        let quickQuoteEvent: AnyPublisher<Void, Never>
    }
    
    // MARK: - Output state
    enum DetailQuotationPreviewState {
        case none
        case updateDetailQuotationPreview
        case gotoCustomPage(TrimSubOptionSelect)
        case gotoCompletionPage(ContractionQuotation)
    }
    
    // MARK: - Dependencies
    private let quotationUseCase: QuotationUseCase!
    
    // MARK: - Properties
    private var mainSectionHeader = QuotationPreviewMainHeaderModel()
    private var sectionHeaders: [String] = []
    private var secondSectionFooter: String = "데이터 불러오는 중입니다."
    private var trimCarSpec = TrimSubOptionSelect(
        engineID: 1,
        bodyID: 1,
        drivingSystemID: 1)
    private var contractionQuotation = ContractionQuotation(
        carSpecID: 1,
        trimID: 1,
        exteriorColorID: 1,
        interiorColorID: 1,
        additionalOptionIDList: [])
    private var subscriptions = Set<AnyCancellable>()

    // MARK: - Lifecycles
    init(keywords: [String], quotationUseCase: QuotationUseCase) {
        self.quotationUseCase = quotationUseCase
        mainSectionHeader.thumbnailKeywords = keywords
        sectionHeaders = QuotationPreviewHeaderTitleList.lists
        super.init(dataSource: [[]])
    }
}

// MARK: - CharacterSelectSuccessViewModelable
extension DetailQuotationPreviewViewModel: DetailQuotationPreviewViewModelable {
    func transform(input: Input) -> Output {
        return Publishers.MergeMany(
            customButtonEventChains(input),
            quickQuoteEventChains(input),
            viewDidLoadChains(input),
            updateQuotationPreview()
        ).eraseToAnyPublisher()
    }
}

// MARK: - CharacterSelectSuccessViewModelable private function
private extension DetailQuotationPreviewViewModel {
    
    func updateQuotationPreview() -> Output {
        return quotationUseCase.carQuotation.map { [weak self] quotation -> State in
            let carOptions = [
                quotation.engineName,
                quotation.drivingSystemName,
                quotation.bodyName
            ].joined(separator: " ・ ")
            let exteriorColor = quotation.exteriorColor
            let interiorColor = quotation.interiorColor
            self?.trimCarSpec = TrimSubOptionSelect(
                engineID: quotation.engineID,
                bodyID: quotation.bodyID,
                drivingSystemID: quotation.drivingSystemID)
            self?.contractionQuotation = ContractionQuotation(
                carSpecID: quotation.carSpecID,
                trimID: quotation.trimID,
                exteriorColorID: exteriorColor.colorID,
                interiorColorID: interiorColor.colorID,
                additionalOptionIDList: [])
            
            let recommendCarProductOption = QuotationPreviewCarInfoModel(
                carName: "펠리세이드",
                trimName: quotation.trimName,
                carPrice: quotation.trimPrice.toPriceFormat+"원",
                carOptions: carOptions)
                self?.mainSectionHeader.recommendCarProductOption = recommendCarProductOption
                self?.mainSectionHeader.firstSectionTitle = "색상"
                self?.mainSectionHeader.thumbnailUrl = quotation.carImageURL
                self?.secondSectionFooter = quotation.totalPrice.toPriceFormat+"원"
            let optionList: [[QuotationOption]] = [
                [.init(optionID: exteriorColor.colorID,
                       optionName: exteriorColor.colorName,
                       optionImageURL: exteriorColor.colorImageURL,
                       price: exteriorColor.price,
                       comment: exteriorColor.comment),
                 .init(optionID: interiorColor.colorID,
                       optionName: interiorColor.colorName,
                       optionImageURL: interiorColor.colorImageURL,
                       price: interiorColor.price,
                       comment: interiorColor.comment)],
                quotation.options,
                quotation.packages]
                self?.setDataSource(with: optionList)
            return .updateDetailQuotationPreview
        }.eraseToAnyPublisher()
    }
    
    func customButtonEventChains(_ input: Input) -> Output {
        return input.customButtonEvent
            .map { [weak self] _ -> State in
                guard let self else { return .none }
                return .gotoCustomPage(trimCarSpec)
            }
            .eraseToAnyPublisher()
    }
    
    func quickQuoteEventChains(_ input: Input) -> Output {
        return input.quickQuoteEvent
            .map { [weak self] _ -> State in
                guard let self else { return .none }
                return .gotoCompletionPage(contractionQuotation)
            }
            .eraseToAnyPublisher()
    }
    
    func viewDidLoadChains(_ input: Input) -> Output {
        return input.viewDidLoadEvent
            .map { [weak self] _ -> State in
                self?.quotationUseCase.fetchCarQuotation()
                return .none
            }
            .eraseToAnyPublisher()
    }
}

// MARK: - CharacterSSTableViewAdapterDataSource
extension DetailQuotationPreviewViewModel: DetailQuotationPreviewAdapterDataSource {
    var lastSectionHeaderItem: String {
        sectionHeaders[2]
    }
    
    var lastSectionFooterItem: String {
        secondSectionFooter
    }
    
    var mainSectionHeaderItem: QuotationPreviewMainHeaderModel {
        mainSectionHeader
    }
    
    var secondSectionHeaderItem: String {
        sectionHeaders[1]
    }
}
