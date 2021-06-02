//
//  UIColor+Extension.swift
//  RCE
//
//  Created by 叶孤城 on 2021/5/13.
//

import Foundation
import UIKit

extension UIColor {
  
  public convenience init(_ hexString: String) {
    self.init(hexString: hexString, alpha: 1.0)
  }
  
  public convenience init(hexInt: Int, alpha: Float = 1.0) {
    let hexString = String(format: "%06X", hexInt)
    self.init(hexString: hexString, alpha: alpha)
  }
  
  public convenience init(hexString: String, alpha: Float = 1.0) {
    var red: CGFloat = 0
    var green: CGFloat = 0
    var blue: CGFloat = 0
    var mAlpha: CGFloat = CGFloat(alpha)
    var minusLength = 0
    
    let scanner = Scanner(string: hexString)
    
    if hexString.hasPrefix("#") {
      scanner.currentIndex = hexString.index(hexString.startIndex, offsetBy: 1)
      minusLength = 1
    }
    if hexString.hasPrefix("0x") {
      scanner.currentIndex = hexString.index(hexString.startIndex, offsetBy: 2)
      minusLength = 2
    }
    var hexValue: UInt64 = 0
    scanner.scanHexInt64(&hexValue)
    switch hexString.count - minusLength {
    case 3:
      red = CGFloat((hexValue & 0xF00) >> 8) / 15.0
      green = CGFloat((hexValue & 0x0F0) >> 4) / 15.0
      blue = CGFloat(hexValue & 0x00F) / 15.0
    case 4:
      red = CGFloat((hexValue & 0xF000) >> 12) / 15.0
      green = CGFloat((hexValue & 0x0F00) >> 8) / 15.0
      blue = CGFloat((hexValue & 0x00F0) >> 4) / 15.0
      mAlpha = CGFloat(hexValue & 0x00F) / 15.0
    case 6:
      red = CGFloat((hexValue & 0xFF0000) >> 16) / 255.0
      green = CGFloat((hexValue & 0x00FF00) >> 8) / 255.0
      blue = CGFloat(hexValue & 0x0000FF) / 255.0
    case 8:
      red = CGFloat((hexValue & 0xFF000000) >> 24) / 255.0
      green = CGFloat((hexValue & 0x00FF0000) >> 16) / 255.0
      blue = CGFloat((hexValue & 0x0000FF00) >> 8) / 255.0
      mAlpha = CGFloat(hexValue & 0x000000FF) / 255.0
    default:
      break
    }
    self.init(red: red, green: green, blue: blue, alpha: mAlpha)
  }
  
  /// UIColor components value between 0 to 255
  public convenience init(byteRed red: Int, green: Int, blue: Int, alpha: Float = 1.0) {
    self.init(red: CGFloat(red)/255.0,
              green: CGFloat(green)/255.0,
              blue: CGFloat(blue)/255.0,
              alpha: CGFloat(alpha))
  }
  
  public func alpha(_ value: Float) -> UIColor {
    let (red, green, blue, _) = UIColorComponents()
    return UIColor(red: red, green: green, blue: blue, alpha: CGFloat(value))
  }
  
  public func red(_ value: Int) -> UIColor {
    let (_, green, blue, alpha) = UIColorComponents()
    return UIColor(red: CGFloat(value)/255.0, green: green, blue: blue, alpha: alpha)
  }
  
  public func green(_ value: Int) -> UIColor {
    let (red, _, blue, alpha) = UIColorComponents()
    return UIColor(red: red, green: CGFloat(value)/255.0, blue: blue, alpha: alpha)
  }
  
  public func blue(_ value: Int) -> UIColor {
    let (red, green, _, alpha) = UIColorComponents()
    return UIColor(red: red, green: green, blue: CGFloat(value)/255.0, alpha: alpha)
  }
  
  //swiftlint:disable large_tuple
  public func UIColorComponents() -> (CGFloat, CGFloat, CGFloat, CGFloat) {
    var red: CGFloat = 0
    var green: CGFloat = 0
    var blue: CGFloat = 0
    var alpha: CGFloat = 0
    #if os(iOS)
    self.getRed(&red, green: &green, blue: &blue, alpha: &alpha)
    #elseif os(OSX)
    self.usingUIColorSpaceName(NSUIColorSpaceName.calibratedRGB)!.getRed(&red, green: &green, blue: &blue, alpha: &alpha)
    #endif
    return (red, green, blue, alpha)
  }
}

extension UIColor {
  
  public struct Corner: OptionSet {
    public let rawValue: Int
    
    public init(rawValue: Int) {
      self.rawValue = rawValue
    }
    
    public static let none = Corner([])
    public static let leftTop = Corner(rawValue: 1 << 0)
    public static let rightTop = Corner(rawValue: 1 << 1)
    public static let rightBottom = Corner(rawValue: 1 << 2)
    public static let leftBottom = Corner(rawValue: 1 << 3)
    public static let all = Corner([Corner.leftTop, Corner.rightTop, Corner.rightBottom, Corner.leftBottom])
  }
  
  //  corners default all
  public func toImage(size: CGSize = CGSize(width: 1, height: 1),
                      radius: CGFloat = 0,
                      corners: Corner = .none) -> UIImage? {
    #if os(iOS)
    let rect = CGRect(x: 0, y: 0, width: size.width, height: size.height)
    UIGraphicsBeginImageContextWithOptions(size, false, 0)
    
    /// corners
    if radius > 0, corners != .none, let context = UIGraphicsGetCurrentContext() {
      let path = cornersPath(size: size, radius: radius, corners: corners)
      context.addPath(path.cgPath)
      context.clip()
    }
    
    self.setFill()
    UIRectFill(rect)
    let image: UIImage? = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return image
    #elseif os(OSX)
    let image = NSImage(size: size)
    image.lockFocus()
    drawSwatch(in: NSRect(0, 0, size.width, size.height))
    image.unlockFocus()
    return image
    #endif
  }
  
  private func cornersPath(size: CGSize, radius: CGFloat, corners: Corner) -> UIBezierPath {
    let path = UIBezierPath()
    if corners.contains(.leftTop) {
      let point = CGPoint(x: 0, y: radius)
      path.move(to: point)
      let center = CGPoint(x: radius, y: radius)
      path.addArc(withCenter: center, radius: radius, startAngle: .pi, endAngle: .pi * 1.5, clockwise: true)
    } else {
      path.move(to: .zero)
    }
    
    if corners.contains(.rightTop) {
      let center = CGPoint(x: size.width - radius, y: radius)
      path.addArc(withCenter: center, radius: radius, startAngle: .pi * 1.5, endAngle: .pi * 2, clockwise: true)
    } else {
      path.addLine(to: CGPoint(x: size.width, y: 0))
    }
    
    if corners.contains(.rightBottom) {
      let center = CGPoint(x: size.width - radius, y: size.height - radius)
      path.addArc(withCenter: center, radius: radius, startAngle: 0, endAngle: .pi * 0.5, clockwise: true)
    } else {
      path.addLine(to: CGPoint(x: size.width, y: size.height))
    }
    
    if corners.contains(.leftBottom) {
      let center = CGPoint(x: radius, y: size.height - radius)
      path.addArc(withCenter: center, radius: radius, startAngle: .pi * 0.5, endAngle: .pi, clockwise: true)
    } else {
      path.addLine(to: CGPoint(x: 0, y: size.height))
    }
    
    path.close()
    
    return path
  }
}
