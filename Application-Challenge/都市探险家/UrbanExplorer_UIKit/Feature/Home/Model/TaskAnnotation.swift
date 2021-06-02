//
//  TaskAnnotation.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/6/2.
//

import UIKit
import CoreLocation
import Mapbox

class TaskAnnotation: MGLPointAnnotation {
  let task: Task
  
  init(coordinate: CLLocationCoordinate2D, task: Task) {
    self.task = task
    super.init()
    self.coordinate = coordinate
    self.title = task.locationName
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
}
