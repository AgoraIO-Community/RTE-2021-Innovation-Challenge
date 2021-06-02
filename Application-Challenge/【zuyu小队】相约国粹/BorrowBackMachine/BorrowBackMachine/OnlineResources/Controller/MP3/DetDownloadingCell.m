//
//  DetDownloadingCell.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/9/22.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#import "DetDownloadingCell.h"

@implementation DetDownloadingCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}



-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _fileNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(15 + 70, 8, 337, 16)];
        
        [self addSubview:_fileNameLabel];
        
        _progress = [[UIProgressView alloc] initWithProgressViewStyle:UIProgressViewStyleDefault];
        _progress.frame = CGRectMake(15 + 40, 33, 300, 2);
        
        [self addSubview:_progress];
        
        
        _progressLabel = [[UILabel alloc] initWithFrame:CGRectMake(15 + 70, 45, 152, 16)];
        
        [self addSubview: _progressLabel];
        
        _speedLabel = [[UILabel alloc] initWithFrame:CGRectMake(228 + 70, 45, 88, 16)];
        
        [self addSubview:_speedLabel];
        
        _downloadBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _downloadBtn.frame = CGRectMake(328 + 70, 22, 40, 40);
        
        
        self.downloadBtn.clipsToBounds = true;
        
        self.downloadBtn.hidden = YES;
        [self.downloadBtn setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
        [self.downloadBtn setImage:[UIImage imageNamed:@"downing_stop" ] forState:UIControlStateNormal];
        [self.downloadBtn setImage:[UIImage imageNamed:@"downing_start" ]  forState:UIControlStateSelected];
        
        [_downloadBtn addTarget:self action:@selector(clickDownload:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:_downloadBtn];
        
        _isXuanImage = [[UIImageView alloc] initWithFrame:CGRectMake(20, 30, 30, 30)];
        
        //        _image.backgroundColor =[UIColor redColor];
        
        [self addSubview:_isXuanImage];
        
        
        
    }
    
    
    return self;
    
    
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

/**
 *  暂停、下载
 *
 *  @param sender UIButton
 */
- (void)clickDownload:(UIButton *)sender {
    // 执行操作过程中应该禁止该按键的响应 否则会引起异常
    sender.userInteractionEnabled = NO;
    ZFFileModel *downFile = self.fileInfo;
    ZFDownloadManager *filedownmanage = [ZFDownloadManager sharedDownloadManager];
    if(downFile.downloadState == ZFDownloading) { //文件正在下载，点击之后暂停下载 有可能进入等待状态
        self.downloadBtn.selected = YES;
        [filedownmanage stopRequest:self.request];
        
    } else {
        self.downloadBtn.selected = NO;
        [filedownmanage resumeRequest:self.request];
    }
    
    // 暂停意味着这个Cell里的ASIHttprequest已被释放，要及时更新table的数据，使最新的ASIHttpreqst控制Cell
    if (self.btnClickBlock) {
        self.btnClickBlock();
    }
    sender.userInteractionEnabled = YES;
    
}



- (void)clickDownload2:(UIButton *)sender {
    // 执行操作过程中应该禁止该按键的响应 否则会引起异常
    sender.userInteractionEnabled = NO;
    ZFFileModel *downFile = self.fileInfo;
    ZFDownloadManager *filedownmanage = [ZFDownloadManager sharedDownloadManager];
    if(downFile.downloadState == ZFDownloading) { //文件正在下载，点击之后暂停下载 有可能进入等待状态
        self.downloadBtn.selected = YES;
        //        [filedownmanage stopRequest:self.request];
        
    } else {
        self.downloadBtn.selected = NO;
        [filedownmanage resumeRequest:self.request];
    }
    
    // 暂停意味着这个Cell里的ASIHttprequest已被释放，要及时更新table的数据，使最新的ASIHttpreqst控制Cell
    if (self.btnClickBlock) {
        self.btnClickBlock();
    }
    sender.userInteractionEnabled = YES;
    
}













- (void)clickDownload3:(UIButton *)sender {
    // 执行操作过程中应该禁止该按键的响应 否则会引起异常
    sender.userInteractionEnabled = NO;
    ZFFileModel *downFile = self.fileInfo;
    ZFDownloadManager *filedownmanage = [ZFDownloadManager sharedDownloadManager];
    if(downFile.downloadState == ZFDownloading) { //文件正在下载，点击之后暂停下载 有可能进入等待状态
        self.downloadBtn.selected = YES;
        [filedownmanage stopRequest:self.request];
        
    } else {
        //        self.downloadBtn.selected = NO;
        //        [filedownmanage resumeRequest:self.request];
    }
    
    // 暂停意味着这个Cell里的ASIHttprequest已被释放，要及时更新table的数据，使最新的ASIHttpreqst控制Cell
    if (self.btnClickBlock) {
        self.btnClickBlock();
    }
    sender.userInteractionEnabled = YES;
    
}







/**
 *  model setter
 *
 *  @param sessionModel sessionModel
 */
- (void)setFileInfo:(ZFFileModel *)fileInfo
{
    _fileInfo = fileInfo;
    self.fileNameLabel.text =  [self decodeString: fileInfo.fileName];
    // 服务器可能响应的慢，拿不到视频总长度
    if (!fileInfo.fileSize) {
        self.progressLabel.text = @"等待下载";
        self.speedLabel.text = @"0B/S";
        self.speedLabel.text = @" ";
        
        return;
    }
    NSString *currentSize = [ZFCommonHelper getFileSizeString:fileInfo.fileReceivedSize];
    NSString *totalSize = [ZFCommonHelper getFileSizeString:fileInfo.fileSize];
    // 下载进度
    float progress = (float)[fileInfo.fileReceivedSize longLongValue] / [fileInfo.fileSize longLongValue];
    
    self.progressLabel.text = [NSString stringWithFormat:@"%@ / %@ (%.2f%%)",currentSize, totalSize, progress*100];
    
    self.progress.progress = progress;
    
    NSString *spped = [NSString stringWithFormat:@"%@/S",[ZFCommonHelper getFileSizeString:[NSString stringWithFormat:@"%lu",[ASIHTTPRequest averageBandwidthUsedPerSecond]]]];
    self.speedLabel.text = spped;
    
    if (fileInfo.downloadState == ZFDownloading) { //文件正在下载
        self.downloadBtn.selected = NO;
    } else if (fileInfo.downloadState == ZFStopDownload&&!fileInfo.error) {
        self.downloadBtn.selected = YES;
        self.speedLabel.text = @"已暂停";
    }else if (fileInfo.downloadState == ZFWillDownload&&!fileInfo.error) {
        self.downloadBtn.selected = YES;
        self.speedLabel.text = @"等待下载";
    } else if (fileInfo.error) {
        self.downloadBtn.selected = YES;
        self.speedLabel.text = @"错误";
    }
}


#pragma mark - decodeURI
-(NSString *)decodeString:(NSString*)encodedString

{
    //NSString *decodedString = [encodedString stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding ];
    
    NSString *decodedString  = (__bridge_transfer NSString *)CFURLCreateStringByReplacingPercentEscapesUsingEncoding(NULL,
                                                                                                                     (__bridge CFStringRef)encodedString,
                                                                                                                     CFSTR(""),
                                                                                                                     CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding));
    return decodedString;
}



@end
