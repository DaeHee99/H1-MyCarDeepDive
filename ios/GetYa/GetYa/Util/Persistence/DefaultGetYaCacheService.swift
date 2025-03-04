//
//  DefaultGetYaCacheService.swift
//  GetYa
//
//  Created by 양승현 on 2023/08/21.
//

import Foundation

class DefaultGetYaCacheService {
    enum CacheError: Error {
        case encodeError
    }
    
    static let shared = DefaultGetYaCacheService(name: "cache")
    
    private let memoryCache: MemoryCache
    private let diskCache: DiskCache
    
    init(name: String) {
        memoryCache = MemoryCache(name: name)
        diskCache = DiskCache(name: name)
    }
    
    func load(_ key: String) -> Data? {
        if let data = memoryCache.load(key) {
            return data
        } else if let data = diskCache.load(key) {
            memoryCache.write(key, data: data)
            return data
        }
        return nil
    }
    
    func write(_ key: String, data: Data) {
        memoryCache.write(key, data: data)
        diskCache.write(key, data: data)
    }
    
    func isExist(_ key: String) -> Bool {
        return memoryCache.isExist(key) || diskCache.isExist(key)
    }
    
    func removeAll() {
        memoryCache.removeAll()
        diskCache.removeAll()
    }
}
