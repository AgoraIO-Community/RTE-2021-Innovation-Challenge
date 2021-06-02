//
//  AudioChatViewController.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/6/2.
//

import UIKit
import AgoraRtcKit
import RxSwift
import RxCocoa
import RxDataSources
import SVProgressHUD
import AgoraRtmKit

private struct Constants {
  static let cellPadding = 15
  static let cellWidth = (UIScreen.main.bounds.width - 7 * 15)/4
}

class AudioChatViewController: UIViewController {
  var disposeBag = DisposeBag()
  private lazy var container: UIView = {
    let instance = UIView()
    instance.layer.cornerRadius = 20
    instance.clipsToBounds = true
    instance.backgroundColor = .white
    return instance
  }()
  private lazy var collectionView: UICollectionView = {
    let layout = UICollectionViewFlowLayout()
    layout.itemSize = CGSize(width: Constants.cellWidth, height: Constants.cellWidth)
    layout.minimumLineSpacing = 15
    layout.minimumInteritemSpacing = 15
    layout.scrollDirection = .vertical
    let instance = UICollectionView(frame: .zero, collectionViewLayout: layout)
    instance.showsVerticalScrollIndicator = false
    instance.showsHorizontalScrollIndicator = false
    instance.backgroundColor = .white
    instance.register(cellType: AudioChatCollectionViewCell.self)
    instance.contentInset = UIEdgeInsets(top: 0, left: 15, bottom: 0, right: 15)
    return instance
  }()
  private let dataSource: RxCollectionViewSectionedReloadDataSource<AudioChatSection>
  private lazy var titleLabel: UILabel = {
    let instance = UILabel()
    instance.font = .puhui(size: 21, weight: .medium)
    instance.textColor = .black
    instance.text = "组队成员"
    return instance
  }()
  private lazy var closeButton: UIButton = {
    let instance = UIButton()
    instance.backgroundColor = .white
    instance.titleLabel?.font = .puhui(size: 15, weight: .medium)
    instance.setTitle("离开", for: .normal)
    instance.setTitleColor(UIColor(hexString: "#DB5B5B"), for: .normal)
    instance.layer.cornerRadius = 6
    instance.alpha = 0
    return instance
  }()
  fileprivate lazy var joinButton: UIButton = {
    let instance = UIButton()
    instance.setBackgroundImage(R.image.join_background(), for: .normal)
    instance.titleLabel?.font = .puhui(size: 15, weight: .medium)
    instance.setTitle("😄 加入语音", for: .normal)
    instance.setTitleColor(.white, for: .normal)
    return instance
  }()
  fileprivate lazy var quitButton: UIButton = {
    let instance = UIButton()
    instance.setBackgroundImage(R.image.close_audio_room_icon(), for: .normal)
    return instance
  }()
  var isCreator = false
  private lazy var audioKit: AgoraRtcEngineKit = {
    let instance = AgoraRtcEngineKit.sharedEngine(withAppId: AppConfig.agoraAudioKey, delegate: self)
    return instance
  }()
  fileprivate var sectionsSubject = PublishSubject<[AudioChatSection]>()
  var isQuit: BehaviorSubject = BehaviorSubject<Bool>(value: false)
  init() {
    self.dataSource = RxCollectionViewSectionedReloadDataSource<AudioChatSection> { (dataSource, collectionView, indexPath, item) -> UICollectionViewCell in
      let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: AudioChatCollectionViewCell.self)
      cell.updateCell(user: item)
      return cell
    }
    super.init(nibName: nil, bundle: nil)
    bind()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    buildLayout()
  }
  
  private func joinChannel() {
    
    audioKit.joinChannel(byToken: AppConfig.audioToken, channelId: AppConfig.audioChannelName, info: nil, uid: UInt(UserDefaults.standard.currentUser().phone) ?? 0) { channel, uid, elapsed in
      SVProgressHUD.showSuccess(withStatus: "加入语音成功")
      DispatchQueue.main.async {
        UIView.animate(withDuration: 0.3) {
          self.closeButton.alpha = 1
          self.joinButton.alpha = 0
        }
      }
    }
  }
  
  private func leaveChannel() {
    audioKit.leaveChannel { stats in
      SVProgressHUD.showSuccess(withStatus: "离开语音成功")
      DispatchQueue.main.async {
        UIView.animate(withDuration: 0.3) {
          self.closeButton.alpha = 0
          self.joinButton.alpha = 1
        }
      }
    }
  }
  
  private func buildLayout() {
    container.backgroundColor = .white
    view.addSubview(closeButton)
    view.addSubview(container)
    container.addSubview(titleLabel)
    container.addSubview(collectionView)
    view.addSubview(joinButton)
    container.addSubview(quitButton)
    
    closeButton.snp.makeConstraints { make in
      make.bottom.equalToSuperview().inset(60)
      make.left.right.equalToSuperview().inset(15)
      make.height.equalTo(44)
      make.centerX.equalToSuperview()
    }
    
    container.snp.makeConstraints { make in
      make.left.right.equalToSuperview().inset(15)
      make.bottom.equalTo(closeButton.snp.top).offset(-15)
    }
    
    titleLabel.snp.makeConstraints { make in
      make.top.equalToSuperview().offset(20)
      make.left.equalToSuperview().offset(15)
    }
    
    collectionView.snp.makeConstraints { make in
      make.left.right.equalToSuperview()
      make.top.equalTo(titleLabel.snp.bottom).offset(15)
      make.bottom.equalToSuperview().inset(15)
      make.height.equalTo(Constants.cellWidth * 2 + 15)
    }
    
    joinButton.snp.makeConstraints { make in
      make.left.right.equalToSuperview().inset(15)
      make.bottom.equalToSuperview().inset(60)
    }
    
    quitButton.snp.makeConstraints { make in
      make.right.top.equalToSuperview().inset(22)
    }
  }
  
  private func bind() {
    sectionsSubject.asObservable().bind(to: collectionView.rx.items(dataSource: dataSource)).disposed(by: disposeBag)
    joinButton.rx.tap.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.joinChannel()
    }).disposed(by: disposeBag)
    
    closeButton.rx.tap.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.leaveChannel()
    }).disposed(by: disposeBag)
    
    quitButton.rx.tap.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.showAlert()
    }).disposed(by: disposeBag)
  }
  
  private func showAlert() {
    let closeOrQuit = isCreator ? "关闭" : "退出"
    let alert = UIAlertController(title: "是否\(closeOrQuit)任务？", message: nil, preferredStyle: .alert)
    alert.addAction(UIAlertAction(title: "确定", style: .default, handler: { _ in
      self.leaveChannel()
      self.dismiss(animated: true) {
        self.isQuit.onNext(true)
      }
    }))
    alert.addAction(UIAlertAction(title: "取消", style: .cancel, handler: { _ in
      
    }))
    present(alert, animated: true, completion: nil)
  }
}

extension AudioChatViewController: AgoraRtcEngineDelegate {
  func rtcEngine(_ engine: AgoraRtcEngineKit, didOccurError errorCode: AgoraErrorCode) {
    debugPrint("connect agora audio error\(errorCode)")
  }
}

extension Reactive where Base == AudioChatViewController {
  var task: Binder<Task> {
    return Binder(self.base) {
      vc, task in
      let audioSections = [AudioChatSection(items: task.joinedMembers)]
      base.sectionsSubject.onNext(audioSections)
    }
  }
  
  var joinAudio: Observable<Void> {
    return base.joinButton.rx.tap.asObservable()
  }
}
