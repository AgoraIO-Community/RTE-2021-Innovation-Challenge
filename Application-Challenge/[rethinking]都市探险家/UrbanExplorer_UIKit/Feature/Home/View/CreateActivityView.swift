//
//  InitialActivityView.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/25.
//

import Foundation
import UIKit
import RxSwift
import RxCocoa
import RxGesture

enum ActivityState {
  case begin
  case input
  case finish
}

final class CreateActivityView: UIView {
  fileprivate var state = ActivityState.begin
  private lazy var rectangle: UIView = {
    let instance = UIView()
    instance.backgroundColor = UIColor(hexString: "#dee3ea")
    instance.layer.cornerRadius = 2
    return instance
  }()
  private lazy var tipsLabel: UILabel = {
    let instance = UILabel()
    instance.textColor = .black
    instance.font = UIFont.puhui(size: 12, weight: .medium)
    instance.text = "当前半径5公里，共有107为探险家，18个探险主题"
    return instance
  }()
  fileprivate lazy var button: UIButton = {
    let instance = UIButton()
    instance.setTitle("发起探险", for: .normal)
    instance.backgroundColor = UIColor(hexString: "#272727")
    instance.layer.cornerRadius = 30
    instance.clipsToBounds = true
    instance.addTarget(self, action: #selector(handleButtonClick), for: .touchUpInside)
    return instance
  }()
  fileprivate lazy var locationContainer: UIView = {
    let instance = UIView()
    instance.backgroundColor = UIColor(hexString: "#dee3ea")
    instance.layer.cornerRadius = 5
    instance.clipsToBounds = true
    instance.alpha = 0
    return instance
  }()
  fileprivate lazy var locationLabel: UILabel = {
    let instance = UILabel()
    instance.textColor = .black
    instance.font = UIFont.puhui(size: 16, weight: .medium)
    instance.text = "输入探险地点"
    instance.isEnabled = false
    return instance
  }()
  let flatImageView = UIImageView(image: R.image.black_flag_icon())
  private lazy var nameTextFieldContainer: UIView = {
    let instance = UIView()
    instance.backgroundColor = UIColor(hexString: "#dee3ea")
    instance.layer.cornerRadius = 5
    instance.clipsToBounds = true
    instance.alpha = 0
    return instance
  }()
  fileprivate lazy var nameTextField: UITextField = {
    let instance = UITextField()
    instance.backgroundColor = UIColor(hexString: "#dee3ea")
    instance.attributedPlaceholder = NSAttributedString(string: "输入这次探险的名字", attributes: [.foregroundColor: UIColor.black.withAlphaComponent(0.3)])
    instance.textColor = .black
    instance.layer.cornerRadius = 5
    instance.clipsToBounds = true
    instance.font = UIFont.puhui(size: 16, weight: .medium)
    instance.returnKeyType = .done
    return instance
  }()
  private lazy var ageView: AgeSelectView = {
    let instance = AgeSelectView(frame: .zero)
    instance.alpha = 0
    return instance
  }()
  fileprivate let maleCountView = ActivityCountView(type: .male)
  fileprivate let femaleCountView = ActivityCountView(type: .female)
  private var buttonTipsLabelConstraint: NSLayoutConstraint!
  private var buttonAgeViewConstraint: NSLayoutConstraint!
  private var rectangleTipsLabelConstraint: NSLayoutConstraint!
  private var rectangleTextFieldConstraint: NSLayoutConstraint!
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    buildLayout()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  private func buildLayout() {
    layer.cornerRadius = 30
    clipsToBounds = true
    backgroundColor = .white
    addSubview(rectangle)
    addSubview(tipsLabel)
    addSubview(button)
    addSubview(locationContainer)
    locationContainer.addSubview(locationLabel)
    addSubview(nameTextFieldContainer)
    nameTextFieldContainer.addSubview(nameTextField)
    addSubview(ageView)
    addSubview(maleCountView)
    addSubview(femaleCountView)
    
    maleCountView.alpha = 0
    femaleCountView.alpha = 0
    
    rectangle.snp.makeConstraints { (make) in
      make.top.equalToSuperview().offset(28)
      make.centerX.equalToSuperview()
      make.size.equalTo(CGSize(width: 59, height: 4))
    }
    
    tipsLabel.snp.makeConstraints { (make) in
      make.centerX.equalToSuperview()
    }
    rectangleTipsLabelConstraint = tipsLabel.topAnchor.constraint(equalTo: rectangle.bottomAnchor, constant: 22)
    rectangleTipsLabelConstraint.isActive = true
    
    button.snp.makeConstraints { (make) in
      make.size.equalTo(CGSize(width: 223, height: 61))
      make.centerX.equalToSuperview()
      make.bottom.equalToSuperview().inset(42)
    }
    
    buttonTipsLabelConstraint = button.topAnchor.constraint(equalTo: tipsLabel.bottomAnchor, constant: 22)
    buttonTipsLabelConstraint.isActive = true
    
    locationContainer.snp.makeConstraints { make in
      make.left.right.equalToSuperview().inset(17)
      make.height.equalTo(46)
    }
    rectangleTextFieldConstraint = locationContainer.topAnchor.constraint(equalTo: rectangle.bottomAnchor, constant: 17)
    rectangleTextFieldConstraint.isActive = false
    
    nameTextFieldContainer.snp.makeConstraints { make in
      make.left.right.equalToSuperview().inset(17)
      make.height.equalTo(46)
      make.top.equalTo(locationContainer.snp.bottom).offset(20)
    }
    
    locationLabel.snp.makeConstraints { make in
      make.top.bottom.equalToSuperview()
      make.left.right.equalToSuperview().inset(17)
    }
    
    nameTextField.snp.makeConstraints { make in
      make.top.bottom.equalToSuperview()
      make.left.right.equalToSuperview().inset(17)
    }
    
    ageView.snp.makeConstraints { make in
      make.left.equalTo(locationContainer)
      make.top.equalTo(nameTextField.snp.bottom).offset(20)
    }
    
    buttonAgeViewConstraint = button.topAnchor.constraint(equalTo: ageView.bottomAnchor, constant: 50)
    buttonAgeViewConstraint.isActive = false
    
    maleCountView.snp.makeConstraints { make in
      make.left.equalTo(ageView.snp.right).offset(20)
      make.centerY.equalTo(ageView)
    }
    
    femaleCountView.snp.makeConstraints { make in
      make.left.equalTo(maleCountView.snp.right).offset(20)
      make.right.equalToSuperview().inset(17)
      make.centerY.equalTo(maleCountView)
    }
  }
  
  private func layout1() {
    tipsLabel.alpha = 1
    nameTextFieldContainer.alpha = 0
    locationContainer.alpha = 0
    ageView.alpha = 0
    maleCountView.alpha = 0
    femaleCountView.alpha = 0
    ageView.alpha = 0
    
    buttonTipsLabelConstraint.isActive = true
    buttonAgeViewConstraint.isActive = false
    rectangleTipsLabelConstraint.isActive = true
    rectangleTextFieldConstraint.isActive = false
    
    UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.8, initialSpringVelocity: 5, options: .curveEaseInOut) {
      self.layoutIfNeeded()
    } completion: { _ in
      
    }
  }
  
