//
//  LxMusicCreationVc.h
//  SmartPiano
//
//  Created by DavinLee on 2018/1/30.
//  Copyright © 2018年 XiYun. All rights reserved.
//


#import "CKMakeMusic.h"

@interface LxMusicCreationVc : UIViewController

@property (nonatomic, strong) CKMakeMusic *makeMusic;
//自由版标记
@property (nonatomic, assign) BOOL isFullFuntion;

@end
