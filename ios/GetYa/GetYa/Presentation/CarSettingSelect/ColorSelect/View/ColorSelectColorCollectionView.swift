//
//  ColorSelectColorCollectionView.swift
//  GetYa
//
//  Created by 배남석 on 2023/08/15.
//

import UIKit

protocol ColorSelectColorDelegate: AnyObject {
    func touchUpColorCell(index: Int, isAvailable: Bool)
}

class ColorSelectColorCollectionView: UICollectionView {
    enum Constatns {
        static let spacing: CGFloat = .toScaledWidth(value: 12)
        static let cellHeight: CGFloat = .toScaledHeight(value: 76.5)
        static let cellWidth: CGFloat = .toScaledHeight(value: 64)
        static let inset: UIEdgeInsets = UIEdgeInsets(
            top: 0,
            left: .toScaledWidth(value: 16),
            bottom: 0,
            right: .toScaledWidth(value: 16))
    }
    
    // MARK: - UI properties
    
    // MARK: - Properties
    private var colorType: ColorType = .exterior
    weak var colorSelectDelegate: ColorSelectColorDelegate?
    private var availableColorArray: [Color] = []
    private var unAvailableColorArray: [Color] = []
    private var selectedIndexPath: IndexPath?
    
    // MARK: - Lifecycles
    convenience init() {
        self.init(
            frame: .zero,
            collectionViewLayout: UICollectionViewFlowLayout().set {
                $0.minimumLineSpacing = Constatns.spacing
                $0.itemSize = CGSize(width: Constatns.cellWidth, height: Constatns.cellHeight)
                $0.scrollDirection = .horizontal
            })
    }
    
    override init(frame: CGRect, collectionViewLayout layout: UICollectionViewLayout) {
        super.init(frame: frame, collectionViewLayout: layout)
        
        configureUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        
        configureUI()
    }
    
    // MARK: - Private Functions
    private func configureUI() {
        translatesAutoresizingMaskIntoConstraints = false
        delegate = self
        dataSource = self
        register(
            ColorSelectColorCell.self,
            forCellWithReuseIdentifier: ColorSelectColorCell.identifier)
        showsHorizontalScrollIndicator = false
        contentInset = Constatns.inset
    }
    
    // MARK: - Functions
    func setSelectedIndexPath(index: Int) {
        selectedIndexPath = [0, index]
    }
    
    func setUnAvailableColorArray(colorArray: [Color]) {
        unAvailableColorArray = colorArray
    }
    
    func setAvailableColorArray(colorArray: [Color]) {
        availableColorArray = colorArray
    }
    
    func setColorType(type: ColorType) {
        colorType = type
    }
    
    // MARK: - Objc Functions
}

// MARK: - UICollectionViewDelegate
extension ColorSelectColorCollectionView: UICollectionViewDelegate {
    func collectionView(
        _ collectionView: UICollectionView,
        didSelectItemAt indexPath: IndexPath
    ) {
        if selectedIndexPath != indexPath {
            let color = indexPath.row < availableColorArray.count
            ? availableColorArray[indexPath.row]
            : unAvailableColorArray[indexPath.row - availableColorArray.count]
            
            NotificationCenter.default.post(
                name: NSNotification.Name("touchColorCellNotification"),
                object: nil,
                userInfo: ["color": color, "colorType": colorType])
        }
    }
}

// MARK: - UICollectionViewDataSource
extension ColorSelectColorCollectionView: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return availableColorArray.count + unAvailableColorArray.count
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: ColorSelectColorCell.identifier,
            for: indexPath) as? ColorSelectColorCell
        else { return UICollectionViewCell() }
        
        if indexPath.row < availableColorArray.count {
            let color = availableColorArray[indexPath.row]
            cell.setImageURL(imageURL: color.imageURL, isAvailable: true)
        } else {
            let color = unAvailableColorArray[indexPath.row - availableColorArray.count]
            cell.setImageURL(imageURL: color.imageURL, isAvailable: false)
        }
        
        switch colorType {
        case .exterior:
            if indexPath.row < 3 {
                cell.setExteriorTagViewIsHidden(number: indexPath.row + 1, isHidden: false)
            }
        case .interior:
            if indexPath.row == 0 {
                cell.setInteriorTagViewIsHidden(isHidden: false)
            }
        }
        cell.setSelectedImageViewIsHidden(isHidden: selectedIndexPath != indexPath ? true : false)
        
        return cell
    }
}
