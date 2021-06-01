//
//  LxNoteToXmlHelp.h
//  SmartPiano
//
//  Created by DavinLee on 2018/10/23.
//  Copyright © 2018 Ydtec. All rights reserved.
//

#import <Foundation/Foundation.h>
@class CKMakeMusicView;

NS_ASSUME_NONNULL_BEGIN

@interface LxNoteToXmlHelp : NSObject
/**
 *@description 获取作曲曲谱音符信息保存的xml数据源
 **/
+ (NSData *)lx_xmlTransformWithMameMusicView:(CKMakeMusicView *)view
                                   staffName:(NSString *)staffName;
@end

NS_ASSUME_NONNULL_END
