//
//  ZFDownloadingCell.h
//
// Copyright (c) 2016年 任子丰 ( http://github.com/renzifeng )
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

#import <UIKit/UIKit.h>
#import "ZFDownloadManager.h"

typedef void(^ZFBtnClickBlock)(void);


@interface ZFDownloadingCell : UITableViewCell
@property (strong, nonatomic)  UILabel *fileNameLabel;
@property (strong, nonatomic)  UIProgressView *progress;

@property (strong, nonatomic)  UILabel *progressLabel;
@property (strong, nonatomic)  UILabel *speedLabel;
@property (strong, nonatomic)  UIButton *downloadBtn;


/** 下载按钮点击回调block */
@property (nonatomic, copy  ) ZFBtnClickBlock  btnClickBlock;
/** 下载信息模型 */
@property (nonatomic, strong) ZFFileModel      *fileInfo;
/** 该文件发起的请求 */
@property (nonatomic,retain ) ZFHttpRequest    *request;


//全部继续下载
- (void)clickDownload2:(UIButton *)sender;

//全部暂停
- (void)clickDownload3:(UIButton *)sender;


@end
