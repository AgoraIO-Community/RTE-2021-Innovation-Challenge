//
//  QHChatBaseProtocol.h
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/29.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#ifndef QHChatBaseProtocol_h
#define QHChatBaseProtocol_h

@protocol QHChatBaseNewDataViewProtcol <NSObject>

- (void)show;
// 返回是否有隐藏更多的操作，拉下底部时通过此来判断是否刷新数据
- (BOOL)hide;

/**
 刷新，此协议可以类似更新更多View 的显示操作

 @param data 数据
 */
- (void)update:(id)data;

@end

@protocol QHChatBaseViewProtocol <NSObject>

/**
 初始化 ChatView 之后调用，可在此生命周期进行自定义操作
 */
- (void)qhChatCustomChatViewSetup;

/**
 初始化 ChatView - tableView 之后，进行 指定Cell 加载

 @param tableView 公屏
 */
- (void)qhChatAddCell2TableView:(UITableView *)tableView;

/**
 解析内容，并转化为 NSMutableAttributedString（一定要转化这个类型）

 @param data 数据
 @return 结果
 */
- (NSMutableAttributedString *)qhChatAnalyseContent:(NSDictionary *)data;

/**
 自定义 更多View，但必须实现 QHChatBaseNewDataViewProtcol，不支持扩展，可直接继承 QHChatBaseNewDataView

 @return view
 */
- (UIView<QHChatBaseNewDataViewProtcol> *)qhChatTakeHasNewDataView;

/**
 计算高度

 @param tableView 公屏
 @param indexPath 位置
 @return 高度
 */
- (CGFloat)qhChatAnalyseHeight:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 创建自定义 Cell，类似 UITableView 的 delegate 的
 - (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath;

 @param tableView 公屏
 @param indexPath 位置
 @return Cell
 */
- (UITableViewCell *)qhChatChatView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 针对 QHChatBaseViewCell 修改，可改背景，自定义较少，建议修改多的话使用自定义Cell

 @param cell QHChatBaseViewCell对象
 @param indexPath 位置
 */
- (void)qhChatMakeAfterChatBaseViewCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath;

/**
 Content NSMutableAttributedString 的最后设置，使用config配置的字号，行高，如果是自定义，并且不使用config，记得计算行高时也要重写

 @param attr 属性字符串
 */
- (void)qhChatAddCellDefualAttributes:(NSMutableAttributedString *)attr;

/**
 数据字典转为公屏 model 时候，可进行处理，如入场，加入的与最后的一样，就可执行替换

 @param newData 新数据
 @param lastData 最后的数据
 @return YES 则 Replace，NO 则 Add
 */
- (BOOL)qhChatUseReplace:(NSDictionary *)newData old:(NSDictionary *)lastData;

/*
 如果想 insert 数据时判断，建议自定义 insert 函数，即调用 insert 时进行数据分析，添加需要的数据
 */

/*
 长按响应的接口重写，这是继承 QHChatBaseViewCell 时，开启默认的长按手势才会触发，配置为 QHChatBaseConfig.bLongPress
 如果是自定义 cell 的 tap 则自行实现。
 */
- (void)qhlongPressAction:(UILongPressGestureRecognizer *)gec;

@end


#endif /* QHChatBaseProtocol_h */
