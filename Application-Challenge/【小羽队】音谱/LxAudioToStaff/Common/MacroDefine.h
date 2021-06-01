//
//  MacroDefine.h
//  SmartPiano
//
//  Created by Xytec on 16/3/19.
//  Copyright © 2016年 XiYun. All rights reserved.
//

#ifndef MacroDefine_h
#define MacroDefine_h

/** *********************************************   应用相关  *********************************************  **/
#define mAppThemeColrHex @"#425159"
#define mAppViewBgColorHex @"#F1F1F1"
#define mAppIconColorHex @"#49575F"

#define UIColorFromRGB(r,g,b) [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1]
#define UIColorFromHexStr(aStr) [UIColor dy_colorWithHexString:aStr]

/** *********************************************   API简写  *********************************************  **/
#define WF __weak __typeof(self) weakSelf= self

//----------方法简写-------
#define mAppDelegate        ((AppDelegate*)[[UIApplication sharedApplication] delegate])
#define mWindow             [[UIApplication sharedApplication] keyWindow]
#define mKeyWindow          [[UIApplication sharedApplication] keyWindow]
#define mUserDefaults       [NSUserDefaults standardUserDefaults]
#define mNotificationCenter [NSNotificationCenter defaultCenter]

//----------本地缓存文件路径-----
#define kResourceCachePath [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject]
#define kResourceTempPath NSTemporaryDirectory()

//----------页面设计相关-------
#define mStatusBarHeight      20
#define mNavBarHeight         44
#define mTabBarHeight         49
#define mScreenWidth          ([UIScreen mainScreen].bounds.size.width)
#define mScreenHeight         ([UIScreen mainScreen].bounds.size.height)

#define sX                    self.view.frame.origin.x
#define sY                    self.view.frame.origin.y
#define sWidth                self.view.frame.size.width
#define sHeight               self.view.frame.size.height
#define mScale                [UIScreen mainScreen].scale

//加载图片
#define mImageByName(name)        [UIImage imageNamed:name]
#define mImageByPath(name, ext)   [UIImage imageWithContentsOfFile:[[NSBundle mainBundle]pathForResource:name ofType:ext]]

//度弧度转换
#define mDegreesToRadian(x)      (M_PI * (x) / 180.0)
#define mRadianToDegrees(radian) (radian*180.0) / (M_PI)

//颜色转换
#define mRGBColor(r, g, b)         [UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:1.0]
#define mRGBAColor(r, g, b, a) [UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:a]
//rgb颜色转换（16进制->10进制）
#define mRGBToColor(rgb) [UIColor colorWithRed:((float)((rgb & 0xFF0000) >> 16))/255.0 green:((float)((rgb & 0xFF00) >> 8))/255.0 blue:((float)(rgb & 0xFF))/255.0 alpha:1.0]

#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue & 0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]


//CoreSVP
#define mCoreSVPCenter(text) \
[CoreSVP showSVPWithType:CoreSVPTypeCenterMsg \
Msg:text duration:2 allowEdit:NO \
beginBlock:nil completeBlock:nil];

#define mCoreSVPBottom(text) \
[CoreSVP showSVPWithType:CoreSVPTypeBottomMsg \
Msg:text duration:2 allowEdit:NO \
beginBlock:nil completeBlock:nil];

//G－C－D
#define kGCDBackground(block) dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), block)
#define kGCDMain(block)       dispatch_async(dispatch_get_main_queue(),block)

//是否为空
#define mIsNull(field) (!field || [field isEqual:[NSNull null]] || [field isEqualToString:@"(null)"] || [field isEqualToString:@""])

//----------设备系统相关---------
#define mRetina   ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 960), [[UIScreen mainScreen] currentMode].size) : NO)
#define mIsiP5    ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 1136),[[UIScreen mainScreen] currentMode].size) : NO)
#define mIsiP6    ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(750, 1334),[[UIScreen mainScreen] currentMode].size) : NO)
#define mIsiP6Plus ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? (CGSizeEqualToSize(CGSizeMake(1242, 2208),[[UIScreen mainScreen] currentMode].size))||(CGSizeEqualToSize(CGSizeMake(1125, 2001),[[UIScreen mainScreen] currentMode].size)) : NO)
#define mIsiPad    ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(1536, 2048),[[UIScreen mainScreen] currentMode].size) : NO)
#define mIsiPadmini    ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(768, 1024),[[UIScreen mainScreen] currentMode].size) : NO)
#define mIsPad    (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
#define mIsiphone (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)

#define mSystemVersion   ([[[UIDevice currentDevice] systemVersion] floatValue])
#define mCurrentLanguage ([[NSLocale preferredLanguages] objectAtIndex:0])
//#define mAPPVersion      [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"]
//
//#define mAPPbuild        [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]
//#define mAppFullVersion  [NSString stringWithFormat:@"%@.%@",mAPPVersion,mAPPbuild]

#define notFirstLaunch   mAPPVersion
#define mFirstLaunch     [mUserDefaults boolForKey:@"firstLaunch"]  //以系统版本来判断是否是第一次启动，包括升级后启动。
#define mFirstRun        [mUserDefaults boolForKey:@"firstRun"]

#define IsTeacher  ([mUserDefaults boolForKey:@"IsTeacher"] && [YDUserInfo sharedYDUserInfo].controlStudents )     //是否是老师

#define IsTeacherLogin [mUserDefaults boolForKey:@"IsTeacher"]

#define mIsBeControlByTeacher         ([YDUserInfo sharedYDUserInfo].beControlByTeacher == YES)

//连接蓝牙的identifier
#define mIsReCreate      [mUserDefaults boolForKey:@"isReCreate"]   //是否需要重新创建tabview

#define IOS7_OR_LATER    ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0)
#define IOS8_OR_LATER    ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)

#define mIOS_Version  [[[UIDevice currentDevice] systemVersion] floatValue]
#define mIOS_Version_Up_10  ([[[UIDevice currentDevice] systemVersion] floatValue] >= 10.0)
//--------调试相关-------

//ARC
#if __has_feature(objc_arc)
//compiling with ARC
#else
#define mSafeRelease(object)     [object release];  x=nil
#endif

//调试模式下输入debugLog，发布后不再输入。
#ifndef __OPTIMIZE__

#define debugLog(...) NSLog(__VA_ARGS__)
#define debugLog(...) NSLog(__VA_ARGS__)
#define debugMethod(fmt, ...) NSLog((@"\n[文件名:%s]\n" "[函数名:%s]\n" "[行号:%d] \n" fmt), __FILE__, __FUNCTION__, __LINE__, ##__VA_ARGS__)
//#define debugLog(...) debugLog(__VA_ARGS__)
//#define debugMethod(fmt, ...) debugLog((@"In %s,%s [Line %d] " fmt), __PRETTY_FUNCTION__,__FILE__,__LINE__,##__VA_ARGS__)

#else
#define debugLog(...) {}
#define debugLog(...) {}
#define debugMethod(...) {}
#endif

#define ckWeakSelf __weak typeof(self) weakSelf = self;
//发送通知
#define postNot(name) [[NSNotificationCenter defaultCenter] postNotificationName:name object:nil];
//发送带参数的通知
#define postNotObj(name, obj) [[NSNotificationCenter defaultCenter] postNotificationName:name object:obj];
//注册通知监听
//#define addObsever(selector,name) [[NSNotificationCenter defaultCenter] addObserver:self selector:selector name:name object:nil];

#endif /* MacroDefine_h */
