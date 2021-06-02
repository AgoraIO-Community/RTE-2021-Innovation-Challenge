//
//  AppDelegate.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/23.
//

import UIKit
import IQKeyboardManager

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
  var window: UIWindow?
  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    setup()
    window = UIWindow(frame: UIScreen.main.bounds)
    window?.rootViewController = HomeViewController()
    window?.makeKeyAndVisible()
    return true
  }
}

private func setup() {
  IQKeyboardManager.shared().isEnabled = true
  IQKeyboardManager.shared().isEnableAutoToolbar = true
}

