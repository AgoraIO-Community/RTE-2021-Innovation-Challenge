//
//  Task.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/30.
//

import Foundation
import RxDataSources

struct Task: Codable, Equatable {
  let id: String
  let locationName: String
  let name: String
  let maleCount: Int
  let femaleCount: Int
  let range: String
  var joinedMembers = [User]()
  var owenr: User
  let latitude: Float
  let longitude: Float
  
  static func == (lhs: Task, rhs: Task) -> Bool {
    return lhs.id == rhs.id && lhs.joinedMembers == rhs.joinedMembers
  }
}

struct JoinTask: Codable {
  let taskId: String
  let user: User
  let isJoin: Bool
}

struct TaskSection {
  var items: [Task]
}

extension TaskSection: SectionModelType {
  typealias Item = Task
  
  init(original: TaskSection, items: [Task]) {
    self = original
    self.items = items
  }
}
