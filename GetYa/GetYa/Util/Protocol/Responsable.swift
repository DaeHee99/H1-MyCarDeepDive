//
//  Responsable.swift
//  GetYa
//
//  Created by 양승현 on 2023/08/21.
//

import Foundation

protocol Responsable {
    associatedtype ResponseDTO: Decodable
}

enum ResponseType {
    case carRecommendation
    case carRecommendationCustom
    case carSpec
    case carSpecTrims
    case carSpecActivityLog(tirmID: Int)
    case carSpecComparison
    case exteriorColor
    case interiorColor
    case trimColor
    case carSpecAdditionalOption(carSpecId: Int)
    case carSpecBasicOptions(carSpecId: Int)
    case optionsActivityLog(optionId: Int)
    case optionsDetails(optionId: Int)
    case packageOptionsActivityLog(optionId: Int)
    case optionPackageDetails(optionId: Int)
    case optionCarSpecIdTagsTagId(carSpecId: Int, tagId: Int)
    case pdfID
    case pdfCarInfomation(pdfID: String)
    case pdfEmail(pdfEmail: PdfEmail)
    case pdfURL(pdfID: String)
    
    var path: String {
        switch self {
        case .carRecommendation:
            return "car-recommendation"
        case .carRecommendationCustom:
            return "car-recommendation/custom"
        case .carSpec:
            return "car-spec"
        case .carSpecTrims:
            return "car-spec/trims"
        case .carSpecActivityLog(let trimID):
            return "car-spec/activity-log/\(trimID)"
        case .carSpecAdditionalOption(let carSpecId):
            return "car-spec/\(carSpecId)/additional-options"
        case .pdfID:
            return "pdfId"
        case .pdfCarInfomation(let pdfID):
            return "pdf/\(pdfID)/car-information"
        case .pdfEmail(let pdfEmail):
            return "pdf/\(pdfEmail.pdfID)/email/\(pdfEmail.emailName)"
        case .pdfURL(let pdfID):
            return "pdf/\(pdfID)"
        default:
            break
        }
        return ""
    }
}