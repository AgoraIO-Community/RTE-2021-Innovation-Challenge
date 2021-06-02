//
//  SearchLocationViewController.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/29.
//

import UIKit
import RxDataSources
import MapKit
import RxSwift
import RxCocoa

class SearchLocationViewController: UIViewController {
  private var disposeBag = DisposeBag()
  private lazy var container: UIView = {
    let instance = UIView()
    instance.layer.cornerRadius = 30
    instance.clipsToBounds = true
    instance.backgroundColor = .white
    return instance
  }()
  private lazy var searchBar: UISearchBar = {
    let instance = UISearchBar(frame: .zero)
    instance.searchTextField.backgroundColor = UIColor(hexString: "dee3ea")
    instance.returnKeyType = .done
    instance.backgroundColor = .clear
    instance.searchTextField.font = .puhui(size: 16, weight: .medium)
    instance.searchBarStyle = .minimal
    return instance
  }()
  private lazy var tableView: UITableView = {
    let instance = UITableView(frame: .zero, style: .plain)
    instance.backgroundColor = .clear
    instance.separatorStyle = .none
    instance.register(cellType: SearchLocationTableViewCell.self)
    return instance
  }()
  private lazy var dataSource: RxTableViewSectionedReloadDataSource<SearchLocationSection> = {
    return RxTableViewSectionedReloadDataSource<SearchLocationSection> { (dataSource, tableView, indexPath, item) -> UITableViewCell in
      let cell = tableView.dequeueReusableCell(for: indexPath, cellType: SearchLocationTableViewCell.self)
      cell.updateCell(name: item.name, address: item.address)
      return cell
    }
  }()
  fileprivate lazy var confirmButton: UIButton = {
    let instance = UIButton()
    instance.backgroundColor = UIColor(hexString: "#272727")
    instance.titleLabel?.font = .puhui(size: 21, weight: .medium)
    instance.setTitle("确定", for: .normal)
    instance.setTitleColor(.white, for: .normal)
    instance.layer.cornerRadius = 3
    instance.clipsToBounds = true
    return instance
  }()
  private lazy var cancelButton: UIButton = {
    let instance = UIButton()
    instance.setTitle("取消", for: .normal)
    instance.setTitleColor(UIColor(hexString: "#272727"), for: .normal)
    instance.titleLabel?.font = .puhui(size: 21, weight: .medium)
    return instance
  }()
  fileprivate let selectedItemSubject = PublishSubject<SearchLocation>()
  
  override func viewDidLoad() {
    super.viewDidLoad()
    buildLayout()
    bind()
  }
  
  override func viewWillAppear(_ animated: Bool) {
    super.viewWillAppear(animated)
    searchBar.becomeFirstResponder()
  }
  
  private func buildLayout() {
    view.addSubview(container)
    container.addSubview(searchBar)
    container.addSubview(tableView)
    view.addSubview(cancelButton)
    view.addSubview(confirmButton)
    
    container.snp.makeConstraints { make in
      make.left.right.bottom.equalToSuperview()
      make.height.equalToSuperview().multipliedBy(0.7)
    }
    
    searchBar.snp.makeConstraints { make in
      make.top.equalToSuperview().offset(30)
      make.left.right.equalToSuperview().inset(17)
    }
    
    tableView.snp.makeConstraints { make in
      make.top.equalTo(searchBar.snp.bottom)
      make.left.right.bottom.equalToSuperview()
    }
    
    cancelButton.snp.makeConstraints { make in
      make.left.equalToSuperview().offset(26)
      make.top.equalToSuperview().offset(40)
    }
    
    confirmButton.snp.makeConstraints { make in
      make.right.equalToSuperview().inset(26)
      make.centerY.equalTo(cancelButton)
      make.size.equalTo(CGSize(width: 74, height: 38))
    }
  }
  
  private func bind() {
    searchBar.rx
      .text
      .orEmpty
      .throttle(RxTimeInterval.milliseconds(500), scheduler: MainScheduler.instance)
      .distinctUntilChanged()
      .flatMapLatest({ _ -> Observable<String> in
        return self.searchBar.rx.text.orEmpty.asObservable()
      })
      .flatMapLatest({ name -> Observable<[MKMapItem]> in
        return self.mapItems(name: name)
      }).flatMapLatest { items -> Observable<[SearchLocationSection]> in
        let searchItems = items.map {
          SearchLocation(name: $0.name ?? "", address: $0.placemark.address, coordinate: $0.placemark.coordinate)
        }
        return Observable<[SearchLocationSection]>.just([SearchLocationSection(items: searchItems)])
      }
      .bind(to: tableView.rx.items(dataSource: dataSource))
      .disposed(by: disposeBag)
    
    tableView.rx.modelSelected(SearchLocation.self).bind(to: selectedItemSubject).disposed(by: disposeBag)
  }
  
  private func mapItems(name: String) -> Observable<[MKMapItem]> {
    return Observable.create { observer -> Disposable in
      let searchRequest = MKLocalSearch.Request()
      searchRequest.naturalLanguageQuery = name
      searchRequest.resultTypes = .address
      let localSearch = MKLocalSearch(request: searchRequest)
      localSearch.start { response, error in
        if error != nil {
          observer.onNext([])
        }
        if let items = response?.mapItems {
          observer.onNext(items)
          observer.on(.completed)
        } else {
          observer.onNext([])
        }
      }
      return Disposables.create()
    }
  }
}

extension Reactive where Base == MKLocalSearchCompleter {
  var query: Binder<String> {
    return Binder(self.base) {
      search, text in
      search.queryFragment = text
    }
  }
}

extension MKPlacemark {
  var address: String {
    var address = ""
    if let country = self.country {
      address += country
    }
    if let subAdministrativeArea = self.subAdministrativeArea {
      address += subAdministrativeArea
    }
    if let postCode = self.postalCode {
      address += postCode
    }
    if let locality = self.locality {
      address += locality
    }
    if let thoroughfare = self.thoroughfare {
      address += thoroughfare
    }
    if let subThoroughfare = self.subThoroughfare {
      address += subThoroughfare
    }
    return address
  }
}

extension Reactive where Base == SearchLocationViewController {
  var selectedItem: Observable<SearchLocation> {
    return base.confirmButton.rx.tap.withLatestFrom(base.selectedItemSubject.asObservable()).do { _ in
      base.dismiss(animated: true, completion: nil)
    }
  }
}
