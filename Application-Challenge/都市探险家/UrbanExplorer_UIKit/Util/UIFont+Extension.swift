//
//  UIFont+Extension.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/23.
//

import Foundation
import UIKit

extension UIFont {
  static func puhui(size: CGFloat, weight: UIFont.Weight = .regular) -> UIFont? {
    switch weight {
    case .regular:
      return UIFont(name: "AlibabaPuHuiTiR", size: size)
    case .medium:
      return UIFont(name:"AlibabaPuHuiTiM", size: size)
    case .light:
      return UIFont(name:"AlibabaPuHuiTiL", size: size)
    case .heavy:
      return UIFont(name:"AlibabaPuHuiTiH", size: size)
    case .bold:
      return UIFont(name:"AlibabaPuHuiTiB", size: size)
    default:
      return UIFont.systemFont(ofSize: size)
    }
  }

}
