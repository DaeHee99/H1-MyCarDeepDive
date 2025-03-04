//
//  DetailQuotationPreviewViewController.swift
//  GetYa
//
//  Created by 양승현 on 2023/08/07.
//

import UIKit
import Combine

final class DetailQuotationPreviewViewController: BaseViewController {
    private enum Constants {
        enum BottomCustomOrQuoteView {
            static let height: CGFloat = CustomOrQuoteSelectView
                .Constants.intrinsicContentHeight
        }
    }
    
    // MARK: - UI Properties
    private let tableView = DetailQuotationPreviewTableView(frame: .zero, style: .grouped)
    private let bottomCustomOrQuoteView = CustomOrQuoteSelectView()
    
    // MARK: - Properties
    private var adapter: DetailQuotationPreviewTableViewAdapter!
    private var viewModel: (
        any DetailQuotationPreviewViewModelable
        & DetailQuotationPreviewAdapterDataSource
        & CommonQuotationPreviewAdapterDataSource)!
    private let viewDidLoadEvent = PassthroughSubject<Void, Never>()
    private let customButtonEvent = PassthroughSubject<Void, Never>()
    private let quickQuoteEvent = PassthroughSubject<Void, Never>()
    private var subscription: AnyCancellable?
    
    // MARK: - Lifecycles
    init(
        viewModel: some DetailQuotationPreviewViewModelable
        & DetailQuotationPreviewAdapterDataSource
        & CommonQuotationPreviewAdapterDataSource
    ) {
        super.init(nibName: nil, bundle: nil)
        self.viewModel = viewModel
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        let repository = DefaultQuotationRepository()
        let userSelectedModel = CustomRecomendationModel(
            drivingExperienceId: 0,
            familyMembersId: 0,
            carPurposeId: 0,
            personalValueId: 0,
            maxBudget: 0)
        let defualtQuotationUseCase = DefaultQuotationUseCase(
            defaultQuotationRepository: repository,
            userSelectedQuestionModel: userSelectedModel)
        viewModel = DetailQuotationPreviewViewModel(
            keywords: [],
            quotationUseCase: defualtQuotationUseCase)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureUI()
        bind()
        viewDidLoadEvent.send()
    }
    
    // MARK: - Functions
    func configureUI() {
        view.backgroundColor = .white
        bottomCustomOrQuoteView.delegate = self
        setupUI()
    }
}

// MARK: - ViewBindable
extension DetailQuotationPreviewViewController: ViewBindable {
    typealias Input = DetailQuotationPreviewViewModel.DetailQuotationPreviewInput
    typealias State = DetailQuotationPreviewViewModel.DetailQuotationPreviewState
    typealias ErrorType = Error
    
    func bind() {
        let input = Input(
            viewDidLoadEvent: viewDidLoadEvent.eraseToAnyPublisher(),
            customButtonEvent: customButtonEvent.eraseToAnyPublisher(),
            quickQuoteEvent: quickQuoteEvent.eraseToAnyPublisher())
        let output = viewModel.transform(input: input)
        subscription = output.receive(on: DispatchQueue.main)
            .sink { [weak self] in
                self?.render($0)
            }
    }
    
    func render(_ state: State) {
        switch state {
        case .none:
            break
        case .updateDetailQuotationPreview:
            adapter = .init(
                tableView: tableView,
                dataSource: viewModel)
            tableView.reloadData()
        case .gotoCompletionPage(let contractionQuotation):
            navigationController?.pushViewController(
                LoadingViewController(
                    viewModel: LoadingViewModel(
                        contrationQuotation: contractionQuotation,
                        useCase: DefaultLoadingUseCase(
                            repository: DefaultLoadingRepository(
                                provider: SessionProvider())))),
                animated: true)
        case .gotoCustomPage(let trimCarSpec):
            let provider = SessionProvider()
            let useCase = DefaultCarSettingUseCase(
                trimSelectRepository: DefaultTrimSelectRepository(provider: provider),
                colorSelectRepository: DefaultColorSelectRepository(provider: provider),
                optionSelectRepository: DefaultOptionSelectRepository(provider: provider))
            let carSettingSelectViewController = CarSettingSelectViewController(
                viewModel: CarSettingSelectViewModel(useCase: useCase))
            let trimSelectViewController = TrimSelectViewController(
                viewModel: TrimSelectViewModel(
                    trimSubOptionSelect: TrimSubOptionSelect(
                        engineID: trimCarSpec.engineID,
                        bodyID: trimCarSpec.bodyID,
                        drivingSystemID: trimCarSpec.drivingSystemID),
                    useCase: useCase))
            let colorSelectViewController = ColorSelectViewController(
                viewModel: ColorSelectViewModel(useCase: useCase))
            let optionSelectViewController = OptionSelectViewController(
                viewModel: OptionSelectViewModel(useCase: useCase))
            
            carSettingSelectViewController.setViewControllers(
                viewControllers: [
                    trimSelectViewController,
                    colorSelectViewController,
                    optionSelectViewController])
            guard let homeViewController = self.navigationController?.viewControllers.first else { return }
            (UIApplication.shared.connectedScenes.first?.delegate as? SceneDelegate)?
                .removeAllViewContrllerExcept(
                    to: homeViewController,
                    nextViewController: carSettingSelectViewController)
        }
    }
}

// MARK: - CustomOrQuoteSelectViewDelegate
extension DetailQuotationPreviewViewController: CustomOrQuoteSelectViewDelegate {
    func gotoCustomPage() {
        customButtonEvent.send()
    }
    
    func gotoQuotePage() {
        quickQuoteEvent.send()
    }
}

// MARK: - LayoutSupportable
extension DetailQuotationPreviewViewController: LayoutSupportable {
    func setupViews() {
        view.addSubviews([
            tableView,
            bottomCustomOrQuoteView])
    }
    
    func setupConstriants() {
        configureTableView()
        configureBottomCustomOrQuoteView()
    }
    
    // MARK: - LayoutSupportable private helper
    private func configureTableView() {
        NSLayoutConstraint.activate([
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor)])
    }
    
    private func configureBottomCustomOrQuoteView() {
        NSLayoutConstraint.activate([
            bottomCustomOrQuoteView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            bottomCustomOrQuoteView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            bottomCustomOrQuoteView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            bottomCustomOrQuoteView.heightAnchor.constraint(equalToConstant: Constants.BottomCustomOrQuoteView.height),
            bottomCustomOrQuoteView.topAnchor.constraint(
                equalTo: tableView.bottomAnchor,
                constant: -CustomOrQuoteSelectView.Constants.gradientLayerHeight)])
    }
}
