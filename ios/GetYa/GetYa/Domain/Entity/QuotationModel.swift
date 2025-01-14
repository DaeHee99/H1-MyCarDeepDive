//
//  Quotation.swift
//  GetYa
//
//  Created by 양승현 on 2023/08/23.
//

import Foundation

struct QuotationModel {
    let trimName: String
    let trimPrice: Int
    let carImgUrl: String
    let comment1: String
    let comment2: String
    let keyword: String
    let engineName: String
    let engineId: Int
    let drivingSystemName: String
    let drivingSystemId: Int
    let bodyName: String
    let bodyId: Int
    let carSpecId: Int
    let trimId: Int
    let exteriorColor: ColorInfoModel
    let interiorColor: ColorInfoModel
    let options: [QuotationOption]
    let packages: [QuotationOption]
    let totalPrice: Int
}
