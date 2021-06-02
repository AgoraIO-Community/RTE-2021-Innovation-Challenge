//
//  ActivityLocationAnnotationView.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/29.
//

import UIKit
import Mapbox
import Pulsator
import RxSwift

class ActivityAnnotationView: MGLAnnotationView {
  var disposeBag = DisposeBag()
  private lazy var flagImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = R.image.black_flag_icon()
    return instance
  }()
  private lazy var beginSearchImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = R.image.begin_pulstor_icon()
    instance.isHidden = true
    return instance
  }()
  private lazy var radarView: Pulsator = {
    let instance = Pulsator()
    instance.numPulse = 4
    instance.radius = 300
    instance.backgroundColor = UIColor(hexString: "#45D297").cgColor
    instance.pulseInterval = 0.3
    return instance
  }()
  private lazy var locationNameLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 12, weight: .medium)
    instance.textColor = .black
    instance.preferredMaxLayoutWidth = 150
    return instance
  }()
  fileprivate var task: Task
  
  init(annotation: TaskAnnotation, reuseIdentifier: String) {
    task = annotation.task
    super.init(annotation: annotation, reuseIdentifier: reuseIdentifier)
    isUserInteractionEnabled = true
    layer.addSublayer(radarView)
    addSubview(flagImageView)
    flagImageView.frame = CGRect(x: 0, y: 0, width: 32, height: 32)
    addSubview(beginSearchImageView)
    beginSearchImageView.frame = CGRect(x: 0, y: 0, width: 32, height: 32)
    addSubview(locationNameLabel)
    locationNameLabel.text = annotation.title
    locationNameLabel.sizeToFit()
    locationNameLabel.center = CGPoint(x: flagImageView.center.x, y: flagImageView.frame.maxY + 23)
  }
  
  override func layoutSubviews() {
    super.layoutSubviews()
    flagImageView.center = CGPoint(x: bounds.width/2, y: bounds.height/2)
    beginSearchImageView.center = CGPoint(x: bounds.width/2, y: bounds.height/2)
    radarView.position = CGPoint(x: bounds.width/2, y: bounds.height/2)
    locationNameLabel.center = CGPoint(x: bounds.width/2, y: bounds.height - 15)
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func start() {
    flagImageView.isHidden = true
    beginSearchImageView.isHidden = false
    radarView.start()
  }
  
  func stop() {
    flagImageView.isHidden = false
    beginSearchImageView.isHidden = true
    radarView.stop()
  }
  
  func update(taskAnnotation: TaskAnnotation) {
    self.task = taskAnnotation.task
    locationNameLabel.text = self.task.locationName
    locationNameLabel.sizeToFit()
  }
  
  override func setSelected(_ selected: Bool, animated: Bool) {
    super.setSelected(selected, animated: animated)
  }
  
  override func prepareForReuse() {
    super.prepareForReuse()
    disposeBag = DisposeBag()
  }
}

extension Reactive where Base == ActivityAnnotationView {
  var tapTask: Observable<Task> {
    return base.rx.tapGesture().when(.recognized).flatMap {
      _ in
      return Observable.just(base.task)
    }
  }
}
