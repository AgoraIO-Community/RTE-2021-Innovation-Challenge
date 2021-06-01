//
//  QHChatBaseView.m
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/20.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import "QHChatBaseView.h"

#import "QHViewUtil.h"
#import "NSTimer+QHEOCBlocksSupport.h"

#import "QHChatBaseViewCell.h"
#import "QHChatBaseNewDataView.h"

#define kQHCHATBASE_CELLIDENTIFIER @"QHChatbaseCellIdentifier"

@interface QHChatBaseView () <UITableViewDataSource, UITableViewDelegate, QHChatBaseViewCellDelegate>

@property (nonatomic, strong, readwrite) UITableView *mainTableV;
@property (nonatomic) BOOL bAutoReloadChat;
@property (nonatomic, strong) NSTimer *reloadTimer;
@property (nonatomic, strong) UIView<QHChatBaseNewDataViewProtcol> *hasNewDataView;

@property (nonatomic) dispatch_queue_t chatReloadQueue;
@property (nonatomic) BOOL bOutHeight;

@property (nonatomic, strong, readwrite) QHChatBaseBuffer *buffer;

@end

@implementation QHChatBaseView

- (void)dealloc {
    [self p_closeReloadTimer];
    _hasNewDataView = nil;
    #if DEBUG
        NSLog(@"%s", __FUNCTION__);
    #endif
}

- (instancetype)init {
    return [self initWithConfig:[QHChatBaseConfig new]];
}

- (instancetype)initWithConfig:(QHChatBaseConfig *)config {
    self = [super init];
    if (self) {
        self.config = config;
        [self p_setup];
    }
    return self;
}

#pragma mark - Public

+ (instancetype)createChatViewToSuperView:(UIView *)superView {
    QHChatBaseView *subView = [[self alloc] init];
    [superView addSubview:subView];
    [QHViewUtil fullScreen:subView];
    
    return subView;
}

+ (instancetype)createChatViewToSuperView:(UIView *)superView withConfig:(QHChatBaseConfig *)config {
    QHChatBaseView *subView = [[self alloc] initWithConfig:config];
    [superView addSubview:subView];
    [QHViewUtil fullScreen:subView];
    
    return subView;
}

- (void)insertChatData:(NSArray<NSDictionary *> *)data {
    if (data == nil || data.count <= 0) {
        return;
    }
//    onGlobalThreadAsync(^{
        onMainThreadAsync(^{
            [self.buffer append2TempArray:data];
            [self p_reloadAndRefresh:NO];
        });
//    });
}

- (void)clearChatData {
    onMainThreadAsync(^{
        [self p_clearChatData];
    });
}

- (void)scrollToBottom {
    onMainThreadAsync(^{
        [self p_scrollToBottom];
    });
}

#pragma mark - Private

- (void)p_setup {
    [self p_setupData];
    [self p_setupUI];
    [self qhChatCustomChatViewSetup];
}

- (void)p_setupData {
    _buffer = [QHChatBaseBuffer new];
    _buffer.config = _config;
    _bAutoReloadChat = YES;
    self.backgroundColor = [UIColor clearColor];
    
    _chatReloadQueue = dispatch_queue_create("com.qhchat.queue", NULL);
    dispatch_set_target_queue(_chatReloadQueue, dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0));
    if (_config.bOpenScorllFromBottom == YES) {
        _bOutHeight = NO;
    }
    else {
        _bOutHeight = YES;
    }
}

- (void)p_setupUI {
    [self p_addTableView];
    
    if (_config.bLongPress) {
        UILongPressGestureRecognizer *longPressGec = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPressAction:)];
        if (_config.minimumPressDuration > 0) {
            longPressGec.minimumPressDuration = _config.minimumPressDuration;
        }
        [_mainTableV addGestureRecognizer:longPressGec];
    }
}

