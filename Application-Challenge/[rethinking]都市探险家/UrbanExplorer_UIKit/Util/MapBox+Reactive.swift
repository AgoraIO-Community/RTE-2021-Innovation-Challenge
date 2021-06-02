//
//  MapBox+Reactive.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/23.
//

import Foundation
import Mapbox
import RxSwift

extension Reactive where Base == MGLMapView {
  var centerCoorindate: Binder<CLLocationCoordinate2D> {
    return Binder(self.base) {
      mapView, coorindate in
      mapView.setCenter(coorindate, zoomLevel: 13, animated: true)
    }
  }
}
