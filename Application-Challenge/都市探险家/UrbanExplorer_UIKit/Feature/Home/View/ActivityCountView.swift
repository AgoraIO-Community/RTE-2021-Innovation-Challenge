//
//  ActivityCountView.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/29.
//

import Foundation
import UIKit
import RxSwift

final class ActivityCountView: UIView {
  enum ActivityCountType {
    case male
    case female
    
    var image: UIImage? {
      switch self {
      case .male:
        return R.image.male_icon()
      default:
        return R.image.female_icon()
      }
    }
  }
  
  private lazy var genderImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = nil
    return instance
  }()
  fileprivate lazy var countLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 16, weight: .medium)
    instance.textColor = .black
    instance.text = "0"
    return instance
  }()
  
  init(type: ActivityCountType) {
    super.init(frame: .zero)
    backgroundColor = UIColor(hexString: "dee3ea")
    layer.cornerRadius = 5
    clipsToBounds = true
    genderImageView.image = type.image
    addSubview(genderImageView)
    addSubview(countLabel)
    
    genderImageView.snp.makeConstraints { make in
      make.left.equalToSuperview().offset(9)
      make.centerY.equalToSuperview()
    }
    
    countLabel.snp.makeConstraints { make in
      make.top.bottom.equalToSuperview().inset(12)
      make.left.equalTo(genderImageView.snp.right).offset(20)
      make.right.equalToSuperview().inset(20)
    }
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
}

extension Reactive where Base == ActivityCountView {
  var count: Binder<Int> {
    return Binder(self.base) {
      view, count in
      view.countLabel.text = "\(count)"
    }
  }
}