- (void)p_addTableView {
    UITableView *tableV = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
    tableV.dataSource = self;
    tableV.delegate = self;
    // [UITableView 滚动到底部 闪一下的问题](https://dongjiawang.top/2017/07/31/2017-07-31-UITableView-auto-bottom/)
    tableV.estimatedRowHeight = 0;
    tableV.estimatedSectionHeaderHeight = 0;
    tableV.estimatedSectionFooterHeight = 0;
    tableV.separatorStyle = UITableViewCellSeparatorStyleNone;
    tableV.backgroundColor = [UIColor clearColor];
    tableV.tableFooterView = [UIView new];
    tableV.clipsToBounds = YES;
    tableV.scrollsToTop = YES;
    tableV.showsHorizontalScrollIndicator = NO;
    tableV.showsVerticalScrollIndicator = NO;
    tableV.allowsSelection = NO;
    [self addSubview:tableV];
    [QHViewUtil fullScreen:tableV];
    
    _mainTableV = tableV;
    
    //    [tableView registerClass:[QHChatBaseViewCell class] forCellReuseIdentifier:kQHCHATBASE_CELLIDENTIFIER];
    
    [self qhChatAddCell2TableView:_mainTableV];
}

- (void)p_reloadAndRefresh:(BOOL)bRefreshImmediately {
    if (_bAutoReloadChat == YES) {
        if (_reloadTimer == nil || _reloadTimer.isValid == NO) {
            __weak typeof(self) weakSelf = self;
            _reloadTimer = [NSTimer qheoc_scheduledTimerWithTimeInterval:_config.chatReloadDuration block:^{
                dispatch_sync(weakSelf.chatReloadQueue, ^{
                    @try {
                        [weakSelf p_reloadAction];
                    } @catch (NSException *exception) {
                    } @finally {
                    }
                });
            } repeats:YES];
            [[NSRunLoop mainRunLoop] addTimer:_reloadTimer forMode:NSRunLoopCommonModes];
        }
        if (bRefreshImmediately == YES) {
            // [NSTimer的使用 停止 暂停 重启 - wahaha13168 - CSDN博客](https://blog.csdn.net/wahaha13168/article/details/52804048)
            // setFireDate 会立即触发 Timer 并重新计时，而 fire 只是立即触发
            // 该延迟可以避免加载过清空时，公屏列表出现空白的情况
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(MIN(0.05, _config.chatReloadDuration/2) * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [self.reloadTimer setFireDate:[NSDate date]];
            });
        }
    }
    else {
        [self.hasNewDataView update:@(self.buffer.chatDatasTempArray.count)];
    }
}

- (void)p_reloadAction {
    if (self.buffer.chatDatasTempArray.count <= 0) {
        [self p_closeReloadTimer];
        return;
    }
    if (_bAutoReloadChat == NO) {
        [self.hasNewDataView update:@(self.buffer.chatDatasTempArray.count)];
        [self p_closeReloadTimer];
        return;
    }
    
    NSArray<NSDictionary *> *tempArray = [NSArray arrayWithArray:self.buffer.chatDatasTempArray];
    [self.buffer clearTempArray];
    
    __block BOOL isReplaceChatData = NO;
    [tempArray enumerateObjectsUsingBlock:^(NSDictionary * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        QHChatBaseModel *model = [[QHChatBaseModel alloc] initWithChatData:obj];
        model.cellConfig = self.config.cellConfig;
        if (self.config.bInsertReplace) {
            BOOL bReplace = [self qhChatUseReplace:obj old:[self.buffer.chatDatasArray lastObject].originChatDataDic];
            if (bReplace == YES) {
                [self.buffer replaceObjectAtLastIndexWith:model];
                isReplaceChatData = YES;
            }
            else {
                [self.buffer append2Array:model];
            }
        }
        else {
            [self.buffer append2Array:model];
        }
    }];
    
    BOOL bDeleteChatData = [self.buffer removeObjectsInRange];
    
    // 只有当加入的数据为1个，且是替换，和此次没有删除数据时，才启动指定刷新
    if (tempArray.count == 1 && bDeleteChatData == NO && isReplaceChatData == YES) {
        NSIndexPath *indexPath = [NSIndexPath indexPathForRow:(self.buffer.chatDatasArray.count - 1) inSection:0];
//        if (isReplaceChatData == YES) {
            [self.mainTableV reloadRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationNone];
//        }
//        else {
//            [self.mainTableV insertRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationNone];
//        }
    }
    else {
    // [IOS开发之CLAyer 隐式动画 - 简书](https://www.jianshu.com/p/930cea99023d)
        [CATransaction begin];
        [CATransaction setDisableActions:YES];
        [self.mainTableV reloadData];
        [CATransaction commit];
    }
    if (_config.bOpenScorllFromBottom == NO) {
        if (self.mainTableV.isDragging == NO && self.mainTableV.tracking == NO) {
            [self p_scrollToFinalBottom];
        }
    }
    else {
        CGFloat hasCellHeight = 0;
        if (_bOutHeight == NO) {
            hasCellHeight = [self p_hasCellHeight];
            if (hasCellHeight >= self.mainTableV.bounds.size.height) {
                _bOutHeight = YES;
            }
        }
        
        if (_bOutHeight == YES) {
            if (self.mainTableV.isDragging == NO && self.mainTableV.tracking == NO) {
                [self p_scrollToFinalBottom];
            }
        }
        else {
            [self p_updateTableContentInset:hasCellHeight];
        }
    }
    tempArray = nil;
}

