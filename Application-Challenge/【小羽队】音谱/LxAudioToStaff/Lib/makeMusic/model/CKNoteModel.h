//
//  CKNoteModel.h
//  SmartPiano
//
//  Created by xy on 2018/4/20.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MacroDefine.h"

typedef NS_ENUM(NSUInteger, DefultNum) {
    NumZero,
    NumOne,
    NumTwo,
    NumThree,
    NumFour,
    NumFives,
    NumSix,
    NumSeven,
    NumEight,
    NumNine,
    NumTen,
};

@interface CKNoteModel : NSObject

/** 音符标记值 */
@property (nonatomic, copy) NSString *midiTag;
/** 双音符标记值 */
@property (nonatomic, copy) NSString *secondMidiTag;
/** 音符时长 */
@property (nonatomic, copy) NSString *duration;
/** 强度 */
@property (nonatomic, assign) NSString *strength;
/** 升降 **/
@property (assign, nonatomic) NSString *alert;
/** 是否是连音符 */
@property (nonatomic, assign) BOOL link;
/** 默认图片 */
@property (nonatomic, copy)  NSString *normalImage;
/** 对显示图片 */
@property (nonatomic, copy) NSString *rightImage;
/** 错显示图片 */
@property (nonatomic, copy) NSString *leftImage;
/** 可操作图片 */
@property (nonatomic, copy) NSString *opreationImage;
/** 视图的位置 */
@property (nonatomic, copy) NSString *frameStr;
@property (nonatomic, copy) NSString *pointStr;

/**
 是否是附点音符
 */
@property (nonatomic, assign) BOOL isDot;

/**
 是否是休止符
 */
@property (nonatomic, assign) BOOL isRest;
/**
 音名
 */
@property (nonatomic, copy) NSString *abcName;

/**
 音符类型
 */
@property (nonatomic, assign) MusicNodeType noteType;


/**
 音符按键时长
 */
@property (nonatomic, assign) CGFloat nodeTime;



@end
