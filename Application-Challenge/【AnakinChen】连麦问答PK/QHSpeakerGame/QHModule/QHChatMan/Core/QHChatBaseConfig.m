//
//  QHChatBaseConfig.m
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/23.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import "QHChatBaseConfig.h"

@implementation QHChatBaseConfig

- (instancetype)init {
    self = [super init];
    if (self) {
        _fontName = nil;
        _cellConfig.fontSize = 10;
        _cellConfig.cellWidth = 0;
        _cellConfig.cellLineSpacing = 1;
        _cellEdgeInsets = UIEdgeInsetsZero;
        
        _chatCountMax = 300;
        _chatCountDelete = 100;
        _chatReloadDuration = 0.2;
        _bLongPress = NO;
        _minimumPressDuration = -1;
        _bOpenScorllFromBottom = NO;
        _hasUnlock = NO;
        _bAutoCellHeight = YES;
        _bInsertReplace = NO;
    }
    return self;
}

// [解析和重写NSObject 的isEqual和hash方法 - 简书](https://www.jianshu.com/p/502c5da1f170)
- (BOOL)isEqualToCellConfig:(QHChatCellConfig)cellConfig {
//    if (cellConfig == NULL) {
//        return NO;
//    }
    
    if (_cellConfig.fontSize != cellConfig.fontSize ||
        _cellConfig.cellWidth != cellConfig.cellWidth ||
        _cellConfig.cellLineSpacing != cellConfig.cellLineSpacing) {
        return NO;
    }
    
    return YES;
}

@end
