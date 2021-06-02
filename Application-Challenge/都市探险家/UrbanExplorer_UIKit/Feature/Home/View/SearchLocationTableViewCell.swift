//
//  SearchLocationTableViewCell.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/29.
//

import UIKit
import Reusable

class SearchLocationTableViewCell: UITableViewCell, Reusable {
  private lazy var nameLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 17, weight: .medium)
    instance.textColor = .black
    return instance
  }()
  private lazy var addressLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 10, weight: .medium)
    instance.textColor = .black.withAlphaComponent(0.5)
    return instance
  }()
  var flagImageView = UIImageView(image: R.image.black_flag_icon())
  
  override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
    super.init(style: style, reuseIdentifier: reuseIdentifier)
    selectionStyle = .none
    buildLayout()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  private func buildLayout() {
    backgroundColor = .white
    flagImageView.isHidden = true
    contentView.addSubview(nameLabel)
    contentView.addSubview(addressLabel)
    contentView.addSubview(flagImageView)
    
    flagImageView.snp.makeConstraints { make in
      make.centerY.equalToSuperview()
      make.right.equalToSuperview().inset(14)
    }
    
    nameLabel.snp.makeConstraints { make in
      make.left.equalToSuperview().offset(30)
      make.top.equalToSuperview().offset(14)
    }
    
    addressLabel.snp.makeConstraints { make in
      make.left.equalTo(nameLabel)
      make.right.equalTo(flagImageView.snp.left).inset(30)
      make.top.equalTo(nameLabel.snp.bottom).offset(2)
      make.bottom.equalToSuperview().inset(14)
    }
  }
  
  override func setSelected(_ selected: Bool, animated: Bool) {
    super.setSelected(selected, animated: animated)
    flagImageView.isHidden = !isSelected
  }
  
  func updateCell(name: String, address: String) {
    nameLabel.text = name
    addressLabel.text = address
  }
}
