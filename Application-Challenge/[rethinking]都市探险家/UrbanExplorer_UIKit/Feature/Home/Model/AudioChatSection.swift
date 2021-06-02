//
//  AudioChatSection.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/6/2.
//

import Foundation
import RxDataSources

struct AudioChatSection {
  var items: [User]
}

extension AudioChatSection: SectionModelType {
  typealias Item = User
  
  init(original: AudioChatSection, items: [User]) {
    self = original
    self.items = items
  }
}
