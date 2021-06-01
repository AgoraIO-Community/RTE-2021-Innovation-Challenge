//
//  LxMcLineManager.h
//  SmartPiano
//
//  Created by DavinLee on 2019/4/30.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol LxMcLineManagerDelegate <NSObject>
@required
/**   根据连线首音符及连线尾音符获取包括中间音符的容器    **/
- (NSMutableArray <LxMcNoteView *>*_Nonnull)sectionNotesWithFirstLineNote:(LxMcNoteView *_Nonnull)firstLineNote endLineNote:(LxMcNoteView *_Nullable)endLineNote;
@end

@class LxMcNoteView;
NS_ASSUME_NONNULL_BEGIN
@interface LxMcLineManager : NSObject
/**   持有方法代理    **/
@property (weak, nonatomic) id <LxMcLineManagerDelegate> delegate;
/**   添加连线音符（起始音符，终止音符）    **/
- (void)lx_addLineMusicNoteView:(LxMcNoteView *)noteView;
/**  添加中间连线音符     **/
- (void)lx_insertLineMusicNoteView:(LxMcNoteView *)noteView;
/**   删除音符（包括中间音符）    **/
- (void)lx_deleteMusicNoteView:(LxMcNoteView *)noteView;
/**   取消插入或添加操作    **/
- (void)lx_cancelNoteOperateWithNote:(LxMcNoteView *)noteView;
/**   移除所有连线    **/
- (void)lx_removeAllLine;
@end
NS_ASSUME_NONNULL_END