- (void)p_scrollToFinalBottom {
    if (self.buffer.chatDatasArray.count > 0) {
        NSIndexPath *indexPath = [NSIndexPath indexPathForRow:self.buffer.chatDatasArray.count - 1 inSection:0];
        // 控制滑动底部的动画时长
        // 由于使用预测 Cell 高度，所以去掉滑动到底部的动画，避免出现跳动现象
//        [UIView animateWithDuration:MIN(0.2, self.config.chatReloadDuration - 0.05) animations:^{
            [self.mainTableV scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionTop animated:NO];
//        }];
    }
}

- (void)p_refreshAutoReloadChat {
    NSIndexPath *indexPath = [[_mainTableV indexPathsForVisibleRows] lastObject];
    
    if (indexPath.row == self.buffer.chatDatasArray.count - 1) {
        _bAutoReloadChat = YES;
    }
    
    if (_bAutoReloadChat == NO) {
        //当向上滑动，隐藏全部cell时，vidx是nil，就要从新判断下；
        _bAutoReloadChat = (_mainTableV.contentOffset.y >= _mainTableV.contentSize.height);
    }
    
    if (_bAutoReloadChat == YES) {
        if ([_hasNewDataView hide]) {
            [self p_reloadAndRefresh:YES];
        }
    }
}

- (void)p_closeReloadTimer {
    if (_reloadTimer != nil) {
        [_reloadTimer invalidate];
    }
    _reloadTimer = nil;
}

- (NSAttributedString *)p_goContent:(NSIndexPath *)indexPath {
    QHChatBaseModel *model = [self.buffer getChatData:indexPath.row];
    if (model.chatAttributedText != nil && [_config isEqualToCellConfig:model.cellConfig] == YES) {
        return model.chatAttributedText;
    }
    model.cellConfig = _config.cellConfig;
    
    NSMutableAttributedString *content = [self qhChatAnalyseContent:[self.buffer getChatData:indexPath.row].originChatDataDic];
    
    if (content != nil) {
        [self qhChatAddCellDefualAttributes:content];
        model.chatAttributedText = content;
    }
    return content;
}

- (CGFloat)p_goHeight:(NSIndexPath *)indexPath {
    QHChatBaseModel *model = [self.buffer getChatData:indexPath.row];
    if (model.cellHeight > 0 && [_config isEqualToCellConfig:model.cellConfig] == YES) {
        return model.cellHeight;
    }
    
    NSAttributedString *content = [self p_goContent:indexPath];
    CGFloat h = 0;
    CGFloat hh = [self qhChatAnalyseHeight:_mainTableV heightForRowAtIndexPath:indexPath];
    if (hh >= 0) {
        h = hh;
    }
    else {
        if (content != nil) {
            // [iOS 计算NSString宽高与计算NSAttributedString的宽高 - 简书](https://www.jianshu.com/p/76ab08089941)
            CGFloat needWidth = (_config.cellConfig.cellWidth - _config.cellEdgeInsets.left - _config.cellEdgeInsets.right);
            NSStringDrawingOptions options = NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading;
            CGRect rect = [content boundingRectWithSize:CGSizeMake(needWidth, CGFLOAT_MAX) options:options context:nil];
            // +cellLineSpacing 是由于上面计算出是包括设置的行距，所以只加一次为最后一行
            h = rect.size.height + _config.cellConfig.cellLineSpacing + _config.cellEdgeInsets.top + _config.cellEdgeInsets.bottom;
        }
    }
    
    model.cellHeight = h;
    return model.cellHeight;
}

