//
//  User.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/23.
//

import Foundation
import UIKit

struct User: Codable, Equatable {
  var phone: String
  var avatarIndex: Int
  var name: String
  var taskCount: Int
  var teamCount: Int
  var level: Int
  
  func avatarImage() -> UIImage? {
    if avatarIndex == 1 {
      return R.image.avatar1()
      
    }
    if avatarIndex == 2 {
      return R.image.avatar2()
    }
    
    if avatarIndex == 3 {
      return R.image.avatar3()
    }
    
    return nil
  }
}

extension User: Mock {
  static func mock() -> User {
    return User(phone: "18611693632", avatarIndex: 1, name: "Ye", taskCount: 10, teamCount: 5, level: 5)
  }
}
