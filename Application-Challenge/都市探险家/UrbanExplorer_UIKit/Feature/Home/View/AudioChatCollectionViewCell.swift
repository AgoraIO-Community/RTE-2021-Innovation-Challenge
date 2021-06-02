//
//  AudoChatCollectionViewCell.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/6/2.
//

import UIKit
import Reusable

class AudioChatCollectionViewCell: UICollectionViewCell, Reusable {
  private lazy var avatarImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = R.image.append_user_icon()
    return instance
  }()
  private lazy var micImageView: UIImageView = {
    let instance = UIImageView()
    instance.contentMode = .scaleAspectFit
    instance.image = R.image.mic_icon()
    return instance
  }()
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    layer.cornerRadius = 20
    clipsToBounds = true
    backgroundColor = .clear
    contentView.addSubview(avatarImageView)
    contentView.addSubview(micImageView)
    
    avatarImageView.snp.makeConstraints { make in
      make.edges.equalToSuperview()
    }
    
    micImageView.snp.makeConstraints { make in
      make.bottom.right.equalToSuperview()
    }
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func updateCell(user: User) {
    avatarImageView.image = user.avatarImage()
  }
}