  private func layout2() {
    tipsLabel.alpha = 0
    nameTextFieldContainer.alpha = 1
    locationContainer.alpha = 1
    ageView.alpha = 1
    maleCountView.alpha = 1
    femaleCountView.alpha = 1
    
    buttonTipsLabelConstraint.isActive = false
    buttonAgeViewConstraint.isActive = true
    rectangleTipsLabelConstraint.isActive = false
    rectangleTextFieldConstraint.isActive = true
    
    UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.8, initialSpringVelocity: 5, options: .curveEaseInOut) {
      self.layoutIfNeeded()
    } completion: { _ in
      
    }
  }
  
  private func layout3() {
    
  }
  
  @objc private func handleButtonClick() {
    switch state {
    case .begin:
      state = .input
      layout2()
    case .input:
      state = .finish
      layout3()
    case .finish:
      state = .begin
      layout1()
    }
  }
  
  func reset() {
    state = .begin
    layout1()
  }
}

extension Reactive where Base == CreateActivityView {
  var activityName: Observable<String> {
    return base.nameTextField.rx.text.orEmpty.asObservable()
  }
  
  var activityLocation: Binder<String?> {
    return base.locationLabel.rx.text
  }
  
  var maleCount: Binder<Int> {
    return base.maleCountView.rx.count
  }
  
  var femaleCount: Binder<Int> {
    return base.femaleCountView.rx.count
  }
  
  var locationFiedDidClick: Observable<UITapGestureRecognizer> {
    return base.locationContainer.rx.tapGesture().when(.recognized)
  }
  
  var tapToNextState: Observable<ActivityState> {
    return base.button.rx.tap.flatMapLatest { _ -> Observable<ActivityState> in
      return .just(base.state)
    }
  }
  
  var tapMaleView: Observable<UITapGestureRecognizer> {
    return base.maleCountView.rx.tapGesture().when(.recognized)
  }
  
  var tapFemaleView: Observable<UITapGestureRecognizer> {
    return base.femaleCountView.rx.tapGesture().when(.recognized)
  }
}