- (void)p_clickNewDataViewAction {
    onMainThreadAsync(^{
        self.hasNewDataView.hidden = YES;
        [self p_scrollToFinalBottom];
        self.bAutoReloadChat = YES;
        [self p_reloadAndRefresh:YES];
    });
}

- (CGFloat)p_hasCellHeight {
    NSInteger numRows = [self tableView:self.mainTableV numberOfRowsInSection:0];
    CGFloat hasCellHeight = 0;
    for (NSInteger i = 0; i < numRows; i++) {
        hasCellHeight += [self tableView:self.mainTableV heightForRowAtIndexPath:[NSIndexPath indexPathForItem:i inSection:0]];
    }
    return hasCellHeight;
}

- (void)p_updateTableContentInset:(CGFloat)height {
    CGFloat contentInsetTop = MAX(self.mainTableV.bounds.size.height - height, 0);
    self.mainTableV.contentInset = UIEdgeInsetsMake(contentInsetTop, 0, 0, 0);
}

- (void)p_clearChatData {
    [self p_closeReloadTimer];
    
    BOOL bRefresh = YES;
    if (self.buffer.chatDatasArray.count <= 0) {
        bRefresh = NO;
    }
    [self.buffer clear];
    
    self.bAutoReloadChat = YES;
    [self.hasNewDataView hide];

    if (bRefresh) {
        [CATransaction begin];
        [CATransaction setDisableActions:YES];
        [self.mainTableV reloadData];
        [CATransaction commit];
    }
        
    if (self.config.bOpenScorllFromBottom == YES) {
        self.bOutHeight = NO;
    }
}

- (void)p_scrollToBottom {
    [self p_closeReloadTimer];
    _hasNewDataView.hidden = YES;
    _bAutoReloadChat = YES;
    if (self.buffer.chatDatasArray.count > 0) {
        [CATransaction begin];
        [CATransaction setDisableActions:YES];
        [self.mainTableV reloadData];
        [CATransaction commit];
        
        CGFloat hasCellHeight = 0;
        
        if (_config.bOpenScorllFromBottom == YES) {
            _bOutHeight = NO;
            hasCellHeight = [self p_hasCellHeight];
            if (hasCellHeight >= self.mainTableV.bounds.size.height) {
                _bOutHeight = YES;
            }
        }
        
        if (_bOutHeight == YES || _config.bOpenScorllFromBottom == NO) {
            if (self.mainTableV.isDragging == NO && self.mainTableV.tracking == NO) {
                [self p_scrollToFinalBottom];
            }
        }
        else {
            [self p_updateTableContentInset:hasCellHeight];
        }
    }
    [self p_reloadAndRefresh:NO];
}

#pragma mark - QHChatBaseViewProtocol

- (void)qhChatCustomChatViewSetup {
}

- (void)qhChatAddCell2TableView:(UITableView *)tableView {
}

- (NSMutableAttributedString *)qhChatAnalyseContent:(NSDictionary *)data {
    NSMutableAttributedString *c = [[NSMutableAttributedString alloc] initWithString:data[@"c"]];
    return c;
}

- (UIView<QHChatBaseNewDataViewProtcol> *)qhChatTakeHasNewDataView {
    QHChatBaseNewDataView *view = [QHChatBaseNewDataView createViewToSuperView:self];
    return view;
}

- (CGFloat)qhChatAnalyseHeight:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return -1;
}

- (UITableViewCell *)qhChatChatView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    return nil;
}

- (void)qhChatMakeAfterChatBaseViewCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
}

- (void)qhChatAddCellDefualAttributes:(NSMutableAttributedString *)attr {
    [QHChatBaseUtil addCellDefualAttributes:attr lineSpacing:_config.cellConfig.cellLineSpacing fontSize:_config.cellConfig.fontSize];
}

