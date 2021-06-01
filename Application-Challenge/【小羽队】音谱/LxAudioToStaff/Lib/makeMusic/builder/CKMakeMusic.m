//
//  CKMakeMusic.m
//  SmartPiano
//
//  Created by xy on 2018/5/19.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "CKMakeMusic.h"
#import "UIButton+Default.h"
#import "UIImage+Default.h"

@interface CKMakeMusic()

@end

@implementation CKMakeMusic

- (CKMakeMusicView *)makeMusicView {
    CKMakeMusicView *makeMusicView = [[CKMakeMusicView alloc] initWithFrame:CGRectMake(0, 0, mScreenWidth, mScreenHeight)];
    
    //设置为双音谱
    LxMcStaffLineView *tabsView = [self tabsView];
    makeMusicView.tabsView = tabsView;
//    默认琴键输入模式
//    makeMusicView.tabsView.editMode = LxMcEditModeKeyborad;
    //默认添加四小节
//    makeMusicView.eightButton = [self eightButton];
//    makeMusicView.fourButton = [self fourButton];
//    self
    //显示节拍选择器
//    makeMusicView.beatView = [self beatView:LxMcStaffBeats4_4];
    //左边音符
    makeMusicView.leftNotesView = [self leftNotesView];
    //右边音符
    makeMusicView.rightNotesView = [self rightNotesView];
    return makeMusicView;
}


#pragma mark  --  getter
//- (CKMakeMusicNotesView *)leftNotesView {
//    CKMakeMusicNotesView *leftNotesView = [[CKMakeMusicNotesView alloc] initWithFrame:CGRectMake(0, kMcIceLandSpace, kMcNotesWidth, kMcNotesWidth *  5)];
//    NSArray *array = [NSStringFromClass([self class]) componentsSeparatedByString:@"CKMakeMusic"];
//    NSString *fileName = [@"leftNotes" stringByAppendingString:[array lastObject]];
//    NSArray *modelArray = [CKNoteModel noteModelWithFileName:fileName];
//    self.leftNotesArray = modelArray;
//    leftNotesView.dataSource = modelArray;
//    leftNotesView.transform = CGAffineTransformMakeScale(kMcBubbleZoomScale, kMcBubbleZoomScale);
//    leftNotesView.x = 0;
//    return leftNotesView;
//}
//
//- (CKMakeMusicNotesView *)rightNotesView {
//    CKMakeMusicNotesView *rightNotesView = [[CKMakeMusicNotesView alloc] initWithFrame:CGRectMake(0, kMcIceLandSpace, kMcNotesWidth, kMcNotesWidth * (WeLibraryAPI.sharedInstance.bookState > kBookStateTwo ? 6 : 5))];
//    NSArray *array = [NSStringFromClass([self class]) componentsSeparatedByString:@"CKMakeMusic"];
//    NSString *fileName = [@"rightNotes" stringByAppendingString:[array lastObject]];
//    NSArray *modelArray = [CKNoteModel noteModelWithFileName:fileName];
//    self.rightNotesArray = modelArray;
//    if (modelArray.count <= 0) {
//        return nil;
//    }
//    rightNotesView.dataSource = modelArray;
//    rightNotesView.transform = CGAffineTransformMakeScale(kMcBubbleZoomScale, kMcBubbleZoomScale);
//    rightNotesView.x = mScreenWidth - rightNotesView.width;
//    return rightNotesView;
//}

- (LxMcStaffLineView *)tabsView {
    /** 五线谱设置 **/
    LxMcStaffLineView *tabsView = [[LxMcStaffLineView alloc] initWithFrame:CGRectMake(0, 0, 835, 485)];
    /** 音谱需要放大在此设置放大倍数 */
    //        [mUserDefaults setFloat:spaceScale forKey:spaceScaleKey];
    //        [mUserDefaults synchronize];
    [tabsView lx_defaultStaffLineViewWithStaffType:0 beatsType:LxMcStaffBeats4_4];
        
    return tabsView;
}

- (CKBellView *)bellView {
    CKBellView *bellView = [[CKBellView alloc] init];
    return bellView;
}

- (UIButton *)fourButton {
    UIButton *fourBeatBtn = [UIButton lx_defaultBtnWithImageName:@"write_4"];
    [fourBeatBtn setImage:[UIImage imageNamed:@"write_seleted4"] forState:UIControlStateSelected];
    return fourBeatBtn;
}

