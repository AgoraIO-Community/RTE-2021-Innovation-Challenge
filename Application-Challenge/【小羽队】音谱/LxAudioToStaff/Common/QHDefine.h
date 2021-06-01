//
//  QHDefine.h
//  QHTableViewProfile
//
//  Created by 李翔 on 16/7/7.
//  Copyright © 2016年 XiYun. All rights reserved.
//
//

#ifndef QHTableViewProfile_QHDefine_h
#define QHTableViewProfile_QHDefine_h

typedef NS_ENUM(NSInteger,KNotePartsType){
    /**
     *五线谱
     */
    KNotePartsStaff         = 0,//五线谱
    /**
     *高音谱号
     */
    KNotePartsHighPitch     = 1,//高音谱号
    /**
     *低音谱号
     */
    KNotePartsLowPitch      = 2,//低音谱号
    /**
     *小节线
     */
    KNotePartsBarLine       = 4,//小节线
    /**
     *大括号
     */
    KnotePartsBrace         = 3,//大括号
    /**
     *终止线
     */
    KNotePartsTeminatedLine = 5,//终止线
    /************************音乐音符************************/
    /**
     *二分音符
     */
    KNotePartsHalf       = 6,
    /**
     *二分音符反向
     */
    KNotePartsHalf_Opposite = 7,
    /**
     *二分附点音符
     */
    KNotePartsHalf_dot    = 8,
    /**
     *四分音符
     */
    KNotePartsQuater      = 9,
    /**
     *四分音符反向
     */
    KNotePartsQuater_Opposite = 10,
    /**
     *四分音符附点
     */
    KNotePartsQuater_dot  = 11,
    /**
     *八分音符
     */
    KNotePartsEighth      = 12,
    /**
     *八分音符反向
     */
    KNotePartsEigth_Opposite = 13,
    /**
     *八分音符附点
     */
    KNotePartsEigth_dot    = 14,
    /**
     *十六分音符
     */
    KNotePartsSixth        = 15,
    /**
     *十六分音符反向
     */
    KNotePartsSixth_Opposite = 16,
    /**
     *十六分音符附点
     */
    KNotePartsSixth_dot     = 17,
    /**
     *全音符
     */
    KNotePartsWhole         = 18,
    /**
     *二分休止
     */
    KNotePartsHalf_stop     = 19,
    /**
     *四分休止
     */
    KNotePartsQuater_stop   = 20,
    /**
     *八分休止
     */
    KNotePartsEighth_stop   = 21,
    /**
     *十六分休止
     */
    KNotePartsSixth_stop    = 22,
    /**
     *全休止
     */
    KNotePartsWhole_stop    = 23,
    /**
     *下加、上加横线
     */
    KNotePartsHorLine       = 24,

    /**
     *渐快、渐慢
     */
    KNoteParts_Rit          = 25,
    KNoteParts_Accel        = 26,
    /**
     *强弱
     */
    KNotePartS_PP           = 27,
    KNoteParts_P            = 28,
    KNoteParts_MP           = 29,
    KNoteParts_MF           = 30,
    KNoteParts_F            = 31,
    KNoteParts_FF           = 32,
    /**
     *文本框类型
     */
    KNotePartsText          = 100,
};
/**
 *弧线、直线类型枚举
 */
typedef NS_ENUM(NSInteger,DoubleTapLineType){
    DoubleTapLineNormal  = 0,
    DoubleTapLineHor     = 1,
    DoubleTapLineCurUp   = 2,
    DoubleTapLineCurDown = 3,
    DoubleTapLineFade    = 4,
};

/**
 *阿里云bucket
 */
#define noteBucketName  @"hailun-palette"
/**
 *图片存储临时目录
 */
#define tempPathDirectorySuffix  @"tempPng"
/**
 *临时文件名前缀
 */
#define tempImageNameSuffix @"NotePng:"

#define QHRGB(r, g, b)    [UIColor colorWithRed:(r)/255.f green:(g)/255.f blue:(b)/255.f alpha:1.f]
#define QHRGBA(r, g, b,a) [UIColor colorWithRed:(r)/255.f green:(g)/255.f blue:(b)/255.f alpha:a]

#define defaultFont(s) [UIFont fontWithName:@"HYLeMiaoTiW" size:s]

#define QHKEY_WINDOW      [[UIApplication sharedApplication] keyWindow]
#define QHScreenWidth     [[UIScreen mainScreen] bounds].size.width
#define QHScreenHeight    [[UIScreen mainScreen] bounds].size.height

#define QHIOS_VERSION     [[[UIDevice currentDevice] systemVersion] floatValue]

#ifdef DEBUG
#define QHLog(fmt,...) debugLog((@"\n\n[行号]%d\n" "[函数名]%s\n" "[日志]"fmt"\n"),__LINE__,__FUNCTION__,##__VA_ARGS__);
#else
#define QHLog(fmt,...);
#endif




#endif
