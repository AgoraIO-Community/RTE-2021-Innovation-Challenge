//
//  MockProtocol.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/23.
//

import Foundation

protocol Mock {
  associatedtype T
  static func mock() -> T
}

