//
//  HomeViewController.swift
//  UrbanExplorer_UIKit
//
//  Created by zang qilong on 2021/5/23.
//

import UIKit
import Mapbox
import ReactorKit
import RxSwift
import RxViewController
import RxCoreLocation
import CoreLocation
import AgoraRtmKit
import RxDataSources
import SVProgressHUD

class HomeViewController: UIViewController, View {
  var disposeBag: DisposeBag = DisposeBag()
  private lazy var mapView: MGLMapView = {
    let instance = MGLMapView(frame: .zero, styleURL: MGLStyle.lightStyleURL)
    instance.showsUserLocation = true
    instance.delegate = self
    return instance
  }()
  private lazy var userInfoView: UserInfoView = {
    let instance = UserInfoView()
    return instance
  }()
  private lazy var collectionView: UICollectionView = {
    let layout = UICollectionViewFlowLayout()
    let instance = UICollectionView(frame: .zero, collectionViewLayout: layout)
    instance.showsVerticalScrollIndicator = false
    instance.showsHorizontalScrollIndicator = false
    return instance
  }()
  private lazy var dataSource: RxCollectionViewSectionedReloadDataSource<TaskSection> = {
    return RxCollectionViewSectionedReloadDataSource<TaskSection> { (dataSource, collectionView, indexPath, item) -> UICollectionViewCell in
      //   let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: UICollectionViewCell.self)
      return UICollectionViewCell()
    }
  }()
  private var isCreator = false {
    didSet {
      chatVC.isCreator = isCreator
    }
  }
  private let sendingStateView = SendingStateView(frame: .zero)
  private let createActivityView = CreateActivityView(frame: .zero)
  private let selectedTaskView = SelectedTaskView(frame: .zero)
  private let locationManager = CLLocationManager()
  private let maleCount = BehaviorSubject<Int>(value: 0)
  private let femaleCount = BehaviorSubject<Int>(value: 0)
  private let newTask = PublishSubject<Task>()
  private let receiveJoinTask = PublishSubject<JoinTask>()
  private var targetView: ActivityAnnotationView?
  private let targetAnnotationIdentifier = "targetAnnotationIdentifier"
  private let taskView = TaskView()
  private let channelId = "UrbanExplorerChatChannel"
  private var channel: AgoraRtmChannel?
  private var chatVC = AudioChatViewController()
  
  init() {
    super.init(nibName: nil, bundle: nil)
    self.reactor = HomeReactor()
  }
  
  required init?(coder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override func viewDidLoad() {
    super.viewDidLoad()
    buildLayout()
    userInfoView.update(user: User.mock())
    self.locationManager.requestWhenInUseAuthorization()
    self.locationManager.startUpdatingLocation()
  }
  
  override func viewDidAppear(_ animated: Bool) {
    super.viewDidAppear(animated)
    if !UserDefaults.standard.isUserValiable() {
      let vc = UserLoginViewController()
      vc.modalPresentationStyle = .currentContext
      vc.modalTransitionStyle = .coverVertical
      present(UserLoginViewController(), animated: true, completion: nil)
    } else {
      userInfoView.update(user: UserDefaults.standard.currentUser())
      login()
    }
    
    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + .seconds(5)) {
      self.locationManager.stopUpdatingLocation()
    }
  }
  
