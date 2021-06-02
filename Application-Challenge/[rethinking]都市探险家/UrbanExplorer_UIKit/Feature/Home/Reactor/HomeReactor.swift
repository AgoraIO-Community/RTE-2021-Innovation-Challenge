//
//  HomeReactor.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/23.
//

import Foundation
import ReactorKit
import CoreLocation
import MapKit

final class HomeReactor: Reactor {
  enum Action {
    case selectLocation(SearchLocation)
    case inputActivityName(String)
    case userLocationChanged(CLLocationCoordinate2D)
    case inputMaleCount(Int)
    case inputFemaleCount(Int)
    case sendTask
    case findTask(Task)
    case findNewJoinTask(JoinTask)
    case selectTaskAnnotation(Task)
  }
  
  enum Mutation {
    case setActivityName(String)
    case setSelectedLocation(SearchLocation)
    case setUserLocation(CLLocationCoordinate2D)
    case setMaleCount(Int)
    case setFemaleCount(Int)
    case setError(ReactorError)
    case setTask(Task)
    case appendTask(Task)
    case setSelectedTaskAnnotation(Task)
    case setJoinTask(JoinTask)
  }
  
  struct State {
    var currentCoorindate: CLLocationCoordinate2D?
    var selectedLocation: SearchLocation?
    var createdTask: Task?
    var activityName: String?
    var ageRange: String = "20-30"
    var maleCount: Int = 0
    var femaleCount: Int = 0
    var error: ReactorError?
    var receivedTask: Task?
    var sections: [TaskSection] = []
    var joinedMembers: [User] = []
    var selectedTask: Task?
    
  }
  
  var initialState: State = State()
  
  func mutate(action: Action) -> Observable<Mutation> {
    switch action {
    case let .selectTaskAnnotation(task):
      return .just(.setSelectedTaskAnnotation(task))
    case let .inputActivityName(name):
      return .just(.setActivityName(name))
    case let .selectLocation(location):
    return .just(.setSelectedLocation(location))
    case let .userLocationChanged(coordinate):
      return .just(.setUserLocation(coordinate))
    case let .inputMaleCount(count):
      guard count + currentState.femaleCount <= 8 else {
        return .just(.setError(ReactorError("总人数不能超过8人，请重新选择")))
      }
      return .just(.setMaleCount(count))
    case let .inputFemaleCount(count):
      guard count + currentState.maleCount <= 8 else {
        return .just(.setError(ReactorError("总人数不能超过8人，请重新选择")))
      }
      return .just(.setFemaleCount(count))
    case .sendTask:
      guard currentState.maleCount + currentState.femaleCount > 0 else {
        return .just(.setError(ReactorError("组队人数不能为0")))
      }
      guard let name = currentState.activityName, !name.isEmpty else {
        return .just(.setError(ReactorError("请输入活动名称")))
      }
      guard let locationName = currentState.selectedLocation?.name else {
        return .just(.setError(ReactorError("请选择活动地址")))
      }
      guard let coordinate = currentState.selectedLocation?.coordinate else {
        return .just(.setError(ReactorError("活动地址为空")))
      }
      return .just(.setTask(Task(id: UUID().uuidString, locationName: locationName, name: name, maleCount: currentState.maleCount, femaleCount: currentState.femaleCount, range: "20-30", joinedMembers: [UserDefaults.standard.currentUser()], owenr: UserDefaults.standard.currentUser(), latitude: Float(coordinate.latitude), longitude: Float(coordinate.longitude))))
    case let .findTask(task):
        return .just(.appendTask(task))
    case let .findNewJoinTask(joinTask):
      return .just(.setJoinTask(joinTask))
    }
  }
  
  func reduce(state: State, mutation: Mutation) -> State {
    var state = state
    switch mutation {
    case let .setSelectedLocation(location):
      state.selectedLocation = location
    case let .setActivityName(name):
      state.activityName = name
    case let .setUserLocation(coordinate):
      state.currentCoorindate = coordinate
    case let .setMaleCount(count):
      state.maleCount = count
    case let .setFemaleCount(count):
      state.femaleCount = count
    case let .setError(error):
      state.error = error
    case let .setTask(task):
      state.createdTask = task
    case let .appendTask(task):
      state.receivedTask = task
    case let .setSelectedTaskAnnotation(task):
      state.selectedTask = task
    case let .setJoinTask(joinTask):
      if var createdTask = state.createdTask, createdTask.id == joinTask.taskId {
        if !createdTask.joinedMembers.contains(joinTask.user) {
          createdTask.joinedMembers.append(joinTask.user)
        }
        state.createdTask = createdTask
      }
    }
    return state
  }
}
