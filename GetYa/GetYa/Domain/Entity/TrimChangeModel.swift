//
//  TrimChangeModel.swift
//  GetYa
//
//  Created by 배남석 on 2023/08/25.
//

import Foundation

struct TrimChangeModel {
    let trimSelectModel: TrimSelectModel?
    let otherTrimSelectModel: TrimSelectModel?
    let exteriorColorSelectModel: ColorSelectModel?
    let interiorColorSelectModel: ColorSelectModel?
    let optionSelectModel: AdditionalOptionSelectModel?
    
    init(
        trimSelectModel: TrimSelectModel? = nil,
        otherTrimSelectModel: TrimSelectModel? = nil,
        exteriorColorSelectModel: ColorSelectModel? = nil,
        interiorColorSelectModel: ColorSelectModel? = nil,
        optionSelectModel: AdditionalOptionSelectModel? = nil) {
        self.trimSelectModel = trimSelectModel
        self.otherTrimSelectModel = otherTrimSelectModel
        self.exteriorColorSelectModel = exteriorColorSelectModel
        self.interiorColorSelectModel = interiorColorSelectModel
        self.optionSelectModel = optionSelectModel
    }
}
