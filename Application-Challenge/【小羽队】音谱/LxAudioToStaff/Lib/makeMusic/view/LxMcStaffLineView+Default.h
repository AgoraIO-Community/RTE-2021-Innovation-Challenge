//
//  LxMcStaffLineView+Default.h
//  SmartPiano
//
//  Created by DavinLee on 2019/5/6.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import "LxMcStaffLineView.h"

NS_ASSUME_NONNULL_BEGIN

@interface LxMcStaffLineView (Default)

/**   根据首尾音符获取该区间对应音符    **/
- (NSMutableArray <LxMcNoteView *>*)lx_sectionNotesWithFirstNote:(LxMcNoteView *)firstNoteView endNote:(LxMcNoteView *)endNoteView;
@end

NS_ASSUME_NONNULL_END
