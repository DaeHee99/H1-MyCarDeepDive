//
//  DefaultQuotationRepository.swift
//  GetYa
//
//  Created by 양승현 on 2023/08/21.
//

import Foundation

struct DefaultQuotationRepository: QuotationRepository {
    typealias Endpoints = QuotationEndpoint
    let provider = SessionProvider()
    
    func fetchCarDetailRecommendation(
        with requestDTO: CustomRecomendationDTO
    ) async throws -> Quotation {
        let endpoint = Endpoints.shared.fetchCarDetailRecommendationOption(with: requestDTO)
        let commonDTO = try await provider.request(endpoint: endpoint)
        return commonDTO.data.toDomain()
    }
}
