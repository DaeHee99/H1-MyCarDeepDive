//
//  TrimOptionDetailOptionCollectionView.swift
//  GetYa
//
//  Created by 배남석 on 2023/08/12.
//

import UIKit

class TrimOptionDetailOptionCollectionView: UICollectionView {
    enum Constants {
        enum Cell {
            static let spacing = CGFloat(8).scaledWidth
            static let width = CGFloat(95).scaledWidth
            static let height = CGFloat(124).scaledHeight
        }
    }
    // MARK: - UI properties
    
    // MARK: - Properties
    private(set) var optionImageURLArray: [String] = []
    private(set) var optionDescriptionTexts: [String] = []
    
    // MARK: - Lifecycles
    init(optionImageURLArray: [String], optionDescriptionTexts: [String]) {
        super.init(frame: .zero, collectionViewLayout: UICollectionViewFlowLayout())
        
        configureUI()
        setOptionImageURLArray(optionImageURLArray: optionImageURLArray)
        setDescriptionTexts(texts: optionDescriptionTexts)
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
        self.translatesAutoresizingMaskIntoConstraints = false
        self.dataSource = self
        self.delegate = self
        self.register(
            TrimOptionDetailOptionCell.self,
            forCellWithReuseIdentifier: TrimOptionDetailOptionCell.identifier)
    }
    
    // MARK: - Functions
    func setOptionImageURLArray(optionImageURLArray: [String]) {
        self.optionImageURLArray = optionImageURLArray
    }
    
    func setDescriptionTexts(texts: [String]) {
        optionDescriptionTexts = texts
    }
    
    // MARK: - Objc Functions

}

// MARK: - UICollectionView Delegate
extension TrimOptionDetailOptionCollectionView: UICollectionViewDelegate {
    
}

// MARK: - UICollectionView DataSource
extension TrimOptionDetailOptionCollectionView: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return optionImageURLArray.count
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: TrimOptionDetailOptionCell.identifier,
            for: indexPath
        ) as? TrimOptionDetailOptionCell else {
            return UICollectionViewCell()
        }
        cell.setImage(imageURL: optionImageURLArray[indexPath.row])
        cell.setText(text: optionDescriptionTexts[indexPath.row])
        
        return cell
    }
    
}

// MARK: - UICollectionViewDelegate FlowLayout
extension TrimOptionDetailOptionCollectionView: UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        minimumLineSpacingForSectionAt section: Int
    ) -> CGFloat {
        return Constants.Cell.spacing
    }
    
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        return CGSize(width: Constants.Cell.width, height: Constants.Cell.height)
    }
}
