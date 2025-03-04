//
//  AdditionalOptionViewController.swift
//  GetYa
//
//  Created by 배남석 on 2023/08/21.
//

import UIKit

class AdditionalOptionViewController: UIViewController {
    // MARK: - UI properties
    private let collectionView = AdditionalCollectionView()
    
    // MARK: - Properties
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()

        setupViews()
        configureUI()
        collectionView.updateCategorySnapShot(types: Array(TagCategoryType.allCases[1...]))
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        NotificationCenter.default.post(name: NSNotification.Name("AdditionalViewWillAppear"), object: nil)
    }
    
    // MARK: - Private Functions
    private func setupViews() {
        view.addSubview(collectionView)
    }
    
    private func configureUI() {
        configureCollectionView()
    }
    
    private func configureCollectionView() {
        NSLayoutConstraint.activate([
            collectionView.topAnchor.constraint(equalTo: view.topAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
    
    // MARK: - Functions
    func setOptionInquery(inquery: AdditionalOptionInquery) {
        collectionView.setAllItemCount(count: inquery.optionPackageList.count + inquery.additionalOptionList.count)
        collectionView.updatePackageItemSnapShot(data: inquery.optionPackageList)
        collectionView.updateItemSnapShot(data: inquery.additionalOptionList)
    }
    
    func setTagOptionInquery(inquery: AdditionalTagOptionInquery) {
        collectionView.updateTagItemSnasphot(inquery: inquery)
    }
    
    // MARK: - Objc Functions

}
