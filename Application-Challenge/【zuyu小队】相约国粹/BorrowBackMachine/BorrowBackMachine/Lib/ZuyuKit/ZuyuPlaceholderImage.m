//
//  ZuyuPlaceholderImage.m
//  ZuyuWebImage
//
//  Created by zuyu on 2018/10/29.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ZuyuPlaceholderImage.h"
#import "AFNetworking.h"
@implementation ZuyuPlaceholderImage
+(void)loadZuyuPlaceholder
{
    /*
     电子
     新闻
     纸质
     */
    
    NSArray *array = [NSArray arrayWithObjects:@"http://resource.cncgroup.net:8020/Content/Images/defalut_coverimageurl.png",
                      @"http://resource.cncgroup.net:8020/Content/Images/news_defalut_coverimageurl.png",
                      @"http://file.cncgroup.net/UploadImgs/appdefault/default_pearer_book.jpg",
                      nil];
    
    
    int i = 1;

    
    for (NSString *url in array) {
        
        NSString *imageName = [NSString stringWithFormat:@"image%d.png",i];
        
        [self deleteFile:imageName];
        [self downloadURL:url withName:imageName];
        
        i++;
        
        NSLog(@"-------->>>>>>>>>>.%d",i);
    }
    
}



+(void)downloadURL:(NSString *) downloadURL withName:(NSString *)imageName
{
    
    
    //1.创建管理者
    AFHTTPSessionManager *manage  = [AFHTTPSessionManager manager];
    
    //2.下载文件
    
    //2.1 创建请求对象
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString: downloadURL]];
    
    NSURLSessionDownloadTask *downloadTask = [manage downloadTaskWithRequest:request progress:^(NSProgress * _Nonnull downloadProgress) {//进度
        
    } destination:^NSURL * _Nonnull(NSURL * _Nonnull targetPath, NSURLResponse * _Nonnull response) {
        
        NSString *caches = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject];
        //拼接文件全路径
        NSString *fullpath = [caches stringByAppendingPathComponent:imageName];
        NSURL *filePathUrl = [NSURL fileURLWithPath:fullpath];
        
        NSLog(@"=============>>>>>>>>>>>>>>>>>>>>>%@",filePathUrl);
        return filePathUrl;
        
    } completionHandler:^(NSURLResponse * _Nonnull response, NSURL * _Nonnull filePath, NSError * _Nonnull error) {
        
        
    }];
    
    //3.启动任务
    [downloadTask resume];
}

+(void)deleteFile:(NSString *)name
{
    
    NSFileManager* fileManager=[NSFileManager defaultManager];
    
    NSString *caches = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject];
    //拼接文件全路径
    NSString *fullpath = [caches stringByAppendingPathComponent:name];
    
    BOOL blHave=[[NSFileManager defaultManager] fileExistsAtPath:fullpath];
    
    if (!blHave) {
        
        return ;
        
    }else {
        
        BOOL blDele= [fileManager removeItemAtPath:fullpath error:nil];
        
        if (blDele) {
            NSLog(@"缺省图删除成功");
        }else {
            NSLog(@"缺省图删除失败");
        }
        
    }
    
}


#pragma  mark - 返回缺省图.image
+(UIImage *)returnPlaceholder:(NSInteger )type
{
    NSString *imageName = [NSString stringWithFormat:@"image%ld.png",type];

    NSString *caches = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject];

    NSString *fullpath = [caches stringByAppendingPathComponent:imageName];
    
    UIImage *image = [[UIImage alloc] initWithContentsOfFile:fullpath];
    return image;
}






@end
