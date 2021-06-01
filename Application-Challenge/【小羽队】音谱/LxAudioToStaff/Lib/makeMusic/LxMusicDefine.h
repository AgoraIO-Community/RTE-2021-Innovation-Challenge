//
//  LxMusicDefine.h
//  SmartPiano
//
//  Created by 李翔 on 2017/4/5.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#ifndef LxMusicDefine_h
#define LxMusicDefine_h
/****************************曲谱********************************/
#define kMcStaffWholeDuration  2.4
/**
 *@description 进入弹奏页面类型（正常弹奏，作曲游戏保存弹奏
 **/
typedef NS_ENUM(NSInteger, LxMusicEnterSourceType){
    LxMusicEnterSourceNormal,/** 正常弹奏 **/
    LxMusicEnterSourceCompose,/** 作曲游戏保存后弹奏 **/
};
/**
 *@description 左右手模式
 **/
typedef NS_ENUM(NSInteger,LxStaffHandleType){
    LxStaffHandleLeft = 0,
    LxStaffHandleDouble = 1,
    LxStaffHandleRight = 2,
};
/**
 *@description 当前曲谱音符存在左右手
 **/
typedef NS_ENUM(NSInteger, LxStaffExistHandType){
    LxStaffExistHandDouble = 0,
    LxStaffExistHandLeft,
    LxStaffExistHandRight,
};

/**
 *@description 音符临时升号、降号类型
 **/
typedef NS_ENUM(NSInteger, LxNoteTempMajorType){
    LxNoteTempMajorNormal = 0,
    LxNoteTempMajorShape = 1,
    LxNoteTempMajorPit   = 2,
};
/**
 *@description 音符类型
 **/
typedef NS_ENUM(NSInteger,MusicNodeType){
    MusicNode32nd = 32,
    MusicNode16th = 16,
    MusicNodeEighth = 8,
    MusicNodeQuarter = 4,
    MusicNodeHalf = 2,
    MusicNodeWhole = 1,
    MusicNodeRest = 0,//在作曲游戏中禁止使用
    /** 强度音符 */
    MusicNodeStrengthPP = 50,
    MusicNodeStrengthP = 60,//弱
    MusicNodeStrengthMP = 70,
    MusicNodeStrengthMF = 85,//次强
    MusicNodeStrengthF = 100,
    MusicNodeStrengthFF = 110,//强
    /** 音符加载 */
    MusicNodeLoading = 999,//琴键按下未抬起 未知音符类型
    /** 新版作曲游戏 */
    MusicNodeShowABC = 8888,//作曲游戏显示音名
};
/** Lx description   跟停模式下，校准线停留位置  **/
typedef NS_ENUM(NSInteger,FollowStopType) {
    FollowStopRight = 0,/** Lx description   停留音符右边，默认  **/
    FollowStopCenter = 1,/** Lx description   停留在音符中间  **/
    FollowStopLeft = 2,
};
/** Lx description   跟停模式下，弹奏判断类型  **/
typedef NS_ENUM(NSInteger,FollowPlayType) {
    FollowPlayNormal = 0, /** Lx description   正常弹奏，纵列下所有音被弹奏后，进行下一纵列判断  **/
    FollowPlaySynEvent = 1,/** @Lx_description  纵列下所有音被同时弹奏后，进行下一纵列判断    **/
};
/**
 *@description 音符UI类型
 **/
typedef NS_ENUM(NSInteger,NoteLayerType){
    NoteLayerNote,
    NoteLayerRest,
    NoteLayerDot,
};

/**
 *@description 音符弹奏正确的等级(音准)
 **/
typedef NS_ENUM(NSInteger,NotePlayGradeType){
    NotePlayGradeZero = 0,
    NotePlayGradeOne = 1,
    NotePlayGradeTwo = 2,
    NotePlayGradeThree = 3,
};
/**
 *@description 弹奏状态
 **/
typedef NS_ENUM(NSInteger,MusicNodeStatus){
    MusicNodePlayNormal,
    MusicNodePlayPrepare,
    MusicNodePlayNone,//未弹
    MusicNodePlayError,
    MusicNodePlayRecord,
    MusicNodePlayRight,
};

/**
 *@description 弹奏力度
 **/
typedef NS_ENUM(NSInteger,NotePlayDymicGrade){
    NotePlayDymicZero,
    NotePlayDymicLight,
    NotePlayDymicStandard,
    NotePlayDymicHeavy,
};

/**
 *@description 弹奏时值
 **/
typedef NS_ENUM(NSInteger,NotePlayDurationGrade){
    NotePlayFreeZero,
    NotePlayFreePremature,/**过早**/
    NotePlayFreeStandard,
    notePlayFreeLate,
};

/**
 *svg显示放大比例
 **/
#define kScoreStaffScale 1.6f
/**
 *@description 五线谱粗细(未计算倍数)
 **/
#define kStaffLineWidth 0.43
/**
 *检测线开始offsetX
 **/
#define kStartLineOffsetX  185
/**
 *曲谱滚动区域大小
 **/
#define kScollViewWidth 875
/**
 *@description 线条高度
 **/
#define kScoreLineHeight 0.43
/**
 *@description 可弹奏区域 基本单位，  -2 * w ->  -1 * w -> 0 * w -> 1 * w -> 2 *w
 **/
#define kScoreBaseWidth 44.f
/**
 *@description 歌词显示中高音谱 低音谱相隔 间单位数
 **/
#define kLyricsSpatiumCount 14
#endif /* LxMusicDefine_h */
