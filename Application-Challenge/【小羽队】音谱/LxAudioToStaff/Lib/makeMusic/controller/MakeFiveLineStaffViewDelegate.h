//
//  MakeFiveLineStaffViewDelegate.h
//  SmartPiano
//
//  Created by JM(jieson) on 16/4/13.
//  Copyright © 2016年 XiYun. All rights reserved.
//

#import <Foundation/Foundation.h>
@class MakeNoteView;
@class MakeFiveLineStaffView;

@protocol MakeFiveLineStaffViewDelegate <NSObject>
- (void) makeNoteView:(MakeNoteView *) aMakeStaffView willChangeStepToMidiValue:(NSInteger) aMidiValue;
- (void) makeNoteViewDidBeFillFull:(MakeFiveLineStaffView *) aMakeStaffView ;
- (void) makeNoteViewDidBeNotFillFull:(MakeFiveLineStaffView *) aMakeStaffView ;

@end