  private func buildLayout() {
    taskView.alpha = 0
    sendingStateView.alpha = 0
    view.addSubview(mapView)
    view.addSubview(taskView)
    view.addSubview(userInfoView)
    view.addSubview(sendingStateView)
    view.addSubview(createActivityView)
    view.addSubview(selectedTaskView)
    
    mapView.snp.makeConstraints { (make) in
      make.edges.equalToSuperview()
    }
    
    userInfoView.snp.makeConstraints { (make) in
      make.centerX.equalToSuperview()
      make.top.equalToSuperview().offset(57)
    }
    
    createActivityView.snp.makeConstraints { (make) in
      make.left.right.bottom.equalToSuperview()
    }
    
    taskView.snp.makeConstraints { make in
      make.left.right.equalToSuperview().inset(15)
      make.center.equalTo(createActivityView)
    }
    
    sendingStateView.snp.makeConstraints { make in
      make.size.equalTo(CGSize(width: 223, height: 61))
      make.centerX.equalToSuperview()
      make.bottom.equalTo(createActivityView).inset(42)
    }
    
    selectedTaskView.snp.makeConstraints { make in
      make.left.right.equalToSuperview().inset(40)
      make.top.equalTo(view.snp.bottom)
      make.height.equalTo(130)
    }
  }
  
