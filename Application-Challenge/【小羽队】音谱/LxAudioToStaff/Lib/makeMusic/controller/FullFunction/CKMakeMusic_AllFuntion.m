//
//  LxMakeMusic3_1.m
//  SmartPiano
//
//  Created by DavinLee on 2018/9/19.
//  Copyright © 2018年 Ydtec. All rights reserved.
//

#import "CKMakeMusic_AllFuntion.h"

@implementation CKMakeMusic_AllFuntion
- (CKMakeMusicView *)makeMusicView {
    CKMakeMusicView *makeMusicView = [[CKMakeMusicView alloc] initWithFrame:CGRectMake(0, 0, mScreenWidth, mScreenHeight)];
    //设置为高音谱
    LxMcStaffLineView *tabsView = [self tabsView];
    tabsView.isAllVersion = YES;
    makeMusicView.tabsView = tabsView;
    //切换为高音谱
//    [tabsView lx_changeClefType:LxMcMcBothClef];
//    //显示小节添加删除
//    makeMusicView.addButton = [self addButton];
//    //显示小节添加删除
//    makeMusicView.cutButton = [self cutButton];
    //显示节拍选择器
//    makeMusicView.beatView = [self beatViewAllFuntion:LxMcStaffBeats4_4];
    //设置默认音符
//    tabsView.staticNotesArray = [self staticNoteArray];
    //右边音符
    
    //lx TODO:修改
//    makeMusicView.rightNotesView = [self rightNotesView];
//    //左边音符
//    makeMusicView.leftNotesView = [self leftNotesView];
    
    makeMusicView.beatView = [self beatView:LxMcStaffBeatsNone];
    //显示伴奏
    makeMusicView.bellView = [self bellView];
    //左边音符
//    makeMusicView.leftNotesView = [self leftNotesView];
//    makeMusicView.bellView = [self bellView];
//    tabsView.delayPlayTime = 2 * kMcStaffWholeDuration;
    return makeMusicView;
}
#pragma mark  --  getter
//- (CKMakeMusicNotesView *)leftNotesView {
//    CKMakeMusicNotesView *leftNotesView = [[CKMakeMusicNotesView alloc] initWithFrame:CGRectMake(0, 0, kMcNotesWidth, kMcNotesWidth *  8.5))];
////    leftNotesView.backgroundColor = [UIColor redColor];
//    NSArray *array = [NSStringFromClass([self class]) componentsSeparatedByString:@"CKMakeMusic"];
//    NSString *fileName = [@"leftNotes" stringByAppendingString:[array lastObject]];
//    NSArray *modelArray = [CKNoteModel noteModelWithFileName:fileName];
//    super.leftNotesArray = modelArray;
//    leftNotesView.dataSource = modelArray;
//    leftNotesView.transform = CGAffineTransformMakeScale(0.7, 0.7);
//    leftNotesView.x = 0;
//    return leftNotesView;
//}
//
//- (CKMakeMusicNotesView *)rightNotesView {
//    CKMakeMusicNotesView *rightNotesView = [[CKMakeMusicNotesView alloc] initWithFrame:CGRectMake(0, 0, kMcNotesWidth, kMcNotesWidth * (WeLibraryAPI.sharedInstance.bookState > kBookStateTwo ? 8.5 : 8.5))];
////    rightNotesView.backgroundColor = [UIColor redColor];
//    NSArray *array = [NSStringFromClass([self class]) componentsSeparatedByString:@"CKMakeMusic"];
//    NSString *fileName = [@"rightNotes" stringByAppendingString:[array lastObject]];
//    NSArray *modelArray = [CKNoteModel noteModelWithFileName:fileName];
//    super.rightNotesArray = modelArray;
//    if (modelArray.count <= 0) {
//        return nil;
//    }
//    rightNotesView.dataSource = modelArray;
//    rightNotesView.transform = CGAffineTransformMakeScale(0.7, 0.7);
//    rightNotesView.x = mScreenWidth - rightNotesView.width;
//    return rightNotesView;
//}

- (LxMcStaffLineView *)tabsView {
    /** 五线谱设置 **/
    LxMcStaffLineView *tabsView = [[LxMcStaffLineView alloc] initWithFrame:CGRectMake(0, 0, mScreenWidth, mScreenHeight - 60)];
    [tabsView lx_changeClefType:LxMcMcBothClef];
    [mUserDefaults setFloat:spaceScale forKey:spaceScaleKey];
    [mUserDefaults synchronize];
    [tabsView lx_defaultStaffLineViewWithStaffType:LxMcStaffManageNormal beatsType:LxMcStaffBeats4_4];
    tabsView.layer.masksToBounds = YES;
    
    return tabsView;
}

- (LxMcNoteView *)quarter{
    LxMcNoteView *quarterNoteView = [[LxMcNoteView alloc] init];
    quarterNoteView.noteType = MusicNodeQuarter;
    quarterNoteView.isRest = NO;
    quarterNoteView.isDot = NO;
    quarterNoteView.isUpClef = YES;
    return quarterNoteView;
}

- (NSArray *)staticNoteArray {
    LxMcNoteView *static1 = [self quarter];
    static1.isUpClef = NO;
    static1.miditag = 48;
    FixedNotePosition fixed1 = {YES,YES,7};
    static1.isUpClef = NO;
    static1.fixedPosition = fixed1;
    static1.measureIndex = 7;
    [static1 createStaffUI];
    //八小节C4结束
    LxMcNoteView *static2 = [self quarter];
    static2.isUpClef = NO;
    static2.miditag = 55;
    FixedNotePosition fixed2 = {YES,YES,7};
    static2.fixedPosition = fixed2;
    static2.measureIndex = 3;
    [static2 createStaffUI];
    
    //八小节C4结束
    LxMcNoteView *static3 = [self quarter];
    static3.miditag = 67;
    FixedNotePosition fixed3 = {YES,YES,7};
    static3.fixedPosition = fixed3;
    static3.measureIndex = 3;
    [static3 createStaffUI];
    
    LxMcNoteView *static4 = [self quarter];
    static4.miditag = 72;
    FixedNotePosition fixed4 = {YES,YES,7};
    static4.fixedPosition = fixed4;
    static4.measureIndex = 7;
    [static4 createStaffUI];
    
    return @[static1,static2,static3,static4];
}



@end
