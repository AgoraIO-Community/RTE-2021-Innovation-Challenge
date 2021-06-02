//
//  ReactorSuccess.swift
//  RCE
//
//  Created by 叶孤城 on 2021/4/21.
//

import Foundation

private var reactorSuccessIndex = 1
struct ReactorSuccess: Equatable {
  private let id: Int
  let message: String
  
  init(_ message: String) {
    id = reactorSuccessIndex
    reactorSuccessIndex += 1
    self.message = message
  }
}