  func bind(reactor: HomeReactor) {
    reactor.state
      .filter {
        $0.selectedLocation == nil && $0.receivedTask == nil
      }
      .compactMap(\.currentCoorindate)
      .bind(to: mapView.rx.centerCoorindate)
      .disposed(by: disposeBag)
    
    createActivityView.rx
      .activityName
      .map {
        Reactor.Action.inputActivityName($0)
      }
      .bind(to: reactor.action)
      .disposed(by: disposeBag)
    
    maleCount
      .asObservable()
      .map {
        Reactor.Action.inputMaleCount($0)
      }
      .bind(to: reactor.action)
      .disposed(by: disposeBag)
    
    femaleCount
      .asObservable()
      .map {
        Reactor.Action.inputFemaleCount($0)
      }
      .bind(to: reactor.action)
      .disposed(by: disposeBag)
    
    reactor.state
      .map(\.maleCount)
      .distinctUntilChanged()
      .bind(to: createActivityView.rx.maleCount)
      .disposed(by: disposeBag)
    
    reactor.state
      .map(\.femaleCount)
      .distinctUntilChanged()
      .bind(to: createActivityView.rx.femaleCount)
      .disposed(by: disposeBag)
    
    reactor.state
      .compactMap(\.selectedLocation)
      .map(\.name)
      .bind(to: createActivityView.rx.activityLocation)
      .disposed(by: disposeBag)
    
    reactor.state
      .compactMap(\.selectedLocation)
      .subscribe(onNext: {
        [weak self] value in
        guard let self = self else { return }
        self.flyToCoordinate(coordinate: value.coordinate)
      }).disposed(by: disposeBag)
    
    locationManager.rx.location.compactMap {
      $0?.coordinate
    }.map {
      Reactor.Action.userLocationChanged($0)
    }
    .bind(to: reactor.action)
    .disposed(by: disposeBag)
    
    createActivityView.rx.locationFiedDidClick.flatMap { _ -> Observable<SearchLocation> in
      let vc = SearchLocationViewController()
      vc.modalPresentationStyle = .popover
      vc.modalTransitionStyle = .coverVertical
      self.present(vc, animated: true, completion: nil)
      return vc.rx.selectedItem
    }.map {
      Reactor.Action.selectLocation($0)
    }
    .bind(to: reactor.action)
    .disposed(by: disposeBag)
    
    createActivityView.rx
      .tapToNextState
      .filter {
        $0 == .finish
      }
      .map {
        _ in
        return Reactor.Action.sendTask
      }
      .bind(to: reactor.action)
      .disposed(by: disposeBag)
    
    reactor.state
      .compactMap(\.createdTask)
      .distinctUntilChanged()
      .do { task in
        let coordinate = CLLocationCoordinate2D(latitude: CLLocationDegrees(task.latitude), longitude: CLLocationDegrees(task.longitude))
        let annotation = TaskAnnotation(coordinate: coordinate, task: task)
        annotation.title = task.name
        self.mapView.removeAnnotations(self.mapView.visibleAnnotations ?? [])
        self.mapView.addAnnotation(annotation)
        self.showSendingAnimation()
        self.sendTaskMessage(task)
        self.isCreator = true
      }
      .bind(to: taskView.rx.taskBinder)
      .disposed(by: disposeBag)
    
    reactor.state.compactMap(\.receivedTask).distinctUntilChanged().flatMap {
      task -> Observable<MGLPointAnnotation> in
      let coordinate = CLLocationCoordinate2D(latitude: CLLocationDegrees(task.latitude), longitude: CLLocationDegrees(task.longitude))
      let annotation = TaskAnnotation(coordinate: coordinate, task: task)
      annotation.title = task.name
      return Observable.just(annotation)
    }.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.flyToCoordinate(coordinate: value.coordinate)
      self.mapView.addAnnotation(value)
    }).disposed(by: disposeBag)
    
    createActivityView.rx.tapMaleView.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.showCountActionSheet(type: .male)
    }).disposed(by: disposeBag)
    
    createActivityView.rx.tapFemaleView.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.showCountActionSheet(type: .female)
    }).disposed(by: disposeBag)
    
    reactor.state.compactMap(\.error).distinctUntilChanged().map(\.message).bind(to: SVProgressHUD.rx.errorStatus).disposed(by: disposeBag)
    
    newTask.asObservable()
      .map {
        Reactor.Action.findTask($0)
      }
      .bind(to: reactor.action)
      .disposed(by: disposeBag)
    
    NotificationCenter.default.rx.notification(Notification.Name("InputUserInfoSuccess")).subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.userInfoView.update(user: UserDefaults.standard.currentUser())
      self.login()
    }).disposed(by: disposeBag)
    
    reactor.state.compactMap(\.selectedTask)
      .subscribe(onNext: {
        [weak self] task in
        guard let self = self else { return }
        self.showSelectedTaskView(task)
      }).disposed(by: disposeBag)
    
    Observable.combineLatest(taskView.rx.tapGesture(), selectedTaskView.rx.tapGesture())
      .subscribe(onNext: {
        [weak self] value in
        guard let self = self else { return }
        self.present(self.chatVC, animated: true, completion: nil)
      }).disposed(by: disposeBag)

    receiveJoinTask.asObservable().map {
      Reactor.Action.findNewJoinTask($0)
    }
    .bind(to: reactor.action)
    .disposed(by: disposeBag)
    
    reactor.state.compactMap {
      if self.isCreator {
        return $0.createdTask
      } else {
        return $0.receivedTask
      }
    }.distinctUntilChanged()
      .bind(to: chatVC.rx.task)
      .disposed(by: disposeBag)
    
    chatVC.rx.joinAudio.flatMap {
      reactor.state
    }
    .compactMap(\.receivedTask)
    .subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.joinTask(taskId: value.id)
    }).disposed(by: disposeBag)
    
    chatVC.isQuit.asObservable().filter {
      $0
    }.subscribe(onNext: {
      [weak self] value in
      guard let self = self else { return }
      self.targetView?.stop()
      self.mapView.removeAnnotations(self.mapView.visibleAnnotations ?? [])
      self.reset()
    }).disposed(by: disposeBag)
  }
  
  private func flyToCoordinate(coordinate: CLLocationCoordinate2D) {
    let camera = MGLMapCamera(lookingAtCenter: coordinate, altitude: 1500, pitch: 15, heading: 180)
    mapView.setCamera(camera, withDuration: 2, animationTimingFunction: CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeInEaseOut))
  }
  
  private func showSendingAnimation() {
    self.targetView?.start()
    UIView.animate(withDuration: 0.5) {
      self.taskView.alpha = 1
      self.sendingStateView.alpha = 1
      self.createActivityView.alpha = 0
    } completion: { isfinished in
      if isfinished {
        self.taskView.snp.remakeConstraints { make in
          make.left.right.equalToSuperview().inset(15)
          make.top.equalTo(self.userInfoView.snp.bottom).offset(-20)
        }
        UIView.animate(withDuration: 1, delay: 0, options: .curveEaseInOut) {
          self.view.layoutIfNeeded()
        } completion: { _ in
          
        }
      }
    }
    
  }
  
  private func showCountActionSheet(type: ActivityCountView.ActivityCountType) {
    let vc = UIAlertController(title: "选择人数", message: nil, preferredStyle: .actionSheet)
    for i in 1...8 {
      vc.addAction(UIAlertAction(title: "\(i)", style: .default, handler: { _ in
        if type == .male {
          self.maleCount.onNext(i)
        } else {
          self.femaleCount.onNext(i)
        }
      }))
    }
    vc.addAction(UIAlertAction(title: "取消", style: .cancel, handler: { _ in
      
    }))
    present(vc, animated: true, completion: nil)
  }
  
  private func showSelectedTaskView(_ task: Task) {
    selectedTaskView.updateCell(task: task)
    createActivityView.snp.remakeConstraints { make in
      make.left.right.equalToSuperview()
      make.top.equalTo(view.snp.bottom)
    }
    
    UIView.animate(withDuration: 0.5) {
      self.view.layoutIfNeeded()
    } completion: { _ in
      self.selectedTaskView.snp.remakeConstraints { make in
        make.left.right.equalToSuperview().inset(40)
        make.bottom.equalTo(self.view.snp.bottom).inset(60)
        make.height.equalTo(130)
      }
      
      UIView.animate(withDuration: 0.5) {
        self.view.layoutIfNeeded()
      }
    }
  }
  
  private func reset() {
    createActivityView.reset()
    createActivityView.snp.remakeConstraints { (make) in
      make.left.right.bottom.equalToSuperview()
    }
    taskView.snp.remakeConstraints { make in
      make.left.right.equalToSuperview().inset(15)
      make.center.equalTo(createActivityView)
    }
    
    sendingStateView.snp.remakeConstraints { make in
      make.size.equalTo(CGSize(width: 223, height: 61))
      make.centerX.equalToSuperview()
      make.bottom.equalTo(createActivityView).inset(42)
    }
    
    selectedTaskView.snp.remakeConstraints { make in
      make.left.right.equalToSuperview().inset(40)
      make.top.equalTo(view.snp.bottom)
      make.height.equalTo(130)
    }
    
    UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.8, initialSpringVelocity: 5, options: .curveEaseInOut) {
      self.view.layoutIfNeeded()
      self.taskView.alpha = 0
      self.sendingStateView.alpha = 0
    } completion: { _ in
      UIView.animate(withDuration: 0.3) {
        self.createActivityView.alpha = 1
      }
    }
  }
  
  private func login() {
    AgoraRtm.updateKit(delegate: self)
    AgoraRtm.current = UserDefaults.standard.currentUser().phone
    // AgoraRtm.oneToOneMessageType = enableOneToOneSwitch.isOn ? .offline : .normal
    let UserId = UUID().uuidString
    AgoraRtm.kit?.login(byToken: nil, user: UserId) { [unowned self] (errorCode) in
      guard errorCode == .ok else {
        SVProgressHUD.showError(withStatus: "login error: \(errorCode.rawValue)")
        return
      }
      AgoraRtm.status = .online
      DispatchQueue.main.async {
        SVProgressHUD.showSuccess(withStatus: "信息登录成功")
        self.createChannel()
      }
    }
  }
  
  private func createChannel() {
    channel = AgoraRtm.kit?.createChannel(withId: channelId, delegate: self)
    guard channel != nil else {
      SVProgressHUD.showError(withStatus: "创建频道失败")
      return
    }
    joinChannel()
  }
  
  private func joinChannel() {
    channel?.join(completion: { error in
      if error != .channelErrorOk{
        SVProgressHUD.showError(withStatus: "join channel error: \(error.rawValue)")
      }
    })
  }
  
  private func sendTaskMessage(_ task: Task) {
    guard let data = try? JSONEncoder().encode(task) else {
      SVProgressHUD.showError(withStatus: "序列化任务错误")
      return
    }
    let message = AgoraRtmRawMessage(rawData: data, description: "")
    channel?.send(message, completion: { error in
      
    })
  }
  
  private func joinTask(taskId: String) {
    let joinTask = JoinTask(taskId: taskId, user: UserDefaults.standard.currentUser(), isJoin: true)
    guard let data = try? JSONEncoder().encode(joinTask) else {
      SVProgressHUD.showError(withStatus: "序列化任务错误")
      return
    }
    let message = AgoraRtmRawMessage(rawData: data, description: "")
    channel?.send(message, completion: { error in
      
    })
  }
}

