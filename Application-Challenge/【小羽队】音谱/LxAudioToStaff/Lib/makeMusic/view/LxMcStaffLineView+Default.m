//
//  LxMcStaffLineView+Default.m
//  SmartPiano
//
//  Created by DavinLee on 2019/5/6.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import "LxMcStaffLineView+Default.h"

@implementation LxMcStaffLineView (Default)
/**   根据首尾音符获取该区间对应音符    **/
- (NSMutableArray <LxMcNoteView *>*)lx_sectionNotesWithFirstNote:(LxMcNoteView *)firstNoteView endNote:(LxMcNoteView *)endNoteView{
    NSMutableArray <LxMcNoteView *>* allNoteViews = [self lx_getPlayQueueNoteArray];
    
    NSMutableArray <LxMcNoteView *>* sectionNotes = [[NSMutableArray alloc] init];
    for (int i = (int)firstNoteView.staffIndex; i <= endNoteView.staffIndex; i ++) {
        LxMcNoteView *noteView = allNoteViews[i];
        [sectionNotes addObject:noteView];
    }
    return sectionNotes;
}
@end
