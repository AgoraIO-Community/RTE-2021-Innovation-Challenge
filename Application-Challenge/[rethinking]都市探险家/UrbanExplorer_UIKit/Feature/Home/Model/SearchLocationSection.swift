//
//  SearchLocationSection.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/29.
//

import Foundation
import RxDataSources
import CoreLocation

struct SearchLocation {
  let name: String
  let address: String
  let coordinate: CLLocationCoordinate2D
}

struct SearchLocationSection {
  var items: [SearchLocation]
}

extension SearchLocationSection: SectionModelType {
  typealias Item = SearchLocation
  
  init(original: SearchLocationSection, items: [SearchLocation]) {
    self = original
    self.items = items
  }
}
