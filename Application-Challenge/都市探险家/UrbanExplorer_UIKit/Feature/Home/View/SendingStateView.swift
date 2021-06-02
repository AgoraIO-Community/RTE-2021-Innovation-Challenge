//
//  SendingStateView.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/30.
//

import UIKit

class SendingStateView: UIView {
  private lazy var sendingImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFill
    instance.image = R.image.sending_state_icon()
    return instance
  }()
  private lazy var sendingLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 22, weight: .medium)
    instance.textColor = .white
    instance.text = "任务发送中..."
    return instance
  }()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    addSubview(sendingImageView)
    addSubview(sendingLabel)
    sendingImageView.snp.makeConstraints { make in
      make.edges.equalToSuperview()
    }
    
    sendingLabel.snp.makeConstraints { make in
      make.center.equalToSuperview()
    }
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
}
