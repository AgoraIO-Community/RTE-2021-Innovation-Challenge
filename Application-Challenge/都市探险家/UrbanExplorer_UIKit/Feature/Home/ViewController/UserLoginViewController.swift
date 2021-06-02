//
//  UserLoginViewController.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/6/1.
//

import UIKit
import RxSwift
import RxCocoa
import SVProgressHUD

class UserLoginViewController: UIViewController {
  private var disposeBag = DisposeBag()
  @IBOutlet weak var loginButton: UIButton!
  @IBOutlet weak var userNameTextField: UITextField!
  @IBOutlet weak var phoneTextField: UITextField!
  @IBOutlet weak var avatarButton3: UIButton!
  @IBOutlet weak var avatarButton2: UIButton!
  @IBOutlet weak var avatarButton1: UIButton!
  
  private var user = User(phone: "", avatarIndex: 0, name: "", taskCount: 1, teamCount: 1, level: 1)
  
  override func viewDidLoad() {
    super.viewDidLoad()
    bind()
  }
  
  func bind() {
    avatarButton1.rx.tap.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.avatarButton1.alpha = 1
      self.avatarButton2.alpha = 0.5
      self.avatarButton3.alpha = 0.5
      self.user.avatarIndex = 1
    }).disposed(by: disposeBag)
    
    avatarButton2.rx.tap.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.avatarButton1.alpha = 0.5
      self.avatarButton2.alpha = 1
      self.avatarButton3.alpha = 0.5
      self.user.avatarIndex = 2
    }).disposed(by: disposeBag)
    
    avatarButton3.rx.tap.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.avatarButton1.alpha = 0.5
      self.avatarButton2.alpha = 0.5
      self.avatarButton3.alpha = 1
      self.user.avatarIndex = 3
    }).disposed(by: disposeBag)
    
    userNameTextField.rx.text.orEmpty.distinctUntilChanged().asObservable().subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.user.name = value
    }).disposed(by: disposeBag)
    
    phoneTextField.rx.text.orEmpty.distinctUntilChanged().asObservable().subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.user.phone = value
    }).disposed(by: disposeBag)
    
    loginButton.rx.tap.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      guard self.user.avatarIndex > 0, !self.user.name.isEmpty, !(self.user.phone.isEmpty) else {
        SVProgressHUD.showError(withStatus: "请填写完整信息和正确的手机号")
        return
      }
      UserDefaults.standard.saveUser(user: self.user)
      NotificationCenter.default.post(name: NSNotification.Name("InputUserInfoSuccess"), object: nil)
      self.dismiss(animated: true, completion: nil)
    }).disposed(by: disposeBag)
  }
}