- (BOOL)qhChatUseReplace:(NSDictionary *)newData old:(NSDictionary *)lastData {
    return NO;
}

- (void)qhlongPressAction:(UILongPressGestureRecognizer *)gec {
    if (gec.state == UIGestureRecognizerStateBegan) {
        if ([self.delegate respondsToSelector:@selector(chatView:didLongSelectRowWithData:)]) {
            @try {
                CGPoint point = [gec locationInView:_mainTableV];
                NSIndexPath *indexPath = [_mainTableV indexPathForRowAtPoint:point];
                QHChatBaseModel *model = [self.buffer getChatData:indexPath.row];
            
                [self.delegate chatView:self didLongSelectRowWithData:model.originChatDataDic];
            } @catch (NSException *exception) {
            } @finally {
            }
        }
    }
}

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (self.buffer == nil) {
        return 0;
    }
    return self.buffer.chatDatasArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
//    NSLog(@"chen>>cellForRowAtIndexPath-%@", indexPath);
    QHChatBaseModel *model = [self.buffer getChatData:indexPath.row];
    if (model == nil) {
        return nil;
    }
    if (model.chatAttributedText == nil) {
        NSAttributedString *content = [self p_goContent:indexPath];
        model.chatAttributedText = content;
    }
    UITableViewCell *customCell = [self qhChatChatView:tableView cellForRowAtIndexPath:indexPath];
    if (customCell != nil) {
        return customCell;
    }
    QHChatBaseViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kQHCHATBASE_CELLIDENTIFIER];
    if (cell == nil) {
        cell = [[QHChatBaseViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:kQHCHATBASE_CELLIDENTIFIER];
        [cell makeContent:_config.cellEdgeInsets];
    }
    cell.contentL.attributedText = model.chatAttributedText;
    cell.delegate = self;
    [self qhChatMakeAfterChatBaseViewCell:cell forRowAtIndexPath:indexPath];
    
    return cell;
}

#pragma mark - UITableViewDelegate

// 预测 Cell 高度
- (CGFloat)tableView:(UITableView *)tableView estimatedHeightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 44;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
//    NSLog(@"chen>>heightForRowAtIndexPath-%@", indexPath);
    if (!self.config.bAutoCellHeight) {
        CGFloat h = 0;
        @try {
            h = [self p_goHeight:indexPath];
        } @catch (NSException *exception) {
            h = 0;
        } @finally {
            
        }
        return h;
    }
    return UITableViewAutomaticDimension;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    _bAutoReloadChat = NO;
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
    if (decelerate == NO) {
        [self p_refreshAutoReloadChat];
    }
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    [self p_refreshAutoReloadChat];
}

#pragma mark - QHChatBaseViewCellDelegate

- (void)selectViewCell:(UITableViewCell *)viewCell {
    if ([self.delegate respondsToSelector:@selector(chatView:didSelectRowWithData:)] == YES) {
        NSIndexPath *indexPath = [_mainTableV indexPathForCell:viewCell];
        QHChatBaseModel *model = [self.buffer getChatData:indexPath.row];
        [self.delegate chatView:self didSelectRowWithData:model.originChatDataDic];
    }
}

- (void)deselectViewCell:(UITableViewCell *)viewCell {
    if ([self.delegate respondsToSelector:@selector(chatView:didDeselectRowWithData:)] == YES) {
        NSIndexPath *indexPath = [_mainTableV indexPathForCell:viewCell];
        QHChatBaseModel *model = [self.buffer getChatData:indexPath.row];
        [self.delegate chatView:self didDeselectRowWithData:model.originChatDataDic];
    }
}

#pragma mark - Action

- (void)longPressAction:(UILongPressGestureRecognizer *)gec {
    [self qhlongPressAction:gec];
}

#pragma mark - Get

- (UIView<QHChatBaseNewDataViewProtcol> *)hasNewDataView {
    if (_hasNewDataView == nil) {
        _hasNewDataView = [self qhChatTakeHasNewDataView];
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(p_clickNewDataViewAction)];
        [_hasNewDataView addGestureRecognizer:tap];
        
        tap = nil;
    }
    return _hasNewDataView;
}

@end
