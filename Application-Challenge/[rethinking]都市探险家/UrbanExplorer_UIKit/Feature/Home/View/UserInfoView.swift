//
//  UserInfoView.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/23.
//

import UIKit
import SnapKit
import Kingfisher

class UserCharacterView: UIView {
  private lazy var typeLabel: UILabel = {
    let instance = UILabel()
    instance.font = UIFont.puhui(size: 11, weight: .medium)
    instance.textColor = UIColor.black.withAlphaComponent(0.4)
    instance.textAlignment = .center
    return instance
  }()
  private lazy var contentLabel: UILabel = {
    let instance = UILabel()
    instance.font = UIFont.puhui(size: 11, weight: .bold)
    instance.textColor = UIColor.black
    instance.textAlignment = .center
    return instance
  }()
  
  init(title: String) {
    super.init(frame: .zero)
    typeLabel.text = title
    addSubview(typeLabel)
    addSubview(contentLabel)
    
    typeLabel.snp.makeConstraints { (make) in
      make.top.equalToSuperview()
      make.left.right.equalToSuperview()
    }
    
    contentLabel.snp.makeConstraints { (make) in
      make.top.equalTo(typeLabel.snp.bottom)
      make.centerX.equalToSuperview()
      make.bottom.equalToSuperview()
    }
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func update(content: String) {
    contentLabel.text = content
  }
}

class UserInfoView: UIView {
  private lazy var container: UIView = {
    let instance = UIView()
    instance.backgroundColor = .white
    instance.layer.cornerRadius = 26
    instance.clipsToBounds = true
    return instance
  }()
  private lazy var taskView = UserCharacterView(title: "任务")
  private lazy var teamView = UserCharacterView(title: "组队")
  private lazy var levelView = UserCharacterView(title: "等级")
  private lazy var stackView: UIStackView = {
    let instance = UIStackView(arrangedSubviews: [taskView, teamView, levelView])
    instance.spacing = 42
    instance.distribution = .fillEqually
    instance.alignment = .center
    return instance
  }()
  private lazy var avatarImageView: UIImageView = {
    let instance = UIImageView()
    instance.layer.cornerRadius = 47.0/2
    instance.clipsToBounds = true
    return instance
  }()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    layer.shadowColor = UIColor.black.cgColor
    layer.shadowOffset = CGSize(width: 0, height: 4)
    layer.shadowRadius = 4
    layer.shadowOpacity = 0.1
    buildLayou()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  private func buildLayou() {
    addSubview(container)
    container.addSubview(avatarImageView)
    container.addSubview(stackView)
    
    container.snp.makeConstraints { (make) in
      make.edges.equalToSuperview().inset(4)
    }
    
    avatarImageView.snp.makeConstraints { (make) in
      make.size.equalTo(CGSize(width: 47, height: 47))
      make.left.equalToSuperview().offset(4)
      make.bottom.top.equalToSuperview().inset(4)
    }
    
    stackView.snp.makeConstraints { (make) in
      make.centerY.equalToSuperview()
      make.left.equalTo(avatarImageView.snp.right).offset(22)
      make.right.equalToSuperview().inset(34)
    }
  }
  
  public func update(user: User) {
    avatarImageView.image = user.avatarImage()
    taskView.update(content: "\(user.taskCount)")
    teamView.update(content: "\(user.teamCount)")
    levelView.update(content: "\(user.level)")
  }
}
