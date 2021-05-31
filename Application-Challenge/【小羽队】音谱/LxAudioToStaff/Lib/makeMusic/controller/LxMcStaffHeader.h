//
//  LxMcStaffHeader.h
//  SmartPiano
//
//  Created by DavinLee on 2018/1/30.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#ifndef LxMcStaffHeader_h
#define LxMcStaffHeader_h
#import "UIColor+Default.h"
/** 曲谱大调类型 **/
typedef NS_ENUM(NSInteger,LxMcStaffManageType)
{
    LxMcStaffManageNormal = 0,
    LxMcStaffManageGmajor,
    LxMcStaffManageDmajor,
    LxMcStaffManageAmajor,
    LxMcStaffManageEmajor,
    LxMcStaffManageBmajor,
    LxMcStaffManageFmajor_s,
    LxMcStaffManageCmajor_s,
    LxMcStaffManageGmajor_s,
    LxMcStaffManageDmajor_s,
    LxMcStaffManageAmajor_s,
    LxMcStaffManageFmajor,
    
    LxMcStaffManageAminor,
    LxMcStaffManageEminor,
    LxMcStaffManageBminor,
    LxMcStaffManageFminor_s,
    LxMcStaffManageCminor_s,
    LxMcStaffManageGminor_s,
    LxMcStaffManageDminor_s,
    LxMcStaffManageAminor_s,
    LxMcStaffManageFminor,
    LxMcStaffManageCminor,
    LxMcStaffManageGminor,
    LxMcStaffManageDminor,
    
    
    LxMcStaffManageAchordMajor,
    LxMcStaffManageEchordMajor,
};
/** 曲谱节拍类型 **/
typedef NS_ENUM(NSInteger,LxMcStaffBeatsType)
{
    LxMcStaffBeatsNone = 0,
    LxMcStaffBeats2_4 = 2,
    LxMcStaffBeats3_4 = 3,
    LxMcStaffBeats4_4 = 4,
    lxMcStaffBeats3_8 = 5,
};
/** 曲谱高低音谱显示分类 **/
typedef NS_ENUM(NSInteger,LxMcState)
{
    LxMcMcBothClef = 0,
    LxMcMcHighClef = 1,
    LxMcMcLowClef = 2,
};
/** 曲谱高低音谱小节上限 **/
typedef NS_ENUM(NSInteger,LxMcMaxSection)
{
    LxMcMaxSection_0 = 0,
    //四小节
    LxMcMaxSection_4 = 4,
    //八小节
    LxMcMaxSection_8 = 8,
};
/** 曲谱高低音谱显示分类 **/
typedef NS_ENUM(NSInteger,LxMcEditMode)
{
    LxMcEditModeMove = 0,
    LxMcEditModeKeyborad = 1,
    LxMcEditModeABCShow = 2,
    LxMcEditModeFreedom = 3,
};
/**
 *@description 音符类型
 **/
typedef NS_ENUM(NSInteger,LxMcNoteType){
    LxMcNoteRest = 0,
    LxMcNote32nd = 32,
    LxMcNote16th = 16,
    LxMcNoteEighth = 8,
    LxMcNoteQuarter = 4,
    LxMcNoteHalf = 2,
    LxMcNoteWhole = 1,
};
/** 一个全音符的时值，即播放速度 **/
#define kMcStaffWholeDuration  2.4
/** 五线谱线粗 **/
#define kMcStaffLineRude 1
/** 小节线粗 **/
#define kMcStaffMeasureLineRude 2
/** 符杆线粗 **/
#define kMcStaffNoteBodyRude 2.5f
#define kMcDotHeight 5.0 * kMcStaffScale
/** 五线谱的比例 */
static NSString *const spaceScaleKey = @"spaceScaleKey";
#define kMcStaffScale [mUserDefaults floatForKey:spaceScaleKey]
/** 线间距离 **/
#define kMcStaffSpace kMcStaffScale * 11
//static CGFloat kMcStaffSpace = 23.0f;
/** 音符间的最小间距 **/
#define kMcNoteMinBaseSpace 11 * kMcStaffScale
/** 符头的宽 **/
#define kMcNoteHeadWidth 11 * kMcStaffScale * 58.f/ 49.f
/** 符头的高 **/
#define kMcNoteHeadHeight kMcStaffScale * 11
/** 全音符的时值 **/
#define kMcNoteWholeDuration 2.4f
/** 五线谱颜色 **/
#define kMcStaffLineColor [UIColor colorWithRed:0 green:0 blue:0 alpha:1].CGColor
/** 五线谱缩放比例（默认为1） **/
#define kMcStaffZoomScale 1.3
/** 五线谱以外UI缩放比例 **/
#define kMcZoomScale 0.9
/** 左右气泡部件缩小比例 **/
#define kMcBubbleZoomScale 0.6
/** 左右气泡部件放大比例 **/
#define kMcBubbleZoomOutScale 1.0
/** 缩放动画时间 **/
#define kMcZoomScaleDuration 1.0
/** 音符部件间的冰块间距 **/
#define kMcIceLandSpace 130
/** 音符的宽 */
#define kMcNotesWidth 16
#endif /* LxMcStaffHeader_h */
