//
//  AgeSelectView.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/29.
//

import Foundation
import UIKit
import RxSwift

final class AgeSelectView: UIView {
  fileprivate lazy var ageRangeLabel: UILabel = {
    let ageRangeLabel = UILabel()
    ageRangeLabel.textColor = UIColor.black
    ageRangeLabel.font = UIFont.puhui(size: 16, weight: .medium)
    ageRangeLabel.text = "20-30"
    return ageRangeLabel
  }()
  private lazy var ageLabel: UILabel = {
    let ageLabel = UILabel()
    ageLabel.textColor = UIColor.black.withAlphaComponent(0.3)
    ageLabel.font = UIFont.puhui(size: 16, weight: .medium)
    ageLabel.text = "年龄"
    return ageLabel
  }()
  private let imageView = UIImageView(image: R.image.age_select_icon())

  
  override init(frame: CGRect) {
    super.init(frame: frame)
    backgroundColor = UIColor(hexString: "dee3ea")
    layer.cornerRadius = 5
    clipsToBounds = true
    addSubview(ageRangeLabel)
    addSubview(ageLabel)
    addSubview(imageView)
    
    ageLabel.snp.makeConstraints { (make) in
      make.top.bottom.equalToSuperview().inset(12)
      make.left.equalToSuperview().offset(12)
    }
    
    ageRangeLabel.snp.makeConstraints { (make) in
      make.left.equalTo(ageLabel.snp.right).offset(9)
      make.centerY.equalToSuperview()
    }
    
    imageView.snp.makeConstraints { (make) in
      make.left.equalTo(ageRangeLabel.snp.right).offset(9)
      make.right.equalToSuperview().inset(12)
      make.centerY.equalToSuperview()
    }
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
}

extension Reactive where Base == AgeSelectView {
  var ageRange: Binder<String> {
    return Binder(self.base) { view, text in
      view.ageRangeLabel.text = text
    }
  }
}