extension HomeViewController: MGLMapViewDelegate {
  func mapView(_ mapView: MGLMapView, viewFor annotation: MGLAnnotation) -> MGLAnnotationView? {
    guard let taskAnnotation = annotation as? TaskAnnotation else {
      return nil
    }
    var annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: targetAnnotationIdentifier) as? ActivityAnnotationView
    if let activityView = annotationView {
      activityView.update(taskAnnotation: taskAnnotation)
    } else {
      targetView = ActivityAnnotationView(annotation: taskAnnotation, reuseIdentifier: targetAnnotationIdentifier)
      annotationView = targetView
      annotationView?.bounds = CGRect(x: 0, y: 0, width: 120, height: 72)
    }
    annotationView!.rx.tapTask
      .map { task in
        Reactor.Action.selectTaskAnnotation(task)
      }
      .bind(to: reactor!.action)
      .disposed(by: annotationView!.disposeBag)
    return annotationView
  }
  
  func mapView(_ mapView: MGLMapView, shapeAnnotationIsEnabled annotation: MGLShape) -> Bool {
    return true
  }
  
  func mapView(_ mapView: MGLMapView, didSelect annotation: MGLAnnotation) {
    debugPrint("did select annotation")
  }
  
  func mapView(_ mapView: MGLMapView, didSelect annotationView: MGLAnnotationView) {
    debugPrint("did select annotation view")
  }
  
  func mapView(_ mapView: MGLMapView, annotationCanShowCallout annotation: MGLAnnotation) -> Bool {
    return true
  }
  
  func mapView(_ mapView: MGLMapView, calloutViewFor annotation: MGLAnnotation) -> MGLCalloutView? {
    // Instantiate and return our custom callout view.
    guard let taskAnnotation = annotation as? TaskAnnotation else {
      return nil
    }
    return TaskCalloutView(representedObject: taskAnnotation)
  }
  
  func mapView(_ mapView: MGLMapView, tapOnCalloutFor annotation: MGLAnnotation) {
    // Optionally handle taps on the callout.
    print("Tapped the callout for: \(annotation)")
    
    // Hide the callout.
    mapView.deselectAnnotation(annotation, animated: true)
  }
}

extension HomeViewController: AgoraRtmDelegate {
  func rtmKit(_ kit: AgoraRtmKit, messageReceived message: AgoraRtmMessage, fromPeer peerId: String) {
    AgoraRtm.add(offlineMessage: message, from: peerId)
    
  }
}

extension HomeViewController: AgoraRtmChannelDelegate {
  func channel(_ channel: AgoraRtmChannel, messageReceived message: AgoraRtmMessage, from member: AgoraRtmMember) {
    if let rawDataMessage = message as? AgoraRtmRawMessage {
      if let task = try? JSONDecoder().decode(Task.self, from: rawDataMessage.rawData) {
        SVProgressHUD.showInfo(withStatus: "收到task")
        newTask.onNext(task)
      }
      if let joinTask = try? JSONDecoder().decode(JoinTask.self, from: rawDataMessage.rawData) {
        receiveJoinTask.onNext(joinTask)
      }
    }
  }
}
