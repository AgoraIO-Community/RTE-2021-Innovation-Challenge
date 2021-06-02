//
//  UserDefault+Extension.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/6/2.
//

import Foundation

private let loginUserKey = "LoginUserKey"
extension UserDefaults {
  func saveUser(user: User) {
    if let data = try? JSONEncoder().encode(user) {
      UserDefaults.standard.setValue(data, forKey: loginUserKey)
    }
  }
  
  func currentUser() -> User {
    guard let data = UserDefaults.standard.data(forKey: loginUserKey) else {
      fatalError()
    }
    guard let user = try? JSONDecoder().decode(User.self, from: data) else {
      fatalError()
    }
    return user
  }
  
  func isUserValiable() -> Bool {
    return (UserDefaults.standard.data(forKey: loginUserKey) != nil)
  }
}
