//
//  SVProgressHud+Reactive.swift
//  RCE
//
//  Created by 叶孤城 on 2021/4/21.
//

import Foundation
import RxSwift
import RxCocoa
import SVProgressHUD

extension Reactive where Base: SVProgressHUD {
  static var status: Binder<String?> {
    return Binder(UIApplication.shared) { (_, value) in
      SVProgressHUD.show(withStatus: value)
    }
  }
  
  static func status(withImage image: UIImage) -> Binder<String?> {
    return Binder(UIApplication.shared) { (_, value) in
      SVProgressHUD.show(image, status: value)
    }
  }
  
  static var infoStatus: Binder<String?> {
    return Binder(UIApplication.shared) { (_, value) in
      SVProgressHUD.showInfo(withStatus: value)
    }
  }
  
  static var error: Binder<Error> {
    return Binder(UIApplication.shared) { (_, value) in
      SVProgressHUD.showError(withStatus: value.localizedDescription)
    }
  }
  
  static var errorStatus: Binder<String?> {
    return Binder(UIApplication.shared) { (_, value) in
      SVProgressHUD.showError(withStatus: value)
    }
  }
  
  static var successStatus: Binder<String?> {
    return Binder(UIApplication.shared) { (_, value) in
      SVProgressHUD.showSuccess(withStatus: value)
      
    }
  }
  
  static var progress: Binder<Float> {
    return Binder(UIApplication.shared) { (_, value) in
      SVProgressHUD.showProgress(value)
    }
  }
  
  static func progress(withStatus status: String) -> Binder<Float> {
    return Binder(UIApplication.shared) { (_, value) in
      SVProgressHUD.showProgress(value, status: status)
    }
  }
}
