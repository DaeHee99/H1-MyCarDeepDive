//
//  LifeStyleDetailViewController.swift
//  GetYa
//
//  Created by 배남석 on 2023/08/08.
//

import UIKit

class LifeStylePeekViewController: BaseViewController {
    // MARK: - UI properties
    private let contentView = LifeStylePeekContentView()
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupViews()
        configureUI()
    }
    
    // MARK: - Private Functions
    private func setupViews() {
        view.addSubview(contentView)
    }
    
    private func configureUI() {
        self.view.backgroundColor = .white
        
        configureContentView()
    }
    
    private func configureContentView() {
        NSLayoutConstraint.activate([
            contentView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            contentView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            contentView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            contentView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
    
    // MARK: - Functions
    func setupContentView(tagTexts: [String], descriptionText: String, image: UIImage?) {
        contentView.setupTagViews(tagTexts: tagTexts)
        contentView.setupDescriptionText(text: descriptionText)
        contentView.setupPersonImageView(image: image)
    }
}
