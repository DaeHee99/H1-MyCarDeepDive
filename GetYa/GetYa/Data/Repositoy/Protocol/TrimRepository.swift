//
//  TrimRepository.swift
//  GetYa
//
//  Created by 양승현 on 2023/08/21.
//

import Foundation

protocol TrimRepository {
    func fetchTrimCarSpec(with requestDTO: TrimCarSpecRequestDTO) async throws -> [TrimCarSpec]
}