- (UIButton *)eightButton {
    UIButton *eightBeatBtn = [UIButton lx_defaultBtnWithImageName:@"write_8"];
    [eightBeatBtn setImage:[UIImage imageNamed:@"write_seleted8"] forState:UIControlStateSelected];
    return eightBeatBtn;
}

- (UIButton *)cutButton {
    UIButton *cutButton = [UIButton lx_defaultBtnWithImageName:@"write_jian"];
//    [cutButton setImage:[UIImage imageNamed:@"write_seleted4"] forState:UIControlStateSelected];
    return cutButton;
}

- (UIButton *)addButton {
    UIButton *addButton = [UIButton lx_defaultBtnWithImageName:@"write_jia"];
//    [addButton setImage:[UIImage imageNamed:@"write_seleted8"] forState:UIControlStateSelected];
    return addButton;
}


- (UIView *)beatViewAllFuntion:(LxMcStaffBeatsType)beatsType {
    return nil;
    NSArray *imageArray = @[@"allFunction_42", @"allFunction_43", @"allFunction_44"];
    if (beatsType == lxMcStaffBeats3_8) {
        imageArray = @[@"write_83"];
    }
    NSInteger count = imageArray.count;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(113, 50, 55 * count, 100)];
    CGFloat heightDiff = 0;
    NSInteger index = 0;
    for (NSString *imageStr in imageArray) {
        UIButton *btn = [UIButton lx_defaultBtnWithImageName:imageStr];
        btn.hidden = YES;
        [btn setImage:[UIImage imageNamed: [NSString stringWithFormat:@"S%@",imageStr]] forState:UIControlStateSelected];
       
        btn.origin = CGPointMake((view.width / count) * index, heightDiff * index);
        btn.adjustsImageWhenHighlighted = NO;
        btn.tag = 52 + index;
        [view addSubview:btn];
        
        index ++;
        
        if (beatsType == index + 1) {
            btn.hidden = NO;
            btn.selected = YES;
        }else if(imageArray.count == 1){
            btn.hidden = NO;
            btn.selected = YES;
        }
        if (beatsType == LxMcStaffBeatsNone) {
            btn.hidden = NO;
            if (index == count) {
                btn.selected = YES;
            }
        }
    }
    return view;
}

- (UIView *)beatView:(LxMcStaffBeatsType)beatsType {
    return nil;
    NSArray *imageArray = @[@"write_42", @"write_43", @"write_44"];
    if (beatsType == lxMcStaffBeats3_8) {
        imageArray = @[@"write_83"];
    }
    NSInteger count = imageArray.count;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(113, 50, 68 * count, 100)];
    CGFloat heightDiff = 0;
  
    NSInteger index = 0;
    for (NSString *imageStr in imageArray) {
        UIButton *btn = [UIButton lx_defaultBtnWithImageName:imageStr];
        btn.hidden = YES;
        [btn setImage:[UIImage imageNamed:[NSString stringWithFormat:@"selected_%@", imageStr]] forState:UIControlStateSelected];
     
        btn.origin = CGPointMake((view.width / count) * index, heightDiff * index);
        btn.adjustsImageWhenHighlighted = NO;
        btn.tag = 52 + index;
        [view addSubview:btn];
        
        index ++;
        
        if (beatsType == index + 1) {
            btn.hidden = NO;
            btn.selected = YES;
        }else if(imageArray.count == 1){
            btn.hidden = NO;
            btn.selected = YES;
        }
        if (beatsType == LxMcStaffBeatsNone) {
            btn.hidden = NO;
            if (index == count) {
                btn.selected = YES;
            }
        }
    }
    return view;
}


- (UIButton *)changeCandyButton {
    UIButton *button = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 29, 51)];
    button.center = CGPointMake(mScreenWidth * 0.5 + 70, 105);
    [button setImage:mImageByName(@"write_changeCandy") forState:UIControlStateSelected];
    [button setImage:mImageByName(@"write_changeNornal") forState:UIControlStateNormal];
    return button;
}

- (NSArray *)staticNoteArray {
    return nil;
}

/**
 获取左边固定音符数组
 */
- (NSArray *)getLeftNotesArray {
    if (self.leftNotesArray) {
        return self.leftNotesArray;
    }else {
        return nil;
    }
}

/**
 获取右边固定音符数组
 */
- (NSArray *)getRightNotesArray {
    if (self.rightNotesArray) {
        return self.rightNotesArray;
    }else {
        return nil;
    }
}

@end
