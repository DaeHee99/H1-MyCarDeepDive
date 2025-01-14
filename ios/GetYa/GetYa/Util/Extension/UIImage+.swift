//
//  UIImage+.swift
//  GetYa
//
//  Created by 양승현 on 2023/08/20.
//

import UIKit

extension UIImage {
    var thumbnail: UIImage? {
        get async {
            let size = CGSize(width: 50, height: 50)
            return await self.byPreparingThumbnail(ofSize: size)
        }
    }
    
    func resize(targetSize: CGSize, opaque: Bool = false) -> UIImage? {
        UIGraphicsBeginImageContextWithOptions(targetSize, opaque, 0)
        guard let context = UIGraphicsGetCurrentContext() else { return nil }
        context.interpolationQuality = .high
        let newRect = CGRect(x: 0, y: 0, width: targetSize.width, height: targetSize.height)
        draw(in: newRect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return newImage
    }
}
