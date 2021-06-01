//
//  LxStaffNoteModel.m
//  SmartPiano
//
//  Created by DavinLee on 2018/1/2.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxStaffNoteModel.h"
#import <NSObject+YYModel.h>

@implementation LxStaffNoteModel
#pragma mark - OverWrith
- (void)setStep:(NSString *)step
{
    _step = step;
    [self checkAndSetMidi];
    
}

- (void)setOctave:(NSInteger)octave
{
    _octave = octave;
    [self checkAndSetMidi];
    
}

- (NSInteger)midiTag
{
    if (_alter > 0 ) {
        return _midiTag + _alter;
    }else if (_alter < 0 )
    {
        return _midiTag + _alter;
    }else
    {
        return _midiTag;
    }
}

- (NSInteger)realMidiTag
{
    return _midiTag;
}
#pragma mark - CallFunction



#pragma mark - Function
- (void)checkAndSetMidi
{
    if (_step && _octave > 0) {
        _midiTag =  [LxStaffNoteModel miditagWithStep:_step octave:_octave];
    }
}

+ (NSInteger)miditagWithStep:(NSString *)step octave:(NSInteger)octave
{
    return (octave - 1) * 12 + 23 + [LxStaffNoteModel stepStrToInteger:step];
}

+ (NSInteger)stepStrToInteger:(NSString *)step
{
    NSDictionary *dic = @{@"C":@(1),
                          @"D":@(3),
                          @"E":@(5),
                          @"F":@(6),
                          @"G":@(8),
                          @"A":@(10),
                          @"B":@(12)
                          };
    return [dic[step] integerValue];
}

+ (NSInteger)octaveWithMidiTag:(NSInteger)midiTag;
{
    return (midiTag - 23) % 12;
}
#pragma mark - CallFunction
/**
 *@description 获取音符数组
 **/
+ (NSArray <LxStaffNoteModel *>*)lx_getModelsWithInfoArray:(NSArray *)infoArray
{
    NSMutableArray <LxStaffNoteModel *>*tempArray = [[NSMutableArray alloc] init];
    for (NSDictionary *info in infoArray) {
        LxStaffNoteModel *model = [LxStaffNoteModel modelWithDictionary:info];
        if (model) {
            [tempArray addObject:model];
        }
    }
    return tempArray;
}

@end
