//
//  TaskView.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/29.
//

import UIKit
import RxSwift

class TaskView: UIView {
  private lazy var container: UIView = {
    let instance = UIView()
    instance.backgroundColor = .white
    instance.layer.cornerRadius = 20
    instance.clipsToBounds = true
    return instance
  }()
  private lazy var taskImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = R.image.new_task_icon()
    return instance
  }()
  fileprivate lazy var taskLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 13, weight: .medium)
    instance.textColor = UIColor(hexString: "#45D297")
    instance.text = "新探险"
    return instance
  }()
  fileprivate lazy var taskNameLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 17, weight: .medium)
    instance.textColor = .black
    return instance
  }()
  private lazy var locationImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = R.image.location_icon()
    return instance
  }()
  fileprivate lazy var locationNameLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 13, weight: .medium)
    instance.textColor = .black
    instance.text = ""
    return instance
  }()
  private lazy var maleImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = R.image.male_icon()
    return instance
  }()
  fileprivate lazy var maleCountLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 13, weight: .medium)
    instance.textColor = .black
    instance.text = ""
    return instance
  }()
  private lazy var femaleImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = R.image.female_icon()
    return instance
  }()
  fileprivate lazy var femaleCountLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 13, weight: .medium)
    instance.textColor = .black
    instance.text = ""
    return instance
  }()
  private lazy var ageTitleLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 13, weight: .medium)
    instance.textColor = .black.withAlphaComponent(0.5)
    instance.text = "年龄"
    return instance
  }()
  fileprivate lazy var ageLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 13, weight: .medium)
    instance.textColor = .black.withAlphaComponent(0.7)
    instance.text = ""
    return instance
  }()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    buildLayout()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  private func buildLayout() {
    addSubview(container)
    container.addSubview(taskImageView)
    container.addSubview(taskLabel)
    container.addSubview(taskNameLabel)
    container.addSubview(locationImageView)
    container.addSubview(locationNameLabel)
    container.addSubview(maleImageView)
    container.addSubview(maleCountLabel)
    container.addSubview(femaleImageView)
    container.addSubview(femaleCountLabel)
    container.addSubview(ageTitleLabel)
    container.addSubview(ageLabel)
    
    container.snp.makeConstraints { make in
      make.edges.equalToSuperview()
    }
    
    taskImageView.snp.makeConstraints { make in
      make.left.equalToSuperview().offset(25)
      make.top.equalToSuperview().offset(35)
    }
    
    taskLabel.snp.makeConstraints { make in
      make.left.equalTo(taskImageView.snp.right).offset(6)
      make.centerY.equalTo(taskImageView)
    }
    
    taskNameLabel.snp.makeConstraints { make in
      make.top.equalTo(taskImageView.snp.bottom).offset(6)
      make.left.equalToSuperview().offset(25)
    }
    
    locationImageView.snp.makeConstraints { make in
      make.left.equalTo(taskNameLabel)
      make.top.equalTo(taskNameLabel.snp.bottom).offset(12)
      make.bottom.equalToSuperview().inset(50)
    }
    
    locationNameLabel.snp.makeConstraints { make in
      make.left.equalTo(locationImageView.snp.right).offset(10)
      make.centerY.equalTo(locationImageView)
      make.width.lessThanOrEqualTo(100)
    }
    
    maleImageView.snp.makeConstraints { make in
      make.left.equalTo(locationNameLabel.snp.right).offset(27)
      make.centerY.equalTo(locationNameLabel)
    }
    
    maleCountLabel.snp.makeConstraints { make in
      make.left.equalTo(maleImageView.snp.right).offset(6)
      make.centerY.equalTo(maleImageView)
    }
    
    femaleImageView.snp.makeConstraints { make in
      make.left.equalTo(maleCountLabel.snp.right).offset(20)
      make.centerY.equalTo(maleCountLabel)
    }
    
    femaleCountLabel.snp.makeConstraints { make in
      make.left.equalTo(femaleImageView.snp.right).offset(6)
      make.centerY.equalTo(femaleImageView)
    }
    
    ageTitleLabel.snp.makeConstraints { make in
      make.left.equalTo(femaleCountLabel.snp.right).offset(20)
      make.centerY.equalTo(femaleCountLabel)
    }
    
    ageLabel.snp.makeConstraints { make in
      make.left.equalTo(ageTitleLabel.snp.right).offset(6)
      make.centerY.equalTo(ageTitleLabel)
    }
  }
  
  func update(task: Task) {
    taskNameLabel.text = task.name
    locationNameLabel.text = task.locationName
    maleCountLabel.text = "\(task.maleCount)"
    femaleCountLabel.text = "\(task.femaleCount)"
    ageLabel.text = task.range
  }
}

extension Reactive where Base == TaskView {
  var taskBinder: Binder<Task> {
    return Binder(self.base) {
      view, task in
      view.taskNameLabel.text = task.name
      view.locationNameLabel.text = task.locationName
      view.maleCountLabel.text = "\(task.maleCount)"
      view.femaleCountLabel.text = "\(task.femaleCount)"
      view.ageLabel.text = task.range
    }
  }
}
