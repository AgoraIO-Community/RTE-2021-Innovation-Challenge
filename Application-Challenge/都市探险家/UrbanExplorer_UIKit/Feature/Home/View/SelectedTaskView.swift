//
//  TaskCollectionViewCell.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/6/1.
//

import UIKit

class SelectedTaskView: UIView {
    private lazy var avatarImageView: UIImageView = {
        let instance = UIImageView()
        instance.contentMode = .scaleAspectFit
        instance.image = nil
        return instance
    }()
    private lazy var nameLabel: UILabel = {
        let instance = UILabel()
        instance.font = .puhui(size: 18, weight: .medium)
        instance.textColor = .black
        return instance
    }()
    private lazy var levelLabel: UILabel = {
        let instance = UILabel()
        instance.font = .puhui(size: 10, weight: .medium)
        instance.textColor = .black.withAlphaComponent(0.4)
        return instance
    }()
    private lazy var taskIconImageView: UIImageView = {
        let instance = UIImageView()
        instance.contentMode = .scaleAspectFit
        instance.image = R.image.new_task_icon()
        return instance
    }()
    private lazy var taskLabel: UILabel = {
        let instance = UILabel()
        instance.font = .puhui(size: 13, weight: .medium)
        instance.textColor = UIColor(hexString: "#45D297")
        instance.text = "新的探险"
        return instance
    }()
    private lazy var taskNameLabel: UILabel = {
        let instance = UILabel()
        instance.font = .puhui(size: 18, weight: .medium)
        instance.textColor = .black
        return instance
    }()
    private lazy var joinLabel: UILabel = {
        let instance = UILabel()
        instance.font = .puhui(size: 12, weight: .medium)
        instance.textColor = .black.withAlphaComponent(0.4)
        return instance
    }()
    private lazy var locationImageView: UIImageView = {
        let instance = UIImageView()
        instance.contentMode = .scaleAspectFit
        instance.image = R.image.location_icon()
        return instance
    }()
    private lazy var locationLabel: UILabel = {
        let instance = UILabel()
        instance.font = .puhui(size: 10, weight: .medium)
        instance.textColor = .black.withAlphaComponent(0.4)
        return instance
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        addSubview(avatarImageView)
        addSubview(nameLabel)
        addSubview(levelLabel)
        addSubview(taskIconImageView)
        addSubview(taskLabel)
        addSubview(taskNameLabel)
        addSubview(joinLabel)
        addSubview(locationImageView)
        addSubview(locationLabel)
        buildLayout()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func buildLayout() {
        layer.cornerRadius = 10
        clipsToBounds = true
      backgroundColor = .white
        avatarImageView.snp.makeConstraints { make in
            make.size.equalTo(CGSize(width: 54, height: 54))
            make.left.top.equalToSuperview().offset(13)
        }
        
        nameLabel.snp.makeConstraints { make in
            make.top.equalTo(avatarImageView.snp.bottom).offset(3)
            make.centerX.equalTo(avatarImageView)
        }
        
        levelLabel.snp.makeConstraints { make in
            make.top.equalTo(nameLabel.snp.bottom)
            make.centerX.equalTo(nameLabel)
        }
        
        taskIconImageView.snp.makeConstraints { make in
            make.left.equalTo(avatarImageView.snp.right).offset(23)
            make.top.equalToSuperview().offset(10)
        }
        
        taskLabel.snp.makeConstraints { make in
            make.left.equalTo(taskIconImageView.snp.right).offset(5)
            make.centerY.equalTo(taskIconImageView)
        }
        
        taskNameLabel.snp.makeConstraints { make in
            make.top.equalTo(taskIconImageView.snp.bottom).offset(3)
            make.left.equalTo(taskIconImageView)
            make.right.equalToSuperview().inset(15)
        }
        
        joinLabel.snp.makeConstraints { make in
            make.left.equalTo(taskNameLabel)
            make.top.equalTo(taskNameLabel.snp.bottom).offset(10)
        }
        
        locationImageView.snp.makeConstraints { make in
            make.top.equalTo(joinLabel.snp.bottom).offset(10)
            make.left.equalTo(joinLabel)
        }
        
        locationLabel.snp.makeConstraints { make in
            make.left.equalTo(locationImageView.snp.right).offset(5)
            make.centerY.equalTo(locationImageView)
        }
    }
    
    public func updateCell(task: Task) {
      avatarImageView.image = task.owenr.avatarImage()
      nameLabel.text = task.owenr.name
      levelLabel.text = "等级" + "\(task.owenr.level)"
      taskNameLabel.text = task.name
      joinLabel.text = "已组队: \(task.joinedMembers.count)/\(task.maleCount + task.femaleCount)"
      locationLabel.text = task.locationName
    }
}
