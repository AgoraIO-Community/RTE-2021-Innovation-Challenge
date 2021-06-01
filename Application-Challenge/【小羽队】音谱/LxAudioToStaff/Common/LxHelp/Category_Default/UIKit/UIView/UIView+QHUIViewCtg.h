//
//  UIView+QHUIViewCtg.h
//
//  Created by imqiuhang on 15/7/23.
//  Copyright (c) 2015年 imqiuhang. All rights reserved.
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


@interface UIView (QHUIViewCtg)
@property (nonatomic, assign) CGFloat x;
@property (nonatomic, assign) CGFloat y;
///如需将x,y将向上取整数 如 1.3f~>2.f  则在.m中加上ceilf(x 'or' y);
- (CGFloat)left;
- (void)setLeft:(CGFloat)x;
- (CGFloat)top;
- (void)setTop:(CGFloat)y;
- (CGFloat)right;
- (void)setRight:(CGFloat)right;
- (CGFloat)bottom;
- (void)setBottom:(CGFloat)bottom;
//*
- (CGFloat)centerX;
- (void)setCenterX:(CGFloat)centerX;
- (CGFloat)centerY;
- (void)setCenterY:(CGFloat)centerY;
//*
- (CGFloat)width;
- (void)setWidth:(CGFloat)width;
- (CGFloat)height;
- (void)setHeight:(CGFloat)height;
//*
- (CGPoint)origin;
- (void)setOrigin:(CGPoint)origin;
- (CGSize)size;
- (void)setSize:(CGSize)size;
- (UIImage *)screenshotWithQuality:(CGFloat)imageQuality ;
@end
// 版权属于原作者
// http://code4app.com (cn) http://code4app.net (en)
// 发布代码于最专业的源码分享网站: Code4App.com
